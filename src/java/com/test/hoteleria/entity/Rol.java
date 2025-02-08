package com.test.hoteleria.entity;

public class Rol {

    private int id_rol;
    private String nombre_rol;

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

    public String getNombre_rol() {
        return nombre_rol;
    }

    public void setNombre_rol(String nombre_rol) {
        this.nombre_rol = nombre_rol;
    }

    @Override
    public String toString() {
        return "Rol{" + "id_rol=" + id_rol + ", nombre_rol=" + nombre_rol + '}';
    }

}
