package airbnb.com.backend1.Service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import airbnb.com.backend1.Entity.HomeCategory;
import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Request.HomeCategoryRequest;
import airbnb.com.backend1.Entity.Response.HomeCategoryRes;
import airbnb.com.backend1.Exception.EntityExistingException;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Mapper.HomeCategoryMapper;
import airbnb.com.backend1.Repository.HomeCategoryRepos;
import airbnb.com.backend1.Repository.UserRepos;
import airbnb.com.backend1.Service.HomeCategoryService;


@Service
public class HomeCategoryServiceIml implements HomeCategoryService {
    @Autowired
    HomeCategoryRepos homeCategoryRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    HomeCategoryMapper homeCategoryMapper;

    @Override
    public void deleteCategory(Long id) {
       Optional<HomeCategory> entity = homeCategoryRepos.findById(id);
       if(entity.isPresent()) {
        homeCategoryRepos.delete(entity.get());
       } else {
        throw new EntityNotFoundException("the homecategory not found");
       }
     
    }
    @Override
    public List<HomeCategoryRes> getAll() {
        List<HomeCategory> homeCategories = homeCategoryRepos.findAll(Sort.by(Sort.Direction.ASC, "name"));
        List<HomeCategoryRes> responses = homeCategories.stream().map(homecate -> homeCategoryMapper.mapHomeCategoryToRes(homecate)).collect(Collectors.toList());
        return responses;
    }
    @Override
    public HomeCategoryRes getById(Long id) {
        Optional<HomeCategory> entity = homeCategoryRepos.findById(id);
       if(entity.isPresent()) {
        return homeCategoryMapper.mapHomeCategoryToRes(entity.get());
       }
       throw new EntityNotFoundException("the homecategory not found");
    }
    @Override
    public HomeCategoryRes saveCategory(HomeCategoryRequest request) {
        Optional<HomeCategory> entity = homeCategoryRepos.findByName(request.getName());
       if(entity.isPresent()) {
        throw new EntityExistingException("the homecategory exist");
       }
       HomeCategory homeCategory = new HomeCategory(request.getName(), request.getImageUrl());

       homeCategoryRepos.save(homeCategory);
       return homeCategoryMapper.mapHomeCategoryToRes(homeCategory);
    }
    @Override
    public HomeCategoryRes updateCategory(Long id, HomeCategoryRequest request) {
        Optional<HomeCategory> entity = homeCategoryRepos.findById(id);
       if(!entity.isPresent()) {
        throw new EntityNotFoundException("the homecategory not found");
       }
       HomeCategory homeCategory = entity.get();
       if(!request.getName().equals(homeCategory.getName())) {
        Optional<HomeCategory> entityName = homeCategoryRepos.findByName(request.getName());
        if(entityName.isPresent()) {
         throw new EntityExistingException("the homecategory name exist");
        }
        homeCategory.setName(request.getName());
       }
       if(!request.getImageUrl().equals(homeCategory.getImageUrl())) {
        homeCategory.setImageUrl(request.getImageUrl());
       }
       homeCategoryRepos.save(homeCategory);
       return homeCategoryMapper.mapHomeCategoryToRes(homeCategory);
    }
    private Users isCheck(Optional<Users> entity) {
        if(entity.isPresent()) {
            return entity.get();
        }
        throw new EntityNotFoundException("the user not found");
    }
    private Users getAuthUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> entity = userRepos.findByUsername(username);
        Users user = isCheck(entity);
        return user;
    }
    
}
