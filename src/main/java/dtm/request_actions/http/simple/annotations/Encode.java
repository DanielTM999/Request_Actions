package dtm.request_actions.http.simple.annotations;

import dtm.request_actions.http.simple.core.HttpType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Encode {
    HttpType value() default HttpType.JSON;
}
