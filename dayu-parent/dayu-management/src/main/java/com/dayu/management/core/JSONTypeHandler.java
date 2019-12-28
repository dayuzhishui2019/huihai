package com.dayu.management.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(Object.class)
public class JSONTypeHandler extends BaseTypeHandler<Object> {
    private static final PGobject jsonObject = new PGobject();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        jsonObject.setType("json");
        try {
            jsonObject.setValue(new ObjectMapper().writeValueAsString(parameter));  //java对象转化成json字符串
        } catch (IOException e) {
            e.printStackTrace();
        }
        ps.setObject(i, jsonObject);
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);                                 // 返回String
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getString(columnIndex);
    }

}