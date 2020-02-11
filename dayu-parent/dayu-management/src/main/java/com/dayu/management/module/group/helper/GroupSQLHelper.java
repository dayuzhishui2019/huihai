package com.dayu.management.module.group.helper;

import com.google.common.base.Joiner;

import java.util.List;

public class GroupSQLHelper {

    private static final Joiner joiner = Joiner.on("','");

    public static final String SELECTONLYLEAFBYPARENTID = "select\n" +
            "ss.*,\n" +
            "mvc.ip, mvc.port , mvc.username , mvc.\"password\" , mvc.channels\n" +
            "from\n" +
            "(\n" +
            "select\n" +
            "s.\"id\",\n" +
            "s.gid ,\n" +
            "r.\"groupId\" \"parentId\",\n" +
            "s.\"areaNumber\" ,\n" +
            "s.\"dominionCode\" ,\n" +
            "s.\"type\",\n" +
            "s.\"func\" \"func\"\n" +
            "from\n" +
            "group_sensor_relation r,\n" +
            "sensor s\n" +
            "where\n" +
            "1 = 1\n" +
            "and s.\"id\" = r.\"sensorId\"\n" +
            "and \"groupId\" in ('%s') ) ss\n" +
            "left join ma_view_camera mvc on\n" +
            "ss.id = mvc.\"sensorId\"\n";

    public static final String SELECTBYNODEID = "select\n" +
            "ss.*,\n" +
            "mvc.ip, mvc.port , mvc.username , mvc.\"password\" , mvc.channels\n" +
            "from\n" +
            "(\n" +
            "select\n" +
            "s.\"id\",\n" +
            "s.gid ,\n" +
            "r.\"groupId\" \"parentId\",\n" +
            "s.\"areaNumber\" ,\n" +
            "s.\"dominionCode\" ,\n" +
            "s.\"type\",\n" +
            "s.\"func\" \"func\"\n" +
            "from\n" +
            "group_sensor_relation r,\n" +
            "sensor s\n" +
            "where\n" +
            "1 = 1\n" +
            "and s.\"id\" = r.\"sensorId\"\n" +
            "and s.\"id\" in ('%s') ) ss\n" +
            "left join ma_view_camera mvc on\n" +
            "ss.id = mvc.\"sensorId\"\n";
    ;

    public static final String SELECT_GROUP = "select \"id\",\"parentId\" from \"group\"";


    private GroupSQLHelper() {
    }


    public static String selectByNodeId(List<String> nodeIds) {
        return String.format(SELECTBYNODEID, joiner.join(nodeIds));
    }


    public static String selectOnlyLeafByParentId(List<String> parentIds) {
        return String.format(SELECTONLYLEAFBYPARENTID, joiner.join(parentIds));
    }

    public static void main(String[] args) {
        System.out.println(',');
    }
}
