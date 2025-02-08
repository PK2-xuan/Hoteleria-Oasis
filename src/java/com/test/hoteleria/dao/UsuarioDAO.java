package com.test.hoteleria.dao;

import com.test.hoteleria.dbconexion.config;
import com.test.hoteleria.entity.Rol;
import com.test.hoteleria.entity.Usuario;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

// Registrar un nuevo usuario
    public void registrar(Usuario usuario) throws Exception {
        try {
            // Usamos la conexión proporcionada por la clase config
            PreparedStatement st = config.getCn().prepareStatement("{CALL SP_INSERT_USUARIOS(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            st.setInt(1, usuario.getRol().getId_rol());
            st.setString(2, usuario.getPath());
            st.setString(3, usuario.getNombre());
            st.setString(4, usuario.getApellido());
            st.setString(5, usuario.getSexo());
            st.setInt(6, usuario.getDni());
            st.setString(7, usuario.getEmail());
            st.setString(8, usuario.getEstado());
            st.setInt(9, usuario.getTelefono());
            st.setString(10, usuario.getUsername());
            st.setString(11, usuario.getPassword());
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Usuario> listar() throws Exception {
        List<Usuario> lista = new ArrayList<>();
        try (CallableStatement st = config.getCn().prepareCall("{CALL SP_LISTA_USUARIOS()}");
                ResultSet rs = st.executeQuery()) {

            // Procesar el resultado y agregar usuarios a la lista
            while (rs.next()) {
                Usuario usu = new Usuario();
                usu.setId_usuario(rs.getInt("id_usuario"));

                // FK: Crear y asignar el objeto Rol
                Rol rol = new Rol();
                rol.setId_rol(rs.getInt("id_rol"));
                usu.setRol(rol);

                // Llenar otros campos del Usuario
                usu.setPath(rs.getString("img_path"));
                usu.setNombre(rs.getString("nombre"));
                usu.setApellido(rs.getString("apellido"));
                usu.setSexo(rs.getString("sexo"));
                usu.setDni(rs.getInt("dni"));
                usu.setEmail(rs.getString("email"));
                usu.setEstado(rs.getString("estado"));
                usu.setTelefono(rs.getInt("telefono"));
                usu.setUsername(rs.getString("username"));

                lista.add(usu);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al listar los usuarios: " + e.getMessage());
        }

        return lista;
    }

// Leer un usuario por su ID
    public Usuario leerID(int id) throws Exception {
        Usuario usuario = null;
        ResultSet rs = null;
        CallableStatement st = null;

        try {
            // Preparar la llamada al procedimiento almacenado
            st = config.getCn().prepareCall("{CALL SP_LISTA_USUARIO_ID(?)}");

            // Establecer el parámetro del procedimiento
            st.setInt(1, id);

            // Ejecutar el procedimiento y obtener el ResultSet
            rs = st.executeQuery();

            // Procesar el resultado
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));

                // Crear y establecer el objeto Rol
                Rol rol = new Rol();
                rol.setId_rol(rs.getInt("id_rol"));
                usuario.setRol(rol);

                // Establecer el resto de los campos de usuario
                usuario.setPath(rs.getString("img_path"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setSexo(rs.getString("sexo"));
                usuario.setDni(rs.getInt("dni"));
                usuario.setEmail(rs.getString("email"));
                usuario.setEstado(rs.getString("estado"));
                usuario.setTelefono(rs.getInt("telefono"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
            }

        } catch (Exception e) {
            throw e;  // Lanza la excepción para que pueda ser manejada en otro lugar si es necesario
        } finally {
            // Asegurarse de cerrar los recursos
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // Log o manejar error al cerrar ResultSet si es necesario
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (Exception e) {
                    // Log o manejar error al cerrar CallableStatement si es necesario
                }
            }
        }

        return usuario;
    }

    // Actualizar los datos de un usuario
    public void actualizar(Usuario usuario) throws Exception {
        CallableStatement st = null;
        try {
            // Usar CallableStatement para llamar al procedimiento almacenado
            st = config.getCn().prepareCall("{CALL SP_UPDATE_USUARIO(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            // Establecer los parámetros de entrada para el procedimiento almacenado
            st.setInt(1, usuario.getId_usuario());  // id_usuario
            st.setInt(2, usuario.getRol().getId_rol());  // id_rol
            st.setString(3, usuario.getPath());  // img_path
            st.setString(4, usuario.getNombre());  // nombre
            st.setString(5, usuario.getApellido());  // apellido
            st.setString(6, usuario.getSexo());  // sexo
            st.setInt(7, usuario.getDni());  // dni
            st.setString(8, usuario.getEmail());  // email
            st.setString(9, usuario.getEstado());  // estado
            st.setInt(10, usuario.getTelefono());  // telefono
            st.setString(11, usuario.getUsername());  // username
            st.setString(12, usuario.getPassword());  // password

            // Ejecutar el procedimiento almacenado
            st.executeUpdate();
        } catch (Exception e) {
            // Mostrar el error para depurar
            e.printStackTrace();
            throw e;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public void eliminar(int id) throws Exception {
        try {
            CallableStatement st = config.getCn().prepareCall("{CALL SP_DELETE_USUARIOS(?)}");
            st.setInt(1, id);
            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new Exception("No se encontró el usuario con id: " + id);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    // Validar el login de un usuario
    public Usuario validarLogin(String username, String password) throws Exception {
        Usuario usuario = null;
        ResultSet rs = null;
        CallableStatement st = null;

        try {
            // Llamar al procedimiento almacenado
            st = config.getCn().prepareCall("{CALL SP_VALIDAR_LOGIN(?, ?)}");
            st.setString(1, username);
            st.setString(2, password);

            // Ejecutar el procedimiento almacenado
            rs = st.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId_usuario(rs.getInt("id_usuario"));

                // Establecer el rol del usuario
                Rol rol = new Rol();
                rol.setId_rol(rs.getInt("id_rol"));
                usuario.setRol(rol);

                // Establecer el resto de los campos
                usuario.setPath(rs.getString("img_path"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setSexo(rs.getString("sexo"));
                usuario.setDni(rs.getInt("dni"));
                usuario.setEmail(rs.getString("email"));
                usuario.setEstado(rs.getString("estado"));
                usuario.setTelefono(rs.getInt("telefono"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();  // Agregar trazas de error para depuración
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }

        return usuario;
    }
}
