package com.test.hoteleria.dao;

import com.test.hoteleria.dbconexion.config;
import com.test.hoteleria.entity.Categoria;
import com.test.hoteleria.entity.Habitacion;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    // Registrar una nueva habitación usando el procedimiento almacenado
    public void registrar(Habitacion habitacion) throws Exception {
        CallableStatement st = null;
        try {
            st = config.getCn().prepareCall("{CALL SP_REGISTRAR_HABITACION(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            st.setInt(1, habitacion.getNumero_habitacion());
            st.setInt(2, habitacion.getCantidad());
            st.setInt(3, habitacion.getCapacidad());
            st.setString(4, habitacion.getCama());
            st.setString(5, habitacion.getServicios());
            st.setInt(6, habitacion.getPiso());
            st.setInt(7, habitacion.getEspacio());
            st.setDouble(8, habitacion.getPrecio());
            st.setString(9, habitacion.getImg_path());
            st.setInt(10, habitacion.getCategoria().getId_categoria());
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    // Listar todas las habitaciones usando el procedimiento almacenado
    public List<Habitacion> listar() throws Exception {
        List<Habitacion> lista = new ArrayList<>();
        ResultSet rs = null;
        CallableStatement st = null;
        try {
            st = config.getCn().prepareCall("{CALL SP_LISTAR_HABITACIONES()}");
            rs = st.executeQuery();
            while (rs.next()) {
                Habitacion habitacion = new Habitacion();
                habitacion.setId_habitacion(rs.getInt("id_habitacion"));
                habitacion.setNumero_habitacion(rs.getInt("numero_habitacion"));
                habitacion.setCantidad(rs.getInt("cantidad"));
                habitacion.setCapacidad(rs.getInt("capacidad"));
                habitacion.setCama(rs.getString("cama"));
                habitacion.setServicios(rs.getString("servicios"));
                habitacion.setPiso(rs.getInt("piso"));
                habitacion.setEspacio(rs.getInt("espacio"));
                habitacion.setPrecio(rs.getDouble("precio"));
                habitacion.setImg_path(rs.getString("img_path"));

                // Establecer la categoría (FK)
                Categoria categoria = new Categoria();
                categoria.setId_categoria(rs.getInt("id_categoria"));
                habitacion.setCategoria(categoria);

                lista.add(habitacion);
            }
        } catch (Exception e) {
            throw e;
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

    // Obtener una habitación por ID usando el procedimiento almacenado
    public Habitacion leerID(int id) throws Exception {
        Habitacion habitacion = null;
        ResultSet rs = null;
        CallableStatement st = null;
        try {
            st = config.getCn().prepareCall("{CALL SP_LISTAR_HABITACION_ID(?)}");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                habitacion = new Habitacion();
                habitacion.setId_habitacion(rs.getInt("id_habitacion"));
                habitacion.setNumero_habitacion(rs.getInt("numero_habitacion"));
                habitacion.setCantidad(rs.getInt("cantidad"));
                habitacion.setCapacidad(rs.getInt("capacidad"));
                habitacion.setCama(rs.getString("cama"));
                habitacion.setServicios(rs.getString("servicios"));
                habitacion.setPiso(rs.getInt("piso"));
                habitacion.setEspacio(rs.getInt("espacio"));
                habitacion.setPrecio(rs.getDouble("precio"));
                habitacion.setImg_path(rs.getString("img_path"));

                // Establecer la categoría (FK)
                Categoria categoria = new Categoria();
                categoria.setId_categoria(rs.getInt("id_categoria"));
                habitacion.setCategoria(categoria);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return habitacion;
    }

    // Modificar la habitación usando el procedimiento almacenado
    public void actualizar(Habitacion habitacion) throws Exception {
        CallableStatement st = null;
        try {
            st = config.getCn().prepareCall("{CALL SP_MODIFICAR_HABITACION(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            st.setInt(1, habitacion.getId_habitacion());
            st.setInt(2, habitacion.getNumero_habitacion());
            st.setInt(3, habitacion.getCantidad());
            st.setInt(4, habitacion.getCapacidad());
            st.setString(5, habitacion.getCama());
            st.setString(6, habitacion.getServicios());
            st.setInt(7, habitacion.getPiso());
            st.setInt(8, habitacion.getEspacio());
            st.setDouble(9, habitacion.getPrecio());
            st.setString(10, habitacion.getImg_path());
            st.setInt(11, habitacion.getCategoria().getId_categoria());
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    // Eliminar la habitación usando el procedimiento almacenado
    public void eliminar(int id) throws Exception {
        CallableStatement st = null;
        try {
            st = config.getCn().prepareCall("{CALL SP_ELIMINAR_HABITACION(?)}");
            st.setInt(1, id);
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }
}
