package biz.princeps.lib.storage.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by spatium on 16.07.17.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    String name();
}
