package biz.princeps.lib.storage;

import biz.princeps.lib.PrincepsLib;
import biz.princeps.lib.storage.annotation.Column;
import biz.princeps.lib.storage.annotation.Table;
import biz.princeps.lib.storage.annotation.Unique;
import biz.princeps.lib.storage.requests.AbstractRequest;
import biz.princeps.lib.storage.requests.Conditions;
import biz.princeps.lib.util.SpigotUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
        this.requests.setApi(this);
        this.requests.setDb(db);
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

                    // location specific
                    if (field.getType().getSimpleName().equals("Location")) {
                        queryBuilder.append("(");
                        queryBuilder.append(100);
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
                    // experimental location support
                    if (value instanceof Location) {
                        value = SpigotUtil.exactlocationToString((Location) value);
                    }
                    if (value instanceof String || (value instanceof Boolean && db instanceof SQLite))
                        queryBuilder.append("'").append(value).append("'");
                    else if (value instanceof Integer || value instanceof Double || value instanceof Float || value instanceof Long
                            || (value instanceof Boolean && db instanceof MySQL))
                        queryBuilder.append(value);
                    else
                        queryBuilder.append("'").append(value).append("'");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (iteratorValues.hasNext())
                    queryBuilder.append(", ");
            }
            queryBuilder.append(")");
            db.execute(queryBuilder.toString());
        }
    }

    public List<Object> retrieveObjects(Class objectOf, Conditions conditions) {
        // SELECT * FROM tablename WHERE con1 = con1v AND ...
        Table table = (Table) objectOf.getDeclaredAnnotation(Table.class);
        if (table == null)
            return null;

        String query = "SELECT * FROM " + table.name();
        StringBuilder queryBuilder = new StringBuilder(query);

        // conditions
        if (conditions != null) {
            queryBuilder.append(" WHERE ");
            queryBuilder.append(conditions.toString());
        }

        List<Object> objects = new ArrayList<>();
        db.executeQuery(queryBuilder.toString(), (res) -> {
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
                    case "Location":
                        toReturn = SpigotUtil.exactlocationFromString(res.getString(columnName));
                        break;
                    default:
                        toReturn = res.getString(columnName);
                }
            }
        }
        return toReturn;
    }


    public String convertToSQLType(String simpleName) {
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
            case "Location":
                return "VARCHAR";
            default:
                return "VARCHAR";
        }
    }
}
