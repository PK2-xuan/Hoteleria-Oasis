/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.hoteleria.controller;

import com.test.hoteleria.dao.ImageDAO;
import com.test.hoteleria.entity.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.Part;

/**
 *
 * @author xuan
 */
@ManagedBean
@ViewScoped
public class ImageController {

    private Part file;
    private String uploadDir = "D:\\workspace-netbeans\\ProyectHoteleriaOasis\\web\\image"; // Cambia esta ruta a la carpeta donde deseas guardar las imágenes
    private List<Image> images;
    private ImageDAO imageDAO;

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public List<Image> getImages() {
        return images;
    }

    @PostConstruct
    public void init() {
        imageDAO = new ImageDAO();
        try {
            images = imageDAO.getAllImages();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void upload() {
        if (file != null) {
            try (InputStream input = file.getInputStream()) {
                String fileName = getFileName(file);
                File outputFile = new File(uploadDir, fileName);

                try (FileOutputStream output = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                }

                // Guardar solo el nombre del archivo en la base de datos
                Image image = new Image();
                image.setPath(fileName);

                imageDAO.saveImagePath(image);

                System.out.println("File uploaded to: " + outputFile.getAbsolutePath());
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String token : contentDisposition.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 2, token.length() - 1);
            }
        }
        return "default-name";
    }

}
