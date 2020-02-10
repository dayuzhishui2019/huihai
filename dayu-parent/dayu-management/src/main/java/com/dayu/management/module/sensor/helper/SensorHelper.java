package com.dayu.management.module.sensor.helper;

import com.dayu.management.constant.StandingBook;
import com.dayu.management.module.sensor.model.CompareView;
import com.dayu.management.module.sensor.model.DiverseSensorView;
import com.dayu.management.module.sensor.model.LineView;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class SensorHelper {


    // 分类 用于处理台账 导入
    public DiverseSensorView diverse(int hash, Stream<String> older, Stream<String> newer) {

        Multimap<Integer, String> olderHash = hashingBuild(hash, older);
        Multimap<Integer, String> newerHash = hashingBuild(hash, newer);

        List<String> olderList = Lists.newArrayList();
        List<String> newerList = Lists.newArrayList();

        IntStream.range(0, hash).forEach(hashKey -> {
            CompareView view = getUnSame(olderHash.get(hashKey), newerHash.get(hashKey));
            if (!view.getOlder().isEmpty()) {
                olderList.addAll(view.getOlder());
            }
            if (!view.getNewer().isEmpty()) {
                newerList.addAll(view.getNewer());
            }
        });

        Splitter splitter = Splitter.on(",").trimResults();

        Map<String, LineView> relation = Maps.newHashMap();

        //fill
        olderList.forEach(line -> {
            String gid = splitter.trimResults().splitToList(line).get(StandingBook.GID);
            relation.put(gid, relation.getOrDefault(gid, new LineView(gid, line, null)));
        });

        // fill append
        newerList.forEach(line -> {
            String gid = splitter.trimResults().splitToList(line).get(StandingBook.GID);
            LineView v = relation.getOrDefault(gid, new LineView(gid, null, line));
            v.setNewerLine(line);
            relation.put(gid, v);
        });

        DiverseSensorView view = new DiverseSensorView();

        relation.values().forEach(lineView -> {
            if (lineView.getOlderLine() == null && lineView.getNewerLine() == null) {
                return;
            }
            if (lineView.getOlderLine() != null && lineView.getNewerLine() == null) {
                //老的 也许会被删除的
                return;
            }
            if (lineView.getOlderLine() == null && lineView.getNewerLine() != null) {
                //新增
                view.getInsertLines().put(lineView.getGid(), lineView.getNewerLine());

            }
            if (lineView.getOlderLine() != null && lineView.getNewerLine() != null) {
                //更新 策略: 删除原有的,插入新的
                view.getUpdateLines().put(lineView.getGid(), lineView.getNewerLine());
            }
        });
        return view;
    }


    //hashing划分
    public Multimap<Integer, String> hashingBuild(int hash, Stream<String> lines) {
        Multimap<Integer, String> result = HashMultimap.create();
        lines.forEach(line -> {
            int code = (Objects.hash(line) & Integer.MAX_VALUE) % hash;
            result.put(code, line);
        });
        return result;
    }


    //获取两个桶中的不同
    public CompareView getUnSame(Collection<String> older, Collection<String> newer) {
        List<String> nn = Lists.newArrayList(older);
        List<String> mm = Lists.newArrayList(newer);
        CompareView view = new CompareView();
        nn.removeAll(newer);
        mm.removeAll(older);
        view.setOlder(nn);
        view.setNewer(mm);
        older.clear();
        newer.clear();
        return view;
    }
}
