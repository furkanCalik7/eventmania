package com.database.eventmania.backend.validation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Authorizer {
    @Pointcut(value = "execution(* *.*(..))")
    public void allMethods() {

    }

//    @Around("@annotation(AuthorizeTo)")
//    public Object sort(ProceedingJoinPoint joinPoint) throws Exception, Throwable{
//        AuthorizeTo annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(AuthorizeTo.class);
//
////        Object proceed = joinPoint.proceed();
////// retrieve the passed parameter in annotation
////        System.out.print(annotation.order());
////// sort according to the input parameter
////        if(proceed instanceof List){
////            if(s.order().equals("ASC")) Collections.sort((List)proceed);
////            else Collections.sort((List)proceed,Collections.reverseOrder());
////            return proceed;
////        }
//        return proceed;
//    }
}
