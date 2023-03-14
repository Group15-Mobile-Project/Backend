package airbnb.com.backend1.Validation;

import airbnb.com.backend1.Entity.Home;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HomeCloseAndOpenDateValidator implements ConstraintValidator<IsValidDate, Home>  {

    @Override
    public boolean isValid(Home home, ConstraintValidatorContext context) {
        if(home.getOpenBooking().isEqual(home.getCloseBooking()) || home.getOpenBooking().isAfter(home.getCloseBooking())) {
            return false;
        }
        return true;
    }
    
}
