package biz.princeps.lib.storage;

import biz.princeps.lib.PrincepsLib;
import biz.princeps.lib.storage.annotation.Column;
import biz.princeps.lib.storage.annotation.Table;
import biz.princeps.lib.storage.annotation.Unique;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.configuration.file.FileConfiguration;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;

/**
 * Created by spatium on 16.07.17.
 */
public class DatabaseAPI {

    private AbstractDatabase db;
    private AbstractRequest requests;

    public DatabaseAPI(DatabaseType type, AbstractRequest requests, String packageName) {
        this(type, PrincepsLib.prepareDatabaseFile(), requests, packageName);
    }

    public DatabaseAPI(DatabaseType type, FileConfiguration mysqlConfig, AbstractRequest requests, String packageName) {
        Logger log = PrincepsLib.getPluginInstance().getLogger();
        this.setRequests(requests);

        switch (type) {
            case SQLite:
                db = new SQLite(log, PrincepsLib.getPluginInstance().getDataFolder() + "/database.db") {
                };
                break;
            case MySQL:
                db = new MySQL(log, mysqlConfig.getString("MySQL.Hostname"),
                        mysqlConfig.getInt("MySQL.Port"),
                        mysqlConfig.getString("MySQL.Database"),
                        mysqlConfig.getString("MySQL.User"),
                        mysqlConfig.getString("MySQL.Password")) {
                };
                break;
        }

        this.scan(packageName);
    }


    public AbstractDatabase getDatabase() {
        return db;
    }

    private void setRequests(AbstractRequest requests) {
        this.requests = requests;
        requests.api = this;
        requests.db = this.db;
    }

    public <T> T req(Class<T> cls) {
        if (cls.isInstance(requests))
            return cls.cast(requests);
        return null;
    }

    private void scan(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Table.class);

