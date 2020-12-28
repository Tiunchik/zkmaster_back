package org.zkmaster.backend.annotations;

/**
 * Marker that "real-business code" place in {@param to}.
 * Check this class to see business code.
 */
public @interface Delegate {

    Class<?> to() default Object.class;
}
