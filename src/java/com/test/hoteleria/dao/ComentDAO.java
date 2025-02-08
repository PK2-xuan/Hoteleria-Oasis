package com.test.hoteleria.dao;

import com.test.hoteleria.dbconexion.config;
import com.test.hoteleria.entity.Comentario;
import com.test.hoteleria.entity.Usuario;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ComentDAO {

    // Registrar un comentario
    public void registrar(Comentario comentario) throws Exception {
        try {
            // Usamos la conexión proporcionada por la clase config
            CallableStatement st = config.getCn().prepareCall("{CALL SP_INSERT_COMENTARIO(?, ?, ?, ?)}");

            // Establecer los parámetros del procedimiento almacenado
            st.setString(1, comentario.getComentario());           // p_comentario
            st.setString(2, comentario.getFecha_comentario());     // p_fecha_comentario
            st.setString(3, comentario.getClasificacion());        // p_clasificacion
            st.setInt(4, comentario.getUsuario().getId_usuario()); // p_id_usuario

            // Ejecutar el procedimiento
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    // Listar todos los comentarios
    public List<Comentario> listar() throws Exception {
        List<Comentario> lista = new ArrayList<>();
        ResultSet rs = null;
        CallableStatement st = null;

        try {
            // Usamos la conexión proporcionada por la clase config
            st = config.getCn().prepareCall("{CALL SP_LISTA_COMENTARIOS()}");

            // Ejecutar el procedimiento almacenado y obtener el ResultSet
            rs = st.executeQuery();

            // Procesar el resultado
            while (rs.next()) {
                Comentario comment = new Comentario();

                comment.setId_comentario(rs.getInt("id_comentario"));
                comment.setComentario(rs.getString("comentario"));
                comment.setFecha_comentario(rs.getString("fecha_comentario"));
                comment.setClasificacion(rs.getString("clasificacion"));

                // Establecer la relación con Usuario (FK)
                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                comment.setUsuario(usuario);

                // Agregar el comentario a la lista
                lista.add(comment);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            // Cerrar recursos
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }

        return lista;
    }

}
