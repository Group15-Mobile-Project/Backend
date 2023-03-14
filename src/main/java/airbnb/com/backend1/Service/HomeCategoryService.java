package airbnb.com.backend1.Service;

import java.util.List;

import airbnb.com.backend1.Entity.Request.HomeCategoryRequest;
import airbnb.com.backend1.Entity.Response.HomeCategoryRes;

public interface HomeCategoryService {
    List<HomeCategoryRes> getAll();
    HomeCategoryRes getById(Long id);
    HomeCategoryRes saveCategory(HomeCategoryRequest request);
    HomeCategoryRes updateCategory(Long id, HomeCategoryRequest request);
    void deleteCategory(Long id);    
}
