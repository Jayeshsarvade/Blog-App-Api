package com.blog.app.project.blogappapi.service.impl;

import com.blog.app.project.blogappapi.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        //file name  user multipart for upload file on server
        String name = file.getOriginalFilename();//DYWFYSFDY.png
        //random name generate
        String randomId = UUID.randomUUID().toString();   
        String fileName1 = randomId.concat(name.substring(name.lastIndexOf(".")));//23456789.jpg
        //full path
        String filePath = path + File.separator + fileName1;  //     image/23456789.jpg
        //create folder if not created
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }
        //file copy
        Files.copy(file.getInputStream(), Paths.get(filePath));// when we upload a file on server 
        return name;
    }
}
