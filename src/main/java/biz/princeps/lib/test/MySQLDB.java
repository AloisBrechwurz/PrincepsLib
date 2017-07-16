package biz.princeps.lib.test;

import biz.princeps.lib.storage.MySQL;

import java.util.logging.Logger;

/**
 * Created by spatium on 16.07.17.
 */
public class MySQLDB extends MySQL {

    public MySQLDB(Logger logger, String hostname, int port, String database, String username, String password) {
        super(logger, hostname, port, database, username, password);
    }

    @Override
    protected void setupDatabase() {

    }


}
