package org.zkmaster.backend.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    @Around(value = "@annotation(org.zkmaster.backend.aop.Log)")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();

        String args = argsToString(pjp.getArgs());

        String msgTemplate = "AOP LOG: "
                + signature.getDeclaringType().getSimpleName()
                + " -- " + signature.getName() + '(' + args + ") :: ";
        System.err.println(msgTemplate + "START");
        var rsl = pjp.proceed();
        System.err.println(msgTemplate + "END");
        return rsl;
    }

    private String argsToString(Object[] args) {
        StringBuilder rsl = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
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
