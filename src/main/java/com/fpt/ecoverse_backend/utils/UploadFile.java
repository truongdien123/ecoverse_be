package com.fpt.ecoverse_backend.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UploadFile {

    private final Cloudinary cloudinary;

    public UploadFile(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String imageToUrl(MultipartFile multipartFile) {
        try {
            Map<?,?> map = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
            return map.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> imagesToUrl(List<MultipartFile> multipartFiles) {
        List<String> images = new ArrayList<>();
        try {
            for (MultipartFile file : multipartFiles) {
                Map<?,?> map = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                String link = map.get("secure_url").toString();
                images.add(link);
            }
            return images;
        } catch (IOException e) {
            throw new RuntimeException("Upload image failed", e);
        }
    }

    public String uploadExcel(Workbook workbook, String fileName) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            workbook.write(bos);
            byte[] bytes = bos.toByteArray();

            Map uploadResult = cloudinary.uploader().upload(
                    bytes,
                    ObjectUtils.asMap(
                            "public_id", "reports/" + fileName,
                            "resource_type", "raw",
                            "overwrite", true
                    )
            );

            return uploadResult.get("secure_url").toString();

        } catch (Exception e) {
            throw new RuntimeException("Cannot upload excel to Cloudinary", e);
        }
    }
}
