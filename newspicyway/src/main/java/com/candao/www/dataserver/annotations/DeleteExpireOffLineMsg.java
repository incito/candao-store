package com.candao.www.dataserver.annotations;

import java.lang.annotation.*;

/**
 * Created by ytq on 2016/4/19.
 */
@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface DeleteExpireOffLineMsg {
    String value() default "";
}
