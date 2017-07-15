package biz.princeps.lib.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by spatium on 15.07.17.
 */
public abstract class Database {

    protected Logger logger;

    public Logger getLogger() {
        return logger;
    }

    public Database(Logger logger) {
        this.logger = logger;
    }

    public abstract void close();

    protected abstract Connection getConnection();

    protected abstract void setupDatabase();


    public void executeUpdate(String query) {
        executeUpdate(query, null);
    }

    public void execute(String query) {
        execute(query, null);
    }

    public void handleResultSet(String query, Callback callback) {
        handleResultSet(query, callback, null);
    }

    public void executeUpdate(String query, Object... args) {
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(query)) {
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    Object obj = args[i];
                    if (obj instanceof String)
                        st.setString(i + 1, (String) obj);
                    else if (obj instanceof Integer)
                        st.setInt(i + 1, (int) obj);
                    else if (obj instanceof Double)
                        st.setDouble(i + 1, (double) obj);
                    else if (obj instanceof Float)
                        st.setFloat(i + 1, (float) obj);
                    else if (obj instanceof Boolean)
                        st.setBoolean(i + 1, (boolean) obj);
                    else if (obj instanceof Long)
                        st.setLong(i + 1, (long) obj);
                }
            }
            st.executeUpdate();

        } catch (SQLException e) {
            logger.warning("Error while executing update for query: " + query + "\nError:" + e.getMessage());
        }
    }

    public void execute(String query, Object... args) {
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    Object obj = args[i];
                    if (obj instanceof String)
                        st.setString(i + 1, (String) obj);
                    else if (obj instanceof Integer)
                        st.setInt(i + 1, (int) obj);
                    else if (obj instanceof Double)
                        st.setDouble(i + 1, (double) obj);
                    else if (obj instanceof Float)
                        st.setFloat(i + 1, (float) obj);
                    else if (obj instanceof Boolean)
                        st.setBoolean(i + 1, (boolean) obj);
                    else if (obj instanceof Long)
                        st.setLong(i + 1, (long) obj);
                }
            }
            st.execute();

        } catch (SQLException e) {
            logger.warning("Error while executing query: " + query + "\nError:" + e.getMessage());
        }
    }

    public void handleResultSet(String query, Callback callback, Object... args) {
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(query)) {

            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    Object obj = args[i];
                    if (obj instanceof String)
                        st.setString(i + 1, (String) obj);
                    else if (obj instanceof Integer)
                        st.setInt(i + 1, (int) obj);
                    else if (obj instanceof Double)
                        st.setDouble(i + 1, (double) obj);
                    else if (obj instanceof Float)
                        st.setFloat(i + 1, (float) obj);
                    else if (obj instanceof Boolean)
                        st.setBoolean(i + 1, (boolean) obj);
                    else if (obj instanceof Long)
                        st.setLong(i + 1, (long) obj);
                }
            }

            ResultSet res = st.executeQuery();
            callback.runAfter(res);
            res.close();

        } catch (SQLException e) {
            logger.warning("Error while getting result set for query: " + query + "\nError:" + e.getMessage());
        }
    }

}
