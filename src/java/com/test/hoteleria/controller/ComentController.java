 package com.test.hoteleria.controller;

import com.test.hoteleria.dao.ComentDAO;
import com.test.hoteleria.dao.UsuarioDAO;
import com.test.hoteleria.entity.Comentario;
import com.test.hoteleria.entity.Usuario;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class ComentController {

    private Comentario coment = new Comentario();
    private List<Usuario> ltsUsuarios;
    private List<Comentario> ltsComment;

    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }

    public Comentario getComent() {
        return coment;
    }

    public void setComent(Comentario coment) {
        this.coment = coment;
    }

    public List<Usuario> getLtsUsuarios() {
        return ltsUsuarios;
    }

    public List<Comentario> getLtsComment() {
        return ltsComment;
    }

    public void registrar() {
        ComentDAO dao = new ComentDAO();
        try {
            // Asocia el comentario con el usuario logueado
            coment.setUsuario(usuarioBean.getUsuario());
            // Establece la fecha del comentario a la fecha actual
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            coment.setFecha_comentario(sdf.format(new Date()));

            dao.registrar(coment);

            FacesContext.getCurrentInstance().getExternalContext().redirect("rooms.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar comentario", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void listar() throws Exception {
        ComentDAO dao;
        try {
            dao = new ComentDAO();
            ltsComment = dao.listar();

            // Recorremos la lista de comentarios y obtenemos el nombre completo del usuario
            for (Comentario comentario : ltsComment) {
                Usuario usuario = obtenerUsuarioPorId(comentario.getUsuario().getId_usuario()); // Aquí haces la consulta
                comentario.setUsuario(usuario);  // Asumes que la entidad Comentario tiene un método setUsuario
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private Usuario obtenerUsuarioPorId(int idUsuario) throws Exception {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        return usuarioDAO.leerID(idUsuario);  // Asumes que leerID te devuelve el usuario con su nombre y apellido
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
