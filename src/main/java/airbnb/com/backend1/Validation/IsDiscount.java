package airbnb.com.backend1.Validation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Constraint(validatedBy = {DiscountValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IsDiscount {
    String message() default "the close and open date are not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
