package com.dayu.management;

import com.dayu.management.constant.StandingBook;
import com.dayu.management.module.sensor.model.CompareView;
import com.dayu.management.module.sensor.model.LineView;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.io.CharSource;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

public class TestMain {


    public static void main(String[] args) throws IOException {

        CharSource file1 = Files.asCharSource(new File("100w.csv"), Charset.forName("utf8"));
        CharSource file2 = Files.asCharSource(new File("10w-2.csv"), Charset.forName("utf8"));
        int hash = 100;
        Multimap<Integer, String> maps = HashMultimap.create();
        Multimap<Integer, String> maps2 = HashMultimap.create();
        file1.lines().forEach(line -> {
            int code = (Objects.hash(line) & Integer.MAX_VALUE) % hash;
            maps.put(code, line);
        });

        file2.lines().forEach(line -> {
            int code = (Objects.hash(line) & Integer.MAX_VALUE) % hash;
            maps2.put(code, line);

        });

        List<String> older = Lists.newArrayList();
        List<String> newer = Lists.newArrayList();
        IntStream.range(0, hash).forEach(k -> {
            CompareView view = getUnSame(Lists.newArrayList(maps.get(k)), Lists.newArrayList(maps2.get(k)));
            if (!view.getOlder().isEmpty()) {
                older.addAll(view.getOlder());
            }
            if (!view.getNewer().isEmpty()) {
                newer.addAll(view.getNewer());
            }
        });

        Splitter splitter = Splitter.on(",");

        Map<String, LineView> relation = Maps.newHashMap();
        older.forEach(line -> {
            String gid = splitter.trimResults().splitToList(line).get(StandingBook.GID);
            relation.put(gid, relation.getOrDefault(gid, new LineView(gid, line, null)));
        });

        newer.forEach(line -> {
            String gid = splitter.trimResults().splitToList(line).get(StandingBook.GID);
            LineView v = relation.getOrDefault(gid, new LineView(gid, null, line));
            v.setNewerLine(line);
            relation.put(gid, v);
        });

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
            }
            if (lineView.getOlderLine() != null && lineView.getNewerLine() != null) {
                //更新 策略: 删除原有的,插入新的
            }
        });

    }

    public static CompareView getUnSame(List<String> a, List<String> b) {
        List<String> nn = Lists.newArrayList(a);
        List<String> mm = Lists.newArrayList(b);
        CompareView view = new CompareView();
        nn.removeAll(b);
        mm.removeAll(a);
        view.setOlder(nn);
        view.setNewer(mm);
        a.clear();
        b.clear();
        return view;


    }
}
