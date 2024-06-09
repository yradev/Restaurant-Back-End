package source.restaurant_web_project.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import source.restaurant_web_project.errors.NotFoundException;

import java.io.IOException;

@Component
public class AmazonS3Cloud {
    @Value("${aws.bucket-name}")
    private String bucket;
    private final AmazonS3 cloud;
    public AmazonS3Cloud(AmazonS3 cloud) {
        this.cloud = cloud;
    }

    public String upload(String key, MultipartFile file) throws IOException {
        if(file==null){
            throw new NotFoundException("File is missing!");
        }

        if(key==null){
            throw new NotFoundException("File name is missing!");
        }


        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        cloud.putObject(new PutObjectRequest(bucket,key, file.getInputStream(),metadata));

        return cloud.getUrl(bucket,key).toString();
    }

    public void delete(String key){
        if(key==null){
            throw new NotFoundException("We need a key!");
        }
        cloud.deleteObject(bucket,key);
    }
}
