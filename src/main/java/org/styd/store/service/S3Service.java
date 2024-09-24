package org.styd.store.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String directory) {
        // Create in S3 the file name
        String uniqueName = UUID.randomUUID().toString();
        String fileName = directory + uniqueName+".jpg";


        try {
            // Upload the file to S3
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), null));

            // return the file URL
            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file to S3", e);
        }
    }
}

