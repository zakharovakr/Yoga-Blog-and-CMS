package com.zakharovakr.blogCMSMastery.daos;

import org.springframework.web.multipart.MultipartFile;

//not using this
public interface ImageDao {

    public String saveImage(MultipartFile file, String fileName, String directory);

    public String updateImage(MultipartFile file, String fileName, String directory);

    public boolean deleteImage(String fileName);
}
