package biz.princeps.lib.storage.requests;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by spatium on 19.07.17.
 */
public class Conditions {

    private Map<String, Object> conditions;

    protected Conditions(Map<String, Object> conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {

        StringBuilder queryBuilder = new StringBuilder();

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
        return queryBuilder.toString();
    }


    public static class Builder {
        private Map<String, Object> conditions;

        public Builder() {
            conditions = new HashMap<>();
        }

        public Builder addCondition(String column, Object value) {
            conditions.put(column, value);
            return this;
        }

        public Conditions create() {
            return new Conditions(conditions);
        }


    }

}
