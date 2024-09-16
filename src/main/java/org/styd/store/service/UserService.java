package org.styd.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.styd.store.entity.User;
import org.styd.store.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private UserRepository userRepository;

    public void uploadUserProfilePicture(MultipartFile file, Long userId) {

        String directory = "users/" + userId + "/";
        String fileUrl = s3Service.uploadFile(file, directory);

        // store the file url in the database
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPicture(fileUrl);
        userRepository.save(user);
    }
}

