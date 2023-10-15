package stock.chart.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Slf4j
@Aspect
@Component
public class ValidationAdvice {

    @Around("execution(* stock.chart.*.controller.*.*(..))")
    public Object ValidationAdviceHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("ValidationAdviceHandler");
        Object[] args = joinPoint.getArgs();
        BindingResult bindingResult = null;
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                bindingResult = (BindingResult) arg;
            }
        }
        if (bindingResult != null && bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        return joinPoint.proceed();
    }
}
