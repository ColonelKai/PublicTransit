package org.colonelkai.publictransit.options;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandOption {

    @NotNull Class<?> argumentType() default Object.class;

    @NotNull String name() default "";

    @NotNull String setter() default "";

}
