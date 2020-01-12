package com.dayu.management.helper;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.*;
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


    public long copyIn(String tableName, InputStream input) throws IOException, SQLException {
        return manager.copyIn("COPY " + tableName + " FROM STDIN DELIMITER AS ','", input);
    }

    public long copyIn(String tableName, Reader input) throws IOException, SQLException {
        return manager.copyIn("COPY " + tableName + " FROM STDIN DELIMITER AS ','", input);
    }

    public long copyOut(String tableOrQuery, OutputStream output) throws IOException, SQLException {
        return manager.copyOut("COPY (" + tableOrQuery + ") TO STDOUT DELIMITER AS ','", output);
    }


    public long copyOut(String tableOrQuery, Writer output) throws IOException, SQLException {
        return manager.copyOut("COPY (" + tableOrQuery + ") TO STDOUT DELIMITER AS ','", output);
    }

}
