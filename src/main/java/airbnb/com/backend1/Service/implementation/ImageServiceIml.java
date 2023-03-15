package airbnb.com.backend1.Service.implementation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import airbnb.com.backend1.Entity.Image;
import airbnb.com.backend1.Entity.Response.ImagesResponse;
import airbnb.com.backend1.Entity.Response.SingleImageRes;
import airbnb.com.backend1.Exception.BadResultException;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Repository.ImageRepos;
import airbnb.com.backend1.Service.ImageService;
import airbnb.com.backend1.Utils.ImageUtil;

@Service
public class ImageServiceIml implements ImageService {
    @Autowired
    ImageRepos imageRepos;
    @Autowired
    ImageUtil imageUtil;

    @Override
    public byte[] getImageByFileName(String fileName) {
       Optional<Image> entity = imageRepos.findByFileName(fileName);
       if(!entity.isPresent()) {
        throw new EntityNotFoundException("the image not found");
       }
       Image image = entity.get();
       return imageUtil.decompressImage(image.getDatabyte());
    }

    @Override
    public SingleImageRes uploadImage(MultipartFile file) {
        try {
            String fileName =  file.getOriginalFilename() + "_" + UUID.randomUUID();
        System.out.println(file.getOriginalFilename());
        String type = file.getContentType();
        byte[] databyte = imageUtil.compressImage(file.getBytes());
        Image image = new Image(fileName, type, databyte);
        imageRepos.save(image);
        SingleImageRes res = new SingleImageRes(fileName);
        return res;
        } catch (IOException ex) {
            throw new BadResultException(ex.getMessage());
        }
    }

    @Override
    public ImagesResponse uploadImages(List<MultipartFile> files) {
       List<String> fileNames = new ArrayList<>();

       files.stream().forEach(file -> {
            try {
                String fileName =  file.getOriginalFilename() + "_" + UUID.randomUUID();
            System.out.println(file.getOriginalFilename());
            String type = file.getContentType();
            byte[] databyte = imageUtil.compressImage(file.getBytes());
            Image image = new Image(fileName, type, databyte);
            imageRepos.save(image);
            fileNames.add(fileName);
            } catch (IOException ex) {
                throw new BadResultException(ex.getMessage());
            }

       });

       return new ImagesResponse(fileNames);
    }

    
}
