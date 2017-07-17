package biz.princeps.lib.storage;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by spatium on 11.06.17.
 */
abstract class MySQL extends AbstractDatabase {

    protected HikariDataSource ds;

    public MySQL(Logger logger, String hostname, int port, String database, String username, String password) {
        super(logger);
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
            logger.warning("Error while trying to pull a new connection: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void close() {
        ds.close();
    }
}
