package airbnb.com.backend1.Service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import airbnb.com.backend1.Entity.Image;
import airbnb.com.backend1.Entity.Response.ImagesResponse;
import airbnb.com.backend1.Entity.Response.SingleImageRes;

public interface ImageService {
    byte[] getImageByFileName(String fileName);
    ImagesResponse uploadImages(List<MultipartFile> files); 
    SingleImageRes uploadImage(MultipartFile file);
}
