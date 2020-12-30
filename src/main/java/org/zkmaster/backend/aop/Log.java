package org.zkmaster.backend.aop;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom Spring AOP annotation - mark any method for add simple Logging.
 * * DON'T WORK with:
 * static method - cause AOP can't create proxy for static methods.
 * ??? private methods
 *
 * @see LogAspect
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface Log {
}
