package com.dayu.management.module.user.helper;

import com.dayu.management.core.Query;
import com.dayu.management.module.user.mapper.SessionMapper;
import com.dayu.management.module.user.model.Session;
import com.google.common.base.Preconditions;
import com.google.common.cache.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.leus.common.base.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SessionManager {

    @Autowired
    private SessionMapper mapper;

    @Value("${management.session.duration:180000}")
    private int duration;

    private final ScheduledExecutorService scheduled;

    private final LoadingCache<String, Optional<Session>> sessions;

    private SessionManager() {
        scheduled = Executors.newSingleThreadScheduledExecutor();
        sessions = CacheBuilder
                .newBuilder()
                .expireAfterAccess(duration, TimeUnit.MINUTES)
                .maximumSize(5000)
                .removalListener(new SessionRemovalListener())
                .build(new CacheLoader<String, Optional<Session>>() {
                    @Override
                    public Optional<Session> load(String id) throws Exception {
                        List<Session> sessions = mapper.select(Query.create(1, 0).set("id", id));
                        return Objects.isNullOrEmpty(sessions) ? Optional.empty() : Optional.of(sessions.get(0));
                    }
                });
    }


    public Session save(Session session) {
        session.setExpireTime(System.currentTimeMillis() + session.getDuration());
        int i = mapper.insert(Lists.newArrayList(session));
        Preconditions.checkState(i > 0, "保存Session失败");
        sessions.put(session.getId(), Optional.of(session));
        return session;
    }

    public void expire(String sessionId) {
        sessions.invalidate(sessionId);
    }

    public void keepAlive(Session session) {
        session.setExpireTime(session.getDuration() + System.currentTimeMillis());
        sessions.put(session.getId(), Optional.of(session));
    }

    public Session getSession(String sessionId) {
        Optional<Session> optional = sessions.getUnchecked(sessionId);
        if (optional != null && optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public void setAttribute(String sessionId, Map<String, Object> value) {
        Session session = getSession(sessionId);
        Preconditions.checkState(session != null, "Session已经过期");
        session.setValue(value);
        save(session);
    }


    private class SessionRemovalListener implements RemovalListener<String, Optional<Session>> {

        private final Map<String, Long> expireTimes;

        private final ExecutorService single = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("session-update-thread");
            return thread;
        });

        private SessionRemovalListener() {
            expireTimes = Maps.newConcurrentMap();
            //每5分钟向数据库更新一次
            scheduled.scheduleWithFixedDelay(() -> {
                synchronized (expireTimes) {
                    if (expireTimes.isEmpty()) {
                        return;
                    }
                    List<Session> sessions = expireTimes
                            .entrySet()
                            .stream()
                            .map(e -> Session.builder().id(e.getKey()).expireTime(e.getValue()).build())
                            .collect(Collectors.toList());
                    single.submit(() -> mapper.update(sessions));
                    expireTimes.clear();
                }
            }, 5, 5, TimeUnit.MINUTES);
        }


        @Override
        public void onRemoval(RemovalNotification<String, Optional<Session>> removalNotification) {
            Optional<Session> optional = removalNotification.getValue();
            if (!optional.isPresent()) {
                return;
            }
            Session session = optional.get();
            switch (removalNotification.getCause()) {
                case REPLACED:
                    handleUpdate(session);
                    break;
                case EXPLICIT:
                case EXPIRED:
                    handleDel(session);
                    break;
            }
        }

        private void handleUpdate(Session session) {
            //Session 更新后 老的Session被Replace,此时需要更新库里的过期时间,但是又不能太频繁
            //写在一个缓冲区
            synchronized (expireTimes) {
                expireTimes.put(session.getId(), session.getDuration() + System.currentTimeMillis());
            }
        }

        private void handleDel(Session session) {
            //从数据库中删除Session
            int i = mapper.delete(Lists.newArrayList(session.getId()));
            if (i <= 0) {
                log.warn("删除Session不成功,可能已经被移除");
            }
        }
    }
}
