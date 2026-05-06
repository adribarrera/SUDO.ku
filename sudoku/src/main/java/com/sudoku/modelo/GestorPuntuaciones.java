package com.sudoku.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.InputStream;
import java.sql.*;

public class GestorPuntuaciones {

    private static Properties props = new Properties();

    static {
        try (InputStream in = GestorPuntuaciones.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            } else {
                System.err.println("No se ha encontrado el archivo db.properties");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar db.properties: " + e.getMessage());
        }
    }

    private static Connection getConnection() throws SQLException {
        String url = props.getProperty("db.url");

        // Si el archivo properties tiene la URL de la BBDD
        if (url != null && !url.trim().isEmpty()) {
            // El formato jdbc:mysql://user:pass@host... se puede pasar directamente con un
            // solo argumento
            if (url.contains("@")) {
                return DriverManager.getConnection(url);
            }
        }

        // Fallback por si la URL no está pero sí están las propiedades individuales
        if (url == null || url.trim().isEmpty()) {
            String host = props.getProperty("db.host", "localhost");
            String port = props.getProperty("db.port", "4000");
            String db = props.getProperty("db.database", "sudoku");
            url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?sslMode=VERIFY_IDENTITY";
        }

        String user = props.getProperty("db.user", "");
        String password = props.getProperty("db.password", "");
        return DriverManager.getConnection(url, user, password);
    }

    public static void guardarPuntuacion(RegistroPuntuacion registro) {
        String sql = "INSERT INTO puntuaciones (nombre, dificultad, tiempo_segundos) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String nombre = registro.getNombre();
            if (nombre != null && nombre.length() > 50) {
                nombre = nombre.substring(0, 50);
            }

            pstmt.setString(1, nombre);
            pstmt.setString(2, registro.getDificultad());
            pstmt.setInt(3, registro.getTiempoSegundos());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error guardando puntuación en base de datos: " + e.getMessage());
        }
    }

    public static List<RegistroPuntuacion> obtenerTopPuntuaciones(String dificultad) {
        List<RegistroPuntuacion> ranking = new ArrayList<>();
        // Usamos id_puntuacion tal y como lo tienes en tu script
        String sql = "SELECT id_puntuacion, nombre, dificultad, tiempo_segundos, fecha FROM puntuaciones WHERE dificultad = ? ORDER BY tiempo_segundos ASC LIMIT 10";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dificultad);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id_puntuacion");
                    String nombre = rs.getString("nombre");
                    String diff = rs.getString("dificultad");
                    int tiempo = rs.getInt("tiempo_segundos");
                    Timestamp fecha = rs.getTimestamp("fecha");

                    ranking.add(new RegistroPuntuacion(id, nombre, diff, tiempo, fecha));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error obteniendo ranking de la base de datos: " + e.getMessage());
        }

        return ranking;
    }
}
