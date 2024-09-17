package org.styd.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.styd.store.entity.Product;
import org.styd.store.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ProductRepository productRepository;

    public void uploadProductImage(MultipartFile file, Long productId) {

        String directory = "products/" + productId + "/";
        String fileUrl = s3Service.uploadFile(file, directory);

        // store the file url in the database
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setImageUrl(fileUrl);
        productRepository.save(product);

    }
}