package liu.com.aspectj;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class TmpAspectJ {

    @Pointcut("execution(@liu.com.aspectj.AspectJAno * *(..))")
    public void tmpAno(){
    }

    @Around("tmpAno()")
    public void start(ProceedingJoinPoint joinPoint) throws Throwable {
        Object jp =  joinPoint.getThis();
        joinPoint.proceed();
        Log.e("TAG","aspetj start");
        Log.e("TAG","aspetj start"+jp);
    }

}
