package com.test.hoteleria.controller;

import com.test.hoteleria.dao.UsuarioDAO;
import com.test.hoteleria.entity.Rol;
import com.test.hoteleria.entity.Usuario;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@ManagedBean
@RequestScoped
@ViewScoped
public class UserController {

    private Usuario usuario = new Usuario();
    private List<Usuario> ltsUsuarios;
    private List<Rol> ltsRoles;

    private Part file;
    private String uploadDir = "D:\\workspace-netbeans\\ProyectHoteleriaOasis\\web\\image"; //

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public List<Usuario> getLtsUsuarios() {
        return ltsUsuarios;
    }

    public List<Rol> getLtsRoles() {
        return ltsRoles;
    }

    public void registrar() {
        UsuarioDAO dao = new UsuarioDAO();
        try {
            /* autoregistra con el rol de */
            Rol rolCliente = new Rol();
            rolCliente.setId_rol(2);
            usuario.setRol(rolCliente);
            usuario.setEstado("ACTIVO");
            /* autoregistra con el rol de */
            dao.registrar(usuario);
            // FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar usuario", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void registrarAdministrador() {
        UsuarioDAO dao = new UsuarioDAO();
        try {
            /* autoregistra con el rol de */
            Rol rolCliente = new Rol();
            rolCliente.setId_rol(2);
            usuario.setRol(rolCliente);
            usuario.setEstado("ACTIVO");

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

                    usuario.setPath(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            dao.registrar(usuario);
            FacesContext.getCurrentInstance().getExternalContext().redirect("usuario_admi.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar usuario", e.getMessage());
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

    public void register() {
        UsuarioDAO dao = new UsuarioDAO();
        try {
            /* autoregistra con el rol de */
            Rol rolCliente = new Rol();
            rolCliente.setId_rol(2);
            usuario.setRol(rolCliente);
            usuario.setEstado("ACTIVO");
            /* autoregistra con el rol de */
            dao.registrar(usuario);
            FacesContext.getCurrentInstance().getExternalContext().redirect("logging.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar usuario", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void listar() throws Exception {
        UsuarioDAO dao;
        try {
            dao = new UsuarioDAO();
            ltsUsuarios = dao.listar();
        } catch (Exception e) {
            throw e;
        }
    }

    public void leer(int id) throws Exception {
        UsuarioDAO dao;
        Usuario temp;
        try {
            dao = new UsuarioDAO();
            temp = dao.leerID(id);
            if (temp != null) {
                this.usuario = temp;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizar() {
        UsuarioDAO dao = new UsuarioDAO();
        try {
            //   usuario.setRol(getRolSeleccionado());
            dao.actualizar(usuario);
            this.listar();
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al actualizar usuario", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void actualizarAdministrador() {
        UsuarioDAO dao = new UsuarioDAO();
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
                    usuario.setPath(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Actualizar usuario en la base de datos
            dao.actualizar(usuario);
            this.listar();
            FacesContext.getCurrentInstance().getExternalContext().redirect("usuario_admi.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al actualizar usuario", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void eliminar(Usuario usuario) {
        UsuarioDAO dao = new UsuarioDAO();
        try {
            dao.eliminar(usuario.getId_usuario());
            listar();
            FacesContext.getCurrentInstance().getExternalContext().redirect("usuario_admi.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar usuario", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    /* login demon*/
    public String login() {
        UsuarioDAO dao = new UsuarioDAO();
        try {
            Usuario usuarioLogueado = dao.validarLogin(usuario.getUsername(), usuario.getPassword());
            if (usuarioLogueado != null) {
                // Verifica si es administrador o cliente
                int rolId = usuarioLogueado.getRol().getId_rol();
                boolean esAdministrador = (rolId == 1);

                // Guarda el usuario en el ámbito de sesión
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usuarioLogueado);

                if (esAdministrador) {
                    // return "demo.xhtml?faces-redirect=true";
                    return "admin_oasis.xhtml?faces-redirect=true";
                } else {
                    return "home.xhtml?faces-redirect=true"; // Redirige al dashboard de cliente
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario o contraseña incorrectos", "Error de login");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de login", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }
    }

    public void logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate(); // Invalida la sesión actual
        }
        try {
            facesContext.getExternalContext().redirect("logging.xhtml"); // Redirige al usuario a la página de inicio de sesión
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /* login demon*/
    @PostConstruct
    public void init() {
        try {
            listar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
