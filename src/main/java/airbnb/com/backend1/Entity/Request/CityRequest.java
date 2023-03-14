package airbnb.com.backend1.Entity.Request;

import org.hibernate.validator.constraints.Normalized;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CityRequest {
    private String cityName;
    private String countryName;
}
