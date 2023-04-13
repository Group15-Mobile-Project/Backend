package airbnb.com.backend1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import airbnb.com.backend1.Entity.Request.HomeCategoryRequest;
import airbnb.com.backend1.Entity.Response.HomeCategoryRes;
import airbnb.com.backend1.Service.HomeCategoryService;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/homeCategories")
public class HomeCategoryController {
    @Autowired
    HomeCategoryService homeCategoryService;

    @GetMapping("/all")
    public ResponseEntity<List<HomeCategoryRes>> getAll() {
        return new ResponseEntity<List<HomeCategoryRes>>(homeCategoryService.getAll(), HttpStatus.OK);
    }
    @GetMapping("/category/{id}")
    public ResponseEntity<HomeCategoryRes> getById(@PathVariable Long id) {
        return new ResponseEntity<HomeCategoryRes>(homeCategoryService.getById(id), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/category/{id}")
    public ResponseEntity<HomeCategoryRes> updateById(@PathVariable Long id, @RequestBody @Valid HomeCategoryRequest request) {
        return new ResponseEntity<HomeCategoryRes>(homeCategoryService.updateCategory(id, request), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<HomeCategoryRes> createCategory( @RequestBody @Valid HomeCategoryRequest request) {
        return new ResponseEntity<HomeCategoryRes>(homeCategoryService.saveCategory(request), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/category/{id}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable Long id) {
        homeCategoryService.deleteCategory( id);
        return new ResponseEntity<HttpStatus>( HttpStatus.OK);
    }
}
