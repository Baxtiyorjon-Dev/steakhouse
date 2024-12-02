package com.steakhouse.service;


import com.steakhouse.model.Image;
import com.steakhouse.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public Map<String, String> uploadImage(MultipartFile imageFile) throws IOException {
        Image imageToSave = new Image();
        String path = ImageUtils.writeFile(imageFile.getBytes(), imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf(".")));
        imageToSave.setName(path);
        imageToSave.setType(imageFile.getContentType());
//        imageToSave.setImageData(ImageUtils.compressImage(imageFile.getBytes()));
        imageRepository.save(imageToSave);
        Map<String, String> result = new HashMap<>();
        result.put("path", path);
        return result;
    }

    public byte[] downloadImage(String imageName) throws IOException {
        Optional<Image> dbImage = imageRepository.findByName(imageName);
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("./src/main/resources/images/" + dbImage.get().getName()))) {
            byte[] fileBytes = bis.readAllBytes();
            return fileBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
