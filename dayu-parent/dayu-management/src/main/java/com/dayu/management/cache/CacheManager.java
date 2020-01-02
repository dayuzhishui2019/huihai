package com.dayu.management.cache;

import com.dayu.management.module.user.mapper.UserMapper;
import com.dayu.management.module.user.model.User;
import com.dayu.management.utils.ApplicationContextUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface CacheManager {

    LoadingCache<String, Optional<User>> USER = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .maximumSize(100)
            .build(new CacheLoader<String, Optional<User>>() {
                @Override
                public Optional<User> load(String userId) throws Exception {
                    UserMapper mapper = ApplicationContextUtils.getInstance(UserMapper.class);
                    return Optional.ofNullable(mapper.get(userId));
                }
            });


}
