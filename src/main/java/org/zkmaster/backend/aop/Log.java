package org.zkmaster.backend.aop;

import org.springframework.stereotype.Service;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom Spring AOP annotation - mark any method for add simple Logging.
 * Work only with {@link Service} with scope Singleton.
 *
 * @see LogAspect
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface Log {
}
