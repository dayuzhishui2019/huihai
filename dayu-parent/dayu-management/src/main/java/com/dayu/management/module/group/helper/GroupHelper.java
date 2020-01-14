package com.dayu.management.module.group.helper;

import com.dayu.management.helper.DatabaseHelper;
import com.google.common.base.Strings;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.leus.common.cache.AsyncCache;
import com.leus.common.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.*;

@Component
public class GroupHelper {

    @Autowired
    private DatabaseHelper helper;

    private LoadingCache<String, Map<String, String>> async = AsyncCache.createDefault((key, oldValue) -> {
        switch (key) {
            case "GROUP":
                try {
                    return loadAllGroup();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            default:
                return null;
        }
    });


    // ID , PARENT_ID
    private Map<String, String> getGroupTree() {
        return async.getUnchecked("GROUP");
    }


    //获取节点下面的所有子组织
    public List<String> getAllBranch(String id) {
        //nodeId - parentId
        Map<String, String> node_parent = getGroupTree();
        Multimap<String, String> parent_node = HashMultimap.create();
        node_parent.forEach((k, v) -> parent_node.put(v, k));
        List<String> result = levelWalkTree(parent_node, id);
        parent_node.clear();
        return result;
    }


    private Map<String, String> loadAllGroup() throws IOException, SQLException {
        //内存计算不落盘,不进行大字符串分割
        Path path = memory.getPath("/pg_tmp/");
        Files.createDirectories(path);
        Path file = path.resolve(UUIDUtil.randomUUIDw() + ".csv");
        Files.createFile(file);
        Writer writer = Files.newBufferedWriter(file, StandardOpenOption.APPEND);
        helper.copyOut(GroupSQLHelper.SELECT_GROUP, writer);
        writer.flush();
        BufferedReader reader = Files.newBufferedReader(file, Charset.forName("utf-8"));
        Map<String, String> groups = Maps.newHashMap();
        reader.lines().filter(line -> !Strings.isNullOrEmpty(line)).forEach(line -> {
            String[] strings = line.split(",");
            groups.put(strings[0], strings[1]);
        });
        Files.deleteIfExists(file);
        return groups;

    }

    private FileSystem memory = Jimfs.newFileSystem("jimfs-memory", Configuration.unix());


    public static List<String> levelWalkTree(Multimap<String, String> parent_node, String groupId) {
        if (groupId == null) {
            return Lists.newArrayList();
        }
        List<String> res = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(groupId);
        while (!queue.isEmpty()) {
            String current = queue.remove();
            res.add(current);
            queue.addAll(parent_node.get(current));

        }
        return res;
    }

}
