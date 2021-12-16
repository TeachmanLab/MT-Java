package org.mindtrails.domain.questionnaire;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //can use on fields only.
public @interface MeasureField {
    String desc();
    String group() default "";
    String groupDesc() default "";
    int order() default 0;
    String prefNotAns() default "";
}
