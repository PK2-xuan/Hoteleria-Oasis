package com.test.hoteleria.dao;

import com.test.hoteleria.dbconexion.config;
import com.test.hoteleria.entity.Rol;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RolDAO {

    // Registrar un nuevo rol
    public void registrar(Rol rol) throws Exception {
        try {
            // Usamos la conexión proporcionada por la clase config
            PreparedStatement st = config.getCn().prepareStatement("INSERT INTO rol (nombre_rol) VALUES (?)");
            st.setString(1, rol.getNombre_rol());
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    // Listar todos los roles
    public List<Rol> listar() throws Exception {
        List<Rol> lista = new ArrayList<>();
        ResultSet rs = null;
        try {
            // Usamos la conexión proporcionada por la clase config
            PreparedStatement st = config.getCn().prepareStatement("SELECT id_rol, nombre_rol FROM rol");
            rs = st.executeQuery();
            while (rs.next()) {
                Rol rol = new Rol();
                rol.setId_rol(rs.getInt("id_rol"));
                rol.setNombre_rol(rs.getString("nombre_rol"));
                lista.add(rol);
            }
        } catch (Exception e) {
            throw e;
        }
        return lista;
    }

    // Obtener un rol por ID
    public Rol leerID(int id) throws Exception {
        Rol rol = null;
        ResultSet rs = null;
        try {
            // Usamos la conexión proporcionada por la clase config
            PreparedStatement st = config.getCn().prepareStatement("SELECT id_rol, nombre_rol FROM rol WHERE id_rol = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                rol = new Rol();
                rol.setId_rol(rs.getInt("id_rol"));
                rol.setNombre_rol(rs.getString("nombre_rol"));
            }
        } catch (Exception e) {
            throw e;
        }
        return rol;
    }

    // Modificar un rol
    public void modificar(Rol rol) throws Exception {
        try {
            // Usamos la conexión proporcionada por la clase config
            PreparedStatement st = config.getCn().prepareStatement("UPDATE rol SET nombre_rol = ? WHERE id_rol = ?");
            st.setString(1, rol.getNombre_rol());
            st.setInt(2, rol.getId_rol());
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    // Eliminar un rol
    public void eliminar(int id) throws Exception {
        try {
            // Usamos la conexión proporcionada por la clase config
            PreparedStatement st = config.getCn().prepareStatement("DELETE FROM rol WHERE id_rol = ?");
            st.setInt(1, id);
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }
}
