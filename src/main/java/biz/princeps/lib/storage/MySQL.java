package biz.princeps.lib.storage;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by spatium on 11.06.17.
 */
public abstract class MySQL extends Database {

    protected HikariDataSource ds;

    public MySQL(String hostname, int port, String database, String username, String password) {
        super(DatabaseType.MySQL);
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(MysqlDataSource.class.getName());

        config.addDataSourceProperty("serverName", hostname);
        config.addDataSourceProperty("port", port);
        config.addDataSourceProperty("databaseName", database);
        config.addDataSourceProperty("user", username);
        config.addDataSourceProperty("password", password);

        ds = new HikariDataSource(config);
        setupDatabase();
    }

    @Override
    protected Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            logger.logWarn("Error while trying to pull a new connection: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void close() {
        ds.close();
    }
}
