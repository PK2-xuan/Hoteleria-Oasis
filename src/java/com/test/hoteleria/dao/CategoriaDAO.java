package com.test.hoteleria.dao;

import com.test.hoteleria.dbconexion.config;
import com.test.hoteleria.entity.Categoria;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    // Registrar una nueva categoría usando el procedimiento almacenado
    public void registrar(Categoria categoria) throws Exception {
        CallableStatement st = null;
        try {
            st = config.getCn().prepareCall("{CALL SP_REGISTRAR_CATEGORIA(?, ?, ?, ?)}");
            st.setString(1, categoria.getNombre_categoria());
            st.setString(2, categoria.getEstado());
            st.setString(3, categoria.getImg_path());
            st.setString(4, categoria.getDescripcion());
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    // Leer una categoría por su ID usando el procedimiento almacenado
    public Categoria leerID(int id) throws Exception {
        Categoria categoria = null;
        ResultSet rs = null;
        CallableStatement st = null;
        try {
            st = config.getCn().prepareCall("{CALL SP_LISTAR_CATEGORIA_ID(?)}");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                categoria = new Categoria();
                categoria.setId_categoria(rs.getInt("id_categoria"));
                categoria.setNombre_categoria(rs.getString("nombre_categoria"));
                categoria.setEstado(rs.getString("estado"));
                categoria.setImg_path(rs.getString("img_path"));
                categoria.setDescripcion(rs.getString("descripcion"));
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
        return categoria;
    }

    // Listar todas las categorías usando el procedimiento almacenado
    public List<Categoria> listar() throws Exception {
        List<Categoria> lista = new ArrayList<>();
        ResultSet rs = null;
        CallableStatement st = null;
        try {
            st = config.getCn().prepareCall("{CALL SP_LISTAR_CATEGORIAS()}");
            rs = st.executeQuery();
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId_categoria(rs.getInt("id_categoria"));
                categoria.setNombre_categoria(rs.getString("nombre_categoria"));
                categoria.setEstado(rs.getString("estado"));
                categoria.setImg_path(rs.getString("img_path"));
                categoria.setDescripcion(rs.getString("descripcion"));
                lista.add(categoria);
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

    // Modificar los datos de una categoría usando el procedimiento almacenado
    public void modificar(Categoria categoria) throws Exception {
        CallableStatement st = null;
        try {
            st = config.getCn().prepareCall("{CALL SP_MODIFICAR_CATEGORIA(?, ?, ?, ?, ?)}");
            st.setInt(1, categoria.getId_categoria());
            st.setString(2, categoria.getNombre_categoria());
            st.setString(3, categoria.getEstado());
            st.setString(4, categoria.getImg_path());
            st.setString(5, categoria.getDescripcion());
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    // Eliminar una categoría usando el procedimiento almacenado
    public void eliminar(int id) throws Exception {
        CallableStatement st = null;
        try {
            st = config.getCn().prepareCall("{CALL SP_ELIMINAR_CATEGORIA(?)}");
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
