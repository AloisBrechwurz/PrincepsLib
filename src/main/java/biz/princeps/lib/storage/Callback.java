package biz.princeps.lib.storage;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by spatium on 15.07.17.
 */
public interface Callback {

    void runAfter(ResultSet resultSet) throws SQLException;
}
