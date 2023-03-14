package airbnb.com.backend1.Validation;

import java.time.LocalDate;

import airbnb.com.backend1.Entity.Discount;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DiscountValidator implements ConstraintValidator<IsDiscount, Discount> {
    @Override
    public boolean isValid(Discount discount, ConstraintValidatorContext context) {
        if(discount.getCloseDate().isBefore(discount.getOpenDate()) ) {
            return false;
        }
        if(discount.getCloseDate().isEqual(discount.getOpenDate()) ) {
            return false;
        }
       return true;
    }
}
