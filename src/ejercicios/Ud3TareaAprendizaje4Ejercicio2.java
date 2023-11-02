package ejercicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Ud3TareaAprendizaje4Ejercicio2 {

    private static final String CONNECTION_URL = "jdbc:mysql://localhost/dbnotas";
    private static final String USERNAME = "birt";
    private static final String PASSWORD = "birt";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD)) {
            String dni = getDniFromUser();
            String currentName = getStudentName(connection, dni);

            if (currentName != null) {
                System.out.println(currentName);
                String newName = getNewNameFromUser();

                if (!newName.isEmpty()) {
                    updateStudentName(connection, dni, newName);
                    System.out.println("Alumno modificado correctamente");
                } else {
                    System.out.println("No ha introducido un nuevo nombre");
                }
            }
            System.out.println("Fin del programa.");
        } catch (SQLException e) {
            System.err.println("Error al conectarse o trabajar con la base de datos: " + e.getMessage());
        }
    }

    private static String getDniFromUser() {
        System.out.println("Escribe el DNI del alumno que deseas modificar");
        return Consola.readString();
    }

    private static String getStudentName(Connection connection, String dni) throws SQLException {
        String sql = "SELECT apenom FROM alumnos WHERE dni= ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("apenom");
                }
            }
        }
        return null;
    }

    private static String getNewNameFromUser() {
        System.out.println("Escribe el nuevo nombre para el alumno");
        return Consola.readString();
    }

    private static void updateStudentName(Connection connection, String dni, String newName) throws SQLException {
        String sql = "UPDATE alumnos SET apenom = ? WHERE dni= ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setString(2, dni);
            stmt.executeUpdate();
        }
    }
}
