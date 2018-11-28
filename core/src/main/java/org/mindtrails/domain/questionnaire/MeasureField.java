package org.mindtrails.domain.questionnaire;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //can use on fields only.
public @interface MeasureField {
    public String desc();
    public String group() default "";
    public String groupDesc() default "";
    public int order() default 0;
    public String prefNotAns() default "";
}

