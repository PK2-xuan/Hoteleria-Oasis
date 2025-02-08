/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.hoteleria.controller;

import com.test.hoteleria.entity.Usuario;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author xuan
 */
@ManagedBean(name = "usuarioBean")
@SessionScoped
public class UsuarioBean implements Serializable {

    private static final long serialVersionUID = 1L; // Agrega esto para serialización
    private Usuario usuario;

    public Usuario getUsuario() {
        if (usuario == null) {
            usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        }
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usuario);
    }
}
