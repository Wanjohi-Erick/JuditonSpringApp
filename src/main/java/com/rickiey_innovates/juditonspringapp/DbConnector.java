package com.rickiey_innovates.juditonspringapp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DbConnector {
    
    private static HikariConfig  config = new HikariConfig ();
    private static HikariDataSource ds;

    static {
    	
    	config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/juditon?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8");
    	config.setUsername("root");
    	config.setPassword("walgotech");
    	config.setMaximumPoolSize(100);
        config.setConnectionTimeout(300000);
        config.setConnectionTimeout(120000);
        config.setLeakDetectionThreshold(300000);
        ds = new HikariDataSource( config );
        
    }
    
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    
    private DbConnector(){ }
}