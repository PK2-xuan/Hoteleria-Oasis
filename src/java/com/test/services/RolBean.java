package com.test.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.annotation.PostConstruct;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.hoteleria.entity.RolApi;
import java.io.OutputStream;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class RolBean implements Serializable {

    private List<RolApi> roles;  // Lista de roles
    private RolApi rol = new RolApi();  // Rol seleccionado
    private RolApi nuevoRol = new RolApi(); // Objeto para el nuevo rol

    // Getter y Setter para nuevoRol
    public RolApi getNuevoRol() {
        return nuevoRol;
    }

    public void setNuevoRol(RolApi nuevoRol) {
        this.nuevoRol = nuevoRol;
    }

    public List<RolApi> getRoles() {
        return roles;
    }

    public RolApi getRol() {
        return rol;
    }

    public void setRol(RolApi rol) {
        this.rol = rol;
    }

    @PostConstruct
    public void init() {

        // Recuperamos el rol desde el FlashScope (cuando venimos de la edición)
        rol = (RolApi) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("rol");
        if (rol == null) {
            // Si no se encuentra el rol en Flash, obtenemos todos los roles
            obtenerRoles();
        }
    }

    public void obtenerRoles() {
        try {
            URL url = new URL("http://localhost:8090/api/roles/list");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("Respuesta JSON: " + response.toString());

                Gson gson = new Gson();
                roles = gson.fromJson(response.toString(), new TypeToken<List<RolApi>>() {
                }.getType());
            } else {
                System.out.println("Error en la conexión: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void obtenerRolPorId(int idRol) {
        try {
            URL url = new URL("http://localhost:8090/api/roles/" + idRol);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("Respuesta JSON: " + response.toString());

                Gson gson = new Gson();
                rol = gson.fromJson(response.toString(), RolApi.class);
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("rol", rol);
                FacesContext.getCurrentInstance().getExternalContext().redirect("editar-rol.xhtml");
            } else {
                System.out.println("Error en la conexión: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarRolPorId(int idRol) {
        try {
            URL url = new URL("http://localhost:8090/api/roles/" + idRol);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Rol eliminado exitosamente.");
                obtenerRoles();
            } else {
                System.out.println("Error al eliminar el rol: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void agregarRol() {
        try {
            URL url = new URL("http://localhost:8090/api/roles/insert");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            Gson gson = new Gson();
            String jsonInputString = gson.toJson(nuevoRol);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            if (conn.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                System.out.println("Rol creado exitosamente.");
                obtenerRoles();
                FacesContext.getCurrentInstance().getExternalContext().redirect("api.xhtml");
            } else {
                System.out.println("Error al crear el rol: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizarRol() {
        try {
            URL url = new URL("http://localhost:8090/api/roles/" + rol.getIdRol());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            Gson gson = new Gson();
            String jsonInputString = gson.toJson(rol);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Rol actualizado exitosamente.");
                FacesContext.getCurrentInstance().getExternalContext().redirect("usuario_admi.xhtml");
            } else {
                System.out.println("Error al actualizar el rol: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
