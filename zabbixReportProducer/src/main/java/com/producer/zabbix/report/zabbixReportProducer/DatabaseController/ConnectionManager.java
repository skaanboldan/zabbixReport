package com.producer.zabbix.report.zabbixReportProducer.DatabaseController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    public Connection connect() throws SQLException, IOException {
        try (InputStream input = new FileInputStream("src/main/resources/application.properties")) {
            Properties prop = new Properties();
            prop.load(input);
                //System.out.println(prop.getProperty("zabbix.ip"));
        return DriverManager.getConnection(prop.getProperty("test.db.url")+prop.getProperty("test.db.schema"), prop.getProperty("test.db.user"), prop.getProperty("test.db.password"));}
        catch (Exception e)
        {
            throw e;
        }
    }
}
