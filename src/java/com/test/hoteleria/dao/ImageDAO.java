package com.test.hoteleria.dao;

import com.test.hoteleria.dbconexion.config;
import com.test.hoteleria.entity.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImageDAO {

    // Guardar la ruta de la imagen en la base de datos
    public void saveImagePath(Image image) throws SQLException {
        Connection connection = null;
        try {
            // Obtener la conexión de la clase config (Singleton)
            connection = config.getCn();

            String sql = "INSERT INTO images (img_path) VALUES (?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, image.getPath());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw e;  // Re-throw the SQLException to be handled by the calling method
        } finally {
            // Asegurarse de que la conexión se cierre correctamente
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

// Obtener todas las imágenes desde la base de datos
    public List<Image> getAllImages() throws SQLException {
        List<Image> images = new ArrayList<>();
        try (Connection connection = config.getCn(); // Usa la conexión directamente
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM images");
                ResultSet rs = stmt.executeQuery()) { // El ResultSet se obtiene aquí

            while (rs.next()) {
                Image image = new Image();
                image.setId(rs.getInt("id"));
                image.setPath(rs.getString("img_path"));
                images.add(image);
            }
        } catch (SQLException e) {
            throw e;  // Re-throw the SQLException to be handled by the calling method
        }

        return images;
    }

}
