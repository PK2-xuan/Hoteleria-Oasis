package com.test.hoteleria.controller;

import com.test.hoteleria.dao.CategoriaDAO;
import com.test.hoteleria.entity.Categoria;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

@ManagedBean
@RequestScoped
public class CatController {

    private Categoria cat = new Categoria();
    private List<Categoria> ltsCategorias;

    private Part file;
    private String uploadDir = "D:\\workspace-netbeans\\ProyectHoteleriaOasis\\web\\image"; //

    public Categoria getCat() {
        return cat;
    }

    public void setCat(Categoria cat) {
        this.cat = cat;
    }

    public List<Categoria> getLtsCategorias() {
        return ltsCategorias;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public void registrar() {
        CategoriaDAO dao = new CategoriaDAO();
        try {
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

                    cat.setImg_path(fileName); // Guarda la ruta de la imagen
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            dao.registrar(cat);
            FacesContext.getCurrentInstance().getExternalContext().redirect("categoria_admi.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar categoría", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
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

    public void leer(int id) throws Exception {
        CategoriaDAO dao;
        Categoria temp;

        try {
            dao = new CategoriaDAO();
            temp = dao.leerID(id);

            if (temp != null) {
                this.cat = temp;
            }

        } catch (Exception e) {
            throw e;
        }
    }

    public void listar() throws Exception {
        CategoriaDAO dao;
        try {
            dao = new CategoriaDAO();
            ltsCategorias = dao.listar();
        } catch (Exception e) {
            throw e;
        }
    }

public void modificar() {
    CategoriaDAO dao = new CategoriaDAO();
    try {
        // Verifica si se ha seleccionado un nuevo archivo
        if (file != null) {
            try (InputStream input = file.getInputStream()) {
                String fileName = getFileName(file);
                File outputFile = new File(uploadDir, fileName);

                // Guardar el nuevo archivo
                try (FileOutputStream output = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                }

                // Actualizar el path de la imagen
                cat.setImg_path(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Actualizar categoría en la base de datos
        dao.modificar(cat);
        this.listar();
        FacesContext.getCurrentInstance().getExternalContext().redirect("categoria_admi.xhtml");
    } catch (Exception e) {
        e.printStackTrace();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar categoría", e.getMessage());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}

    public void eliminar(Categoria categoria) {
        CategoriaDAO dao = new CategoriaDAO();
        try {
            dao.eliminar(categoria.getId_categoria());
            //   FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar categoría", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    /*demouser*/
    @PostConstruct
    public void init() {
        CategoriaDAO dao = new CategoriaDAO();
        try {
            ltsCategorias = dao.listar();
        } catch (Exception e) {
        }
    }

}
