package com.community.api.service;

import com.community.api.model.ImgFile;
import com.community.api.repository.ImgFileRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j@Service@Transactional
@RequiredArgsConstructor
public class ImgFileService {

    private final ImgFileRepository imgFileRepository;

    @Value("${key.postImgPath}")
    private String postImgPath;

    @Value("${key.postImgUrl}")
    private String postImgUrl;

    @Value("${key.bannerImgPath}")
    private String bannerImgPath;

    @Value("${key.bannerImgUrl}")
    private String bannerImgUrl;

    public ImgFile save(ImgFile imgFile) {
        return imgFileRepository.save(imgFile);
    }

    public ImgFile findByIdEquals(int id) {
        return imgFileRepository.findByIdEquals(id);
    }

    public String saveFile(MultipartFile file) {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        ImgFile imgFile = new ImgFile();
        String fileNameDateTime = dateTimeNow.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        try {
            String origFileName = file.getOriginalFilename();
            String fileName = fileNameDateTime + origFileName;

            if (!postImgPath.endsWith(File.separator)) {
                postImgPath += File.separator;
            }

            File directory = new File(postImgPath);

            if (!directory.exists()) {
                boolean created = directory.mkdirs();  // Create the directory if it doesn't exist
                if (!created) {
                    throw new RuntimeException("Failed to create directory: " + postImgPath);
                }
            }

            String filePath = postImgPath + fileName;
            file.transferTo(new File(filePath));

            imgFile.setOrigFileName(origFileName);
            imgFile.setFilePath(filePath);
            imgFile.setFileName(fileName);
            imgFile.setUploadDate(dateTimeNow);
            imgFile = save(imgFile);

            return postImgUrl + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("File saving failed", e);
        }
    }

    public String saveBannerFile(MultipartFile file) {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        ImgFile imgFile = new ImgFile();
        String fileNameDateTime = dateTimeNow.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        try {
            String origFileName = file.getOriginalFilename();
            String fileName = fileNameDateTime + origFileName;

            if (!bannerImgPath.endsWith(File.separator)) {
                bannerImgPath += File.separator;
            }

            File directory = new File(bannerImgPath);

            if (!directory.exists()) {
                boolean created = directory.mkdirs();  // Create the directory if it doesn't exist
                if (!created) {
                    throw new RuntimeException("Failed to create directory: " + bannerImgPath);
                }
            }

            String filePath = bannerImgPath + fileName;
            file.transferTo(new File(filePath));

            imgFile.setOrigFileName(origFileName);
            imgFile.setFilePath(filePath);
            imgFile.setFileName(fileName);
            imgFile.setUploadDate(dateTimeNow);
            imgFile = save(imgFile);

            return bannerImgUrl + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("File saving failed", e);
        }
    }

    public boolean deleteFile(String fileUrl, String type) {
        // Extract the file name from the URL
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

        // Determine the base path based on the type
        String basePath;
        if ("post".equalsIgnoreCase(type)) {
            basePath = postImgPath;
        } else if ("banner".equalsIgnoreCase(type)) {
            basePath = bannerImgPath;
        } else {
            throw new IllegalArgumentException("Invalid file type. Use 'post' or 'banner'.");
        }

        // Construct the full file path
        File fileToDelete = new File(basePath + File.separator + fileName);

        // Delete the file if it exists
        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                log.info("File deleted successfully: " + fileToDelete.getAbsolutePath());
                return true;
            } else {
                log.error("Failed to delete file: " + fileToDelete.getAbsolutePath());
                return false;
            }
        } else {
            log.warn("File not found: " + fileToDelete.getAbsolutePath());
            return false;
        }
    }
}