package org.styd.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.styd.store.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ProductRepository productRepository;

    public String uploadProductImage(MultipartFile file, Long productId) {

        String directory = "products/" + productId + "/";
        String fileUrl = s3Service.uploadFile(file, directory);

        return fileUrl;

    }
}