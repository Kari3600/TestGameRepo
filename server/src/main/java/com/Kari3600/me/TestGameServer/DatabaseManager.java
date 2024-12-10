package com.Kari3600.me.TestGameServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseManager {

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        config.setJdbcUrl( "jdbc:mariadb://localhost:3306/testgamedatabase" );
        config.setUsername( "TestGameServer" );
        config.setPassword( "abc" );
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        ds = new HikariDataSource( config );
        try {
			Connection conn = ds.getConnection();
			Statement statement = conn.createStatement();
			//statement.execute("CREATE TABLE IF NOT EXISTS Users (Username VARCHAR(20) NOT NULL, Password VARCHAR(20) NOT NULL, PRIMARY KEY(Username))");
			//statement.execute("CREATE INDEX IF NOT EXISTS idx_username ON Users(Username)");
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    private static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static CompletableFuture<ResultSet> executeQuery(String querry, Object... args) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Connection conn = getConnection();
                PreparedStatement stat = conn.prepareStatement(querry);
                int c = 1;
                for (Object obj : args) {
                    stat.setObject(c,obj);
                    c+=1;
                }
                return stat.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public static CompletableFuture<Integer> executeUpdate(String querry, Object... args) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Connection conn = getConnection();
                PreparedStatement stat = conn.prepareStatement(querry);
                int c = 1;
                for (Object obj : args) {
                    stat.setObject(c,obj);
                    c+=1;
                }
                return stat.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
    
}
