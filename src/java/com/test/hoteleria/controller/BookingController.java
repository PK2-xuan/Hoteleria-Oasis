package com.test.hoteleria.controller;

import com.test.hoteleria.dao.ReservaDAO;
import com.test.hoteleria.entity.Habitacion;
import com.test.hoteleria.entity.Usuario;
import com.test.hoteleria.entity.Reservaciones;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class BookingController {

    private Reservaciones reservas = new Reservaciones();
    private List<Reservaciones> ltsReservaciones;
    private List<Habitacion> ltsRooms;
    private List<Usuario> ltsUsuarios;

    @ManagedProperty(value = "#{usuarioBean}")
    private UsuarioBean usuarioBean;

    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }

    public Reservaciones getReservaciones() {
        return reservas;
    }

    public void setReservaciones(Reservaciones reservas) {
        this.reservas = reservas;
    }

    public List<Reservaciones> getLtsReservaciones() {
        return ltsReservaciones;
    }

    public List<Habitacion> getLtsRooms() {
        return ltsRooms;
    }

    public List<Usuario> getLtsUsuarios() {
        return ltsUsuarios;
    }

    public void registrar() {
        ReservaDAO dao = new ReservaDAO();
        try {
            // Asocia el comentario con el usuario logueado
            reservas.setUsuario(usuarioBean.getUsuario());
            reservas.setEstado("Pendiente");

            // Establece la habitación seleccionada
            /* Habitacion habitacion = new Habitacion();
               habitacion.setId_habitacion(idHabitacion);
               reservas.setHabitacion(habitacion);*/
            dao.registrar(reservas);
            FacesContext.getCurrentInstance().getExternalContext().redirect("validacionCompra.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar reserva", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void listar() throws Exception {
        ReservaDAO dao;
        try {
            dao = new ReservaDAO();
            ltsReservaciones = dao.listar();
        } catch (Exception e) {
            throw e;
        }
    }

    public void leer(int id) throws Exception {
        ReservaDAO dao;
        Reservaciones temp;
        try {
            dao = new ReservaDAO();
            temp = dao.leerID(id);
            if (temp != null) {
                this.reservas = temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
