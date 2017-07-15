package biz.princeps.lib.storage;

import com.mysql.cj.core.log.Slf4JLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by spatium on 15.07.17.
 */
public abstract class Database {

    protected Slf4JLogger logger;

    public Slf4JLogger getLogger() {
        return logger;
    }

    public Database(DatabaseType type) {
        logger = new Slf4JLogger(type.name());
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

    public ResultSet getResultSet(String query) {
        return getResultSet(query, null);
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
            logger.logWarn("Error while executing update for query: " + query + "\nError:" + e.getMessage());
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
            logger.logWarn("Error while executing query: " + query + "\nError:" + e.getMessage());
        }
    }

    public ResultSet getResultSet(String query, Object... args) {
        ResultSet res = null;
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

            res = st.executeQuery();

        } catch (SQLException e) {
            logger.logWarn("Error while getting result set for query: " + query + "\nError:" + e.getMessage());
        } finally {
            return res;
        }
    }

}
