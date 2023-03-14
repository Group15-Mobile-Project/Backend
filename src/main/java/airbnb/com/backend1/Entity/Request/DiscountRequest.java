package airbnb.com.backend1.Entity.Request;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DiscountRequest {
   
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate openDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate closeDate;
  
    private int discountRate;
    private Long homeId;
    public DiscountRequest(LocalDate openDate, LocalDate closeDate, int discountRate) {
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.discountRate = discountRate;
    }

    
}
