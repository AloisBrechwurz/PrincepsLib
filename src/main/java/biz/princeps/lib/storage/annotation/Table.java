package biz.princeps.lib.storage.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by spatium on 16.07.17.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    String name();
}
