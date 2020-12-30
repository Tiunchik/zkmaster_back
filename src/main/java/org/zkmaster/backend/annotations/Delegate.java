package org.zkmaster.backend.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marker that "real-business code" delegated to other class{@param to}.
 * Check this class to see business code.
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface Delegate {

    /**
     * Could be useful if interface have more that one implementation,
     * so you can fill this param to show which implementation delegate logic to other class.
     * Examples:
     * # @Delegate(impl = MainServiceDefault.class, to = HostProvider.class)
     * # @Delegate(impl = MainServiceNew.class, to = HostProviderNew.class)
     *
     * @return implementation class
     */
    Class<?> impl() default Object.class;

    /**
     * Show to which class this class/method delegate logic.
     *
     * @return Delegate class.
     */
    Class<?> to();
}
