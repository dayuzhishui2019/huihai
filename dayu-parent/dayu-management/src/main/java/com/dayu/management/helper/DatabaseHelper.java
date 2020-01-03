package com.dayu.management.helper;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

@Slf4j
@Component
public class DatabaseHelper {

    private CopyManager manager;

    @Autowired
    public DatabaseHelper(DataSource dataSource) throws SQLException {
        BaseConnection connection = dataSource.getConnection().unwrap(BaseConnection.class);
        manager = new CopyManager(connection);
    }


    public void copyIn(String tableName, InputStream input) {
        try {
            manager.copyIn("COPY " + tableName + " FROM STDIN", input);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void copyOut(String tableOrQuery, OutputStream output) {
        try {
            manager.copyOut("COPY " + tableOrQuery + " TO STDOUT", output);
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
