package com.test.hoteleria.controller;

import com.test.hoteleria.dao.RolDAO;
import com.test.hoteleria.entity.Rol;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class RolController {

    private Rol rol = new Rol();
    private List<Rol> ltsRoles;

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Rol> getLtsRoles() {
        return ltsRoles;
    }
    
    public void registrar() {
        RolDAO dao = new RolDAO();
        try {
            dao.registrar(rol);
         //   FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar rol", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void listar() throws Exception {
        RolDAO dao;
        try {
            dao = new RolDAO();
            ltsRoles = dao.listar();
            System.out.println("cantidad  "+ltsRoles.size());
        } catch (Exception e) {
            throw e;
        }
    }

    public void leer(int id) throws Exception {
        RolDAO dao;
        Rol temp;

        try {
            dao = new RolDAO();
            temp = dao.leerID(id);

            if (temp != null) {
                this.rol = temp;
            }

        } catch (Exception e) {
            throw e;
        }
    }

    public void modificar() {
        RolDAO dao = new RolDAO();
        try {
            dao.modificar(rol);
            this.listar();
          //  FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar rol", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void eliminar(Rol rol) {
        RolDAO dao = new RolDAO();
        try {
            dao.eliminar(rol.getId_rol());
         //   FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar rol", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    @PostConstruct
    public void init() {
        RolDAO dao = new RolDAO();
        try {
            ltsRoles = dao.listar();
        } catch (Exception e) {
        }
    }
}
