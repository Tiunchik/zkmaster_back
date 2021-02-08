package org.zkmaster.backend.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Aspect {@link Log} annotation processor.
 * Print to console marked method signature + method's args.
 *
 * @author Daniils Loputevs.
 * @version 1.1.
 * @see Log
 */
@Aspect
@Component
public class LogAspect {
    // Define color constants
    private static final String TEXT_RESET = "\u001B[0m";
    private static final String TEXT_BLACK = "\u001B[30m";
    private static final String TEXT_RED = "\u001B[31m";
    private static final String TEXT_GREEN = "\u001B[32m";
    private static final String TEXT_YELLOW = "\u001B[33m";
    private static final String TEXT_BLUE = "\u001B[34m";
    private static final String TEXT_PURPLE = "\u001B[35m";
    private static final String TEXT_CYAN = "\u001B[36m";
    private static final String TEXT_GREY = "\u001B[37m";
    
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    
    
    @Around(value = "@annotation(org.zkmaster.backend.aop.Log)")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        String args = argsToString(pjp.getArgs());
        String msgCore = new Timestamp(System.currentTimeMillis())
                + TEXT_RED + "  AOP LOG"
                + TEXT_RESET + " --- "
                + TEXT_BLUE + "[" + signature.getDeclaringType().getSimpleName() + "] "
                + TEXT_CYAN + signature.getName() + '(' + args + ")"
                + TEXT_YELLOW + "  ::  ";
        
        System.out.println(msgCore + "START" + TEXT_RESET);
        var rsl = pjp.proceed();
        System.out.println(msgCore + "END" + TEXT_RESET);
        return rsl;
    }
    
    private String argsToString(Object[] args) {
        StringBuilder rsl = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            rsl.append(TEXT_PURPLE);
            rsl.append("\"");
            rsl.append(args[i]);
            rsl.append("\"");
            if (i != args.length - 1) {
                rsl.append(", ");
            }
        }
        return rsl.toString();
    }
    
}
