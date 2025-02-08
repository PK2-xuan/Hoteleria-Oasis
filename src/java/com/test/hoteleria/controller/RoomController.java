package com.test.hoteleria.controller;

import com.test.hoteleria.dao.CategoriaDAO;
import com.test.hoteleria.dao.RoomDAO;
import com.test.hoteleria.entity.Habitacion;
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
public class RoomController {

    private Habitacion habitacion = new Habitacion();
    private List<Habitacion> ltsRooms;
    private List<Categoria> ltsCategorias;

    private Part file; // Para la imagen
    private String uploadDir = "D:\\workspace-netbeans\\ProyectHoteleriaOasis\\web\\image"; // Directorio para guardar imágenes

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public List<Habitacion> getLtsRooms() {
        return ltsRooms;
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
        RoomDAO dao = new RoomDAO();
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

                    habitacion.setImg_path(fileName); // Guarda la ruta de la imagen
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            dao.registrar(habitacion);
            FacesContext.getCurrentInstance().getExternalContext().redirect("habitaciones_admi.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar habitación", e.getMessage());
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

    public void listar() throws Exception {
        RoomDAO dao;
        try {
            dao = new RoomDAO();
            ltsRooms = dao.listar();

            // Cargar las categorías de las habitaciones
            for (Habitacion habitacion : ltsRooms) {
                // Asegurarse de que la categoría de cada habitación está cargada
                if (habitacion.getCategoria() != null) {
                    Categoria categoria = obtenerCategoriaPorId(habitacion.getCategoria().getId_categoria());
                    habitacion.setCategoria(categoria);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void leer(int id) throws Exception {
        RoomDAO dao;
        Habitacion temp;
        try {
            dao = new RoomDAO();
            temp = dao.leerID(id);
            if (temp != null) {
                this.habitacion = temp;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizar() {
        RoomDAO dao = new RoomDAO();
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
                    habitacion.setImg_path(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Actualizar habitación en la base de datos
            dao.actualizar(habitacion);
            this.listar();
            FacesContext.getCurrentInstance().getExternalContext().redirect("habitaciones_admi.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al actualizar habitación", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void eliminar(Habitacion habitacion) {

        RoomDAO dao = new RoomDAO();
        try {
            dao.eliminar(habitacion.getId_habitacion());
            listar();
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar hbitacion", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    // Método auxiliar para obtener la categoría por ID
    private Categoria obtenerCategoriaPorId(int idCategoria) throws Exception {
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        return categoriaDAO.leerID(idCategoria);  // Asumimos que leerID te devuelve la categoría con su nombre
    }

    @PostConstruct
    public void init() {
        try {
            listar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
