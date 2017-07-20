package biz.princeps.lib.storage.requests;

import biz.princeps.lib.storage.AbstractDatabase;
import biz.princeps.lib.storage.DatabaseAPI;

/**
 * Created by spatium on 17.07.17.
 */
public abstract class AbstractRequest {

    protected DatabaseAPI api;
    protected AbstractDatabase db;

    public DatabaseAPI getDatabaseAPI() {
        return api;
    }

    public AbstractDatabase getDatabase() {
        return db;
    }

    public void setApi(DatabaseAPI api) {
        this.api = api;
    }

    public void setDb(AbstractDatabase db) {
        this.db = db;
    }
}
