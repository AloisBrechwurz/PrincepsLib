package biz.princeps.lib.storage;

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


}
