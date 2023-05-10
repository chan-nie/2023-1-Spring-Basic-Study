package hello.hellospring.aop;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
@Component  // 스프링 빈으로 등록해서 사용하는 것을 더 선호.
@Aspect
public class TimeTraceAop {
    @Around("execution(* hello.hellospring..*(..))")  // 공통관심사를 작성. 실무에서는 몇가지 안쓰니까 전체를 알 필요는 없음.
    // aop 패키지 하위에는 전체 적용시키도록 세팅.
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());
        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joinPoint.toString()+ " " + timeMs +
                    "ms");
        }
    }
}
