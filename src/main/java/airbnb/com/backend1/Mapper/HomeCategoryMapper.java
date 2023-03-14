package airbnb.com.backend1.Mapper;

import org.springframework.stereotype.Component;

import airbnb.com.backend1.Entity.HomeCategory;
import airbnb.com.backend1.Entity.Response.HomeCategoryRes;
@Component
public class HomeCategoryMapper {
    public HomeCategoryRes mapHomeCategoryToRes(HomeCategory homeCategory) {
        return new HomeCategoryRes(homeCategory.getId(), homeCategory.getName(), homeCategory.getImageUrl());
    }
}
