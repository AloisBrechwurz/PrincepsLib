package biz.princeps.lib.storage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by spatium on 11.06.17.
 */
public abstract class SQLite extends Database {

    private String dbpath;
    private Connection sqlConnection;

    public SQLite(String dbpath) {
        super(DatabaseType.SQLite);
        this.dbpath = dbpath;
        this.initialize();
    }

    private void initialize() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            getLogger().logWarn("The JBDC library for your database type was not found. Please read the plugin's support for more information.");
        }
        Connection conn = getConnection();
        if (conn == null) {
            getLogger().logWarn("Could not establish SQLite Connection");
        }
        this.setupDatabase();
    }

    private Connection createSQLiteConnection() throws SQLException {

        File dbfile = new File(dbpath);
        try {
            if (dbfile.createNewFile()) {
                getLogger().logInfo("Successfully created database file.");
            }
        } catch (IOException e) {
            getLogger().logWarn("Error while creating database file: ", e);
        }
        return DriverManager.getConnection("jdbc:sqlite:" + dbfile);
    }

    @Override
    protected Connection getConnection() {
        // Check if Connection was not previously closed.
        try {
            if (sqlConnection == null || sqlConnection.isClosed()) {
                sqlConnection = this.createSQLiteConnection();
            }
        } catch (SQLException e) {
            getLogger().logWarn("Error while attempting to retrieve connection to database: ", e);
        }
        return sqlConnection;
    }

    @Override
    public void close() {
        try {
            this.sqlConnection.close();
        } catch (SQLException e) {
            getLogger().logWarn("Could not close connection :c", e);
        }
    }


}

