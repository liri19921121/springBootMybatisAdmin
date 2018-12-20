package com.admin.annotation;

import com.admin.entity.PermissionType;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    String url() default "list";
    PermissionType type() default PermissionType.QUERY;
}
