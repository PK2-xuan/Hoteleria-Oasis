package com.test.hoteleria.dao;

import com.test.hoteleria.dbconexion.config;
import com.test.hoteleria.entity.Habitacion;
import com.test.hoteleria.entity.Usuario;
import com.test.hoteleria.entity.Reservaciones;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    // Registrar una nueva reserva usando el procedimiento almacenado
    public void registrar(Reservaciones reservas) throws Exception {
        CallableStatement st = null;
        try {
            st = config.getCn().prepareCall("{CALL SP_REGISTRAR_RESERVA(?, ?, ?, ?, ?)}");
            st.setString(1, reservas.getFecha_inicio());
            st.setString(2, reservas.getFecha_fin());
            st.setString(3, reservas.getEstado());
            st.setInt(4, reservas.getUsuario().getId_usuario());
            st.setInt(5, reservas.getHabitacion().getId_habitacion());
            st.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al registrar la reserva", e);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    // Listar todas las reservas usando el procedimiento almacenado
    public List<Reservaciones> listar() throws Exception {
        List<Reservaciones> lista = new ArrayList<>();
        ResultSet rs = null;
        CallableStatement st = null;
        try {
            st = config.getCn().prepareCall("{CALL SP_LISTAR_RESERVAS()}");
            rs = st.executeQuery();
            while (rs.next()) {
                Reservaciones reserva = new Reservaciones();
                reserva.setId_reserva(rs.getInt("id_reserva"));
                reserva.setFecha_inicio(rs.getString("fecha_inicio"));
                reserva.setFecha_fin(rs.getString("fecha_fin"));
                reserva.setEstado(rs.getString("estado"));

                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                reserva.setUsuario(usuario);

                Habitacion habitacion = new Habitacion();
                habitacion.setId_habitacion(rs.getInt("id_habitacion"));
                reserva.setHabitacion(habitacion);

                lista.add(reserva);
            }
        } catch (Exception e) {
            throw new Exception("Error al listar las reservas", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return lista;
    }

    // Obtener una reserva por ID usando el procedimiento almacenado
    public Reservaciones leerID(int id) throws Exception {
        Reservaciones reserva = null;
        ResultSet rs = null;
        CallableStatement st = null;
        try {
            st = config.getCn().prepareCall("{CALL SP_LISTAR_RESERVA_ID(?)}");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                reserva = new Reservaciones();
                reserva.setId_reserva(rs.getInt("id_reserva"));
                reserva.setFecha_inicio(rs.getString("fecha_inicio"));
                reserva.setFecha_fin(rs.getString("fecha_fin"));
                reserva.setEstado(rs.getString("estado"));

                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                reserva.setUsuario(usuario);

                Habitacion habitacion = new Habitacion();
                habitacion.setId_habitacion(rs.getInt("id_habitacion"));
                reserva.setHabitacion(habitacion);
            }
        } catch (Exception e) {
            throw new Exception("Error al leer la reserva por ID", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return reserva;
    }

    // Listar reservas por usuario usando el procedimiento almacenado
    public List<Reservaciones> listarReservasPorUsuario(int idUsuario) throws Exception {
        List<Reservaciones> lista = new ArrayList<>();
        ResultSet rs = null;
        CallableStatement st = null;
        try {
            st = config.getCn().prepareCall("{CALL SP_LISTAR_RESERVAS_POR_USUARIO(?)}");
            st.setInt(1, idUsuario);
            rs = st.executeQuery();
            while (rs.next()) {
                Reservaciones reserva = new Reservaciones();
                reserva.setId_reserva(rs.getInt("id_reserva"));
                reserva.setFecha_inicio(rs.getString("fecha_inicio"));
                reserva.setFecha_fin(rs.getString("fecha_fin"));
                reserva.setEstado(rs.getString("estado"));

                Usuario usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));
                reserva.setUsuario(usuario);

                Habitacion habitacion = new Habitacion();
                habitacion.setId_habitacion(rs.getInt("id_habitacion"));
                reserva.setHabitacion(habitacion);

                lista.add(reserva);
            }
        } catch (Exception e) {
            throw new Exception("Error al listar reservas por usuario", e);
        } finally {
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
