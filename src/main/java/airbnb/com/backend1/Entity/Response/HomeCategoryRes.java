package airbnb.com.backend1.Entity.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HomeCategoryRes {
    private Long id;
    private String name;
    private String imageUrl;
}
