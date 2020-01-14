package com.dayu.management.module.group.helper;

import com.google.common.base.Joiner;

import java.util.List;

public class GroupSQLHelper {

    private static final Joiner joiner = Joiner.on("','");

    public static final String SELECTONLYLEAFBYPARENTID = "SELECT s.\"id\",s.\"name\", \"groupId\" \"parentId\",2 \"nodeType\",s.\"type\",s.\"func\" \"func\" FROM group_sensor_relation r,sensor s WHERE 1 = 1 AND s.\"id\" = r.\"sensorId\" AND \"groupId\" IN ('%s')";

    public static final String SELECTBYNODEID = "SELECT s.\"id\",s.\"name\", \"groupId\" \"parentId\",2 \"nodeType\",s.\"type\",s.\"func\" \"func\" FROM group_sensor_relation r,sensor s WHERE 1 = 1 AND s.\"id\" = r.\"sensorId\" AND s.\"id\" IN ('%s')";

    public static final String SELECT_GROUP = "select \"id\",\"parentId\" from \"group\"";


    private GroupSQLHelper() {
    }


    public static String selectByNodeId(List<String> nodeIds) {
        return String.format(SELECTBYNODEID, joiner.join(nodeIds));
    }


    public static String selectOnlyLeafByParentId(List<String> parentIds) {
        return String.format(SELECTONLYLEAFBYPARENTID, joiner.join(parentIds));
    }
}
