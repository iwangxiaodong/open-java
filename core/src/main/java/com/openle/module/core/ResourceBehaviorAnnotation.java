package com.openle.module.core;

import com.openle.module.core.Behavior.EnumerableBehavior;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Unused
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ResourceBehaviorAnnotation {

    EnumerableBehavior value();
}