        for (Class<?> aClass : annotated) {
            Set<Field> fields = getAllFields(aClass, withAnnotation(Column.class));

            if (fields.size() > 0) {
                Table anno = aClass.getDeclaredAnnotation(Table.class);
                String query = "CREATE TABLE IF NOT EXISTS " + anno.name() + " (";

                StringBuilder queryBuilder = new StringBuilder(query);
                Iterator<Field> iterator = fields.iterator();
                while (iterator.hasNext()) {
                    Field field = iterator.next();
                    Column column = field.getAnnotation(Column.class);
                    queryBuilder.append(column.name());
                    queryBuilder.append(" ");
                    queryBuilder.append(convertToSQLType(field.getType().getSimpleName()));

                    if (column.length() > 0) {
                        queryBuilder.append("(");
                        queryBuilder.append(column.length());
                        queryBuilder.append(")");
                    }

                    if (iterator.hasNext())
                        queryBuilder.append(", ");
                }

                if (db instanceof SQLite) {
                    queryBuilder.append(", PRIMARY KEY(");
                    // doing like this avoids issues if the users defines more @Unique
                    Column uniqueColumn = null;
                    for (Field field : fields) {
                        Unique unique = field.getAnnotation(Unique.class);
                        if (unique != null) {
                            uniqueColumn = field.getAnnotation(Column.class);
                        }
                    }
                    queryBuilder.append(uniqueColumn.name());
                    queryBuilder.append(")");
                }

                queryBuilder.append(")");

                db.execute(queryBuilder.toString());

                // Alter table
                if (db instanceof MySQL)
                    for (Field field : fields) {
                        Unique unique = field.getAnnotation(Unique.class);
                        if (unique != null) {
                            Column uniqueColumn = field.getAnnotation(Column.class);
                            if (uniqueColumn != null) {
                                String alterQuery = "ALTER TABLE " + anno.name() +
                                        " ADD UNIQUE (" + uniqueColumn.name() + ")";
                                db.execute(alterQuery);
                            }
                        }
                    }
            }
        }
    }

    public void saveObject(Object object) {
        Table table = object.getClass().getAnnotation(Table.class);
        if (table != null) {
            String query = "REPLACE INTO " + table.name() + " (";
            StringBuilder queryBuilder = new StringBuilder(query);

            // Spaltenaufführung
            Set<Field> fields = getAllFields(object.getClass(), withAnnotation(Column.class));
            Iterator<Field> iterator = fields.iterator();
            while (iterator.hasNext()) {
                Field field = iterator.next();
                Column column = field.getAnnotation(Column.class);
                queryBuilder.append(column.name());

                if (iterator.hasNext())
                    queryBuilder.append(", ");
            }

            // Values
            queryBuilder.append(") VALUES (");

            Iterator<Field> iteratorValues = fields.iterator();
            while (iteratorValues.hasNext()) {
                Field field = iteratorValues.next();
                try {
                    field.setAccessible(true);
                    Object value = field.get(object);
                    if (value instanceof String || (value instanceof Boolean && db instanceof SQLite))
                        queryBuilder.append("'" + value + "'");
                    else
                        queryBuilder.append(value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (iteratorValues.hasNext())
                    queryBuilder.append(", ");
            }
            queryBuilder.append(")");

            /* ON DUPLICATE KEY try
            queryBuilder.append(") ON DUPLICATE KEY UPDATE ");

            Iterator<Field> newIt = fields.iterator();
            while (newIt.hasNext()) {
                Field field = newIt.next();
                Column column = field.getAnnotation(Column.class);
                try {
                    queryBuilder.append(column.name())
                            .append(" = ")
                            .append(field.get(object));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (newIt.hasNext())
                    queryBuilder.append(", ");
            }
*/
            System.out.println(queryBuilder.toString());
            db.execute(queryBuilder.toString());
        }
    }

    public List<Object> retrieveObjects(Class objectOf, Map<String, Object> conditions) {
        // SELECT * FROM tablename WHERE con1 = con1v AND ...
        Table table = (Table) objectOf.getDeclaredAnnotation(Table.class);
        if (table == null)
            return null;

        String query = "SELECT * FROM " + table.name();
        StringBuilder queryBuilder = new StringBuilder(query);

        // conditions
        if (conditions != null) {
            queryBuilder.append(" WHERE ");

            Iterator<String> it = conditions.keySet().iterator();
            while (it.hasNext()) {
                String next = it.next();
                queryBuilder.append(next)
                        .append(" = ");
                Object nextValue = conditions.get(next);
                if (nextValue instanceof String)
                    queryBuilder.append("'").append(nextValue).append("'");
                else
                    queryBuilder.append(nextValue);

                if (it.hasNext())
                    queryBuilder.append(" AND ");
            }
        }

        List<Object> objects = new ArrayList<>();
        db.handleResultSet(queryBuilder.toString(), (res) -> {
            while (res.next()) {
                Constructor[] constructors = objectOf.getConstructors();
                for (Constructor constructor : constructors) {
                    if (constructor.isAnnotationPresent(biz.princeps.lib.storage.annotation.Constructor.class)) {
                        Parameter[] params = constructor.getParameters();
                        Object[] paramsValues = new Object[params.length];
                        for (int i = 0; i < params.length; i++) {
                            Column column = params[i].getAnnotation(Column.class);
                            String columnName = column.name();
                            Object value = getValue(res, objectOf, columnName);
                            paramsValues[i] = value;
                        }

                        // Call const
                        try {
                            Object constructed = constructor.newInstance(paramsValues);
                            objects.add(constructed);
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
        return objects;
    }

    private Object getValue(ResultSet res, Class obj, String columnName) throws SQLException {
        Set<Field> columns = getAllFields(obj, withAnnotation(Column.class));
        Object toReturn = null;
        for (Field field : columns) {
            Column anno = field.getAnnotation(Column.class);
            if (anno.name().equals(columnName)) {
                switch (field.getType().getSimpleName()) {
                    case "String":
                        toReturn = res.getString(columnName);
                        break;
                    case "int":
                        toReturn = res.getInt(columnName);
                        break;
                    case "long":
                        toReturn = res.getLong(columnName);
                        break;
                    case "boolean":
                        toReturn = res.getBoolean(columnName);
                        break;
                    case "double":
                        toReturn = res.getDouble(columnName);
                        break;
                    case "float":
                        toReturn = res.getFloat(columnName);
                        break;
                    default:
                        toReturn = res.getString(columnName);
                }
            }
        }
        return toReturn;
    }


    private String convertToSQLType(String simpleName) {
        //     System.out.println(simpleName);
        switch (simpleName) {
            case "String":
                return "VARCHAR";
            case "int":
                return "INTEGER";
            case "long":
                return "BIGINT";
            case "boolean":
                return "BOOLEAN";
            case "double":
                return "DOUBLE";
            case "float":
                return "FLOAT";
            default:
                return "VARCHAR";
        }
    }
}
