package hello.hellospring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect  // Aop에 꼭 써줘야 하는 어노테이션
@Component  // spring bean 으로 등록해줘야함 - 1번째 방법 / 2번째 방법은 직접 등록해주는 방법으로 더 선호됨. (SpringConfig)
public class TimeTraceAop {

    @Around("execution(* hello.hellospring..*(..))")  // 이 메소드를 타켓팅할 곳, 지금은 hello.hellospring아래 전부에 적용
    public Object execute(ProceedingJoinPoint jointPoint) throws Throwable{
        long start = System.currentTimeMillis();
        System.out.println("START: "+jointPoint.toString());
        try{
            //  다음 메소드로 진행됨
            return jointPoint.proceed();
        }finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: "+jointPoint.toString() + " " + timeMs + "ms");
        }

    }
}
