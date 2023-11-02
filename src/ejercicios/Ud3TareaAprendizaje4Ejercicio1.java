package ejercicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Ud3TareaAprendizaje4Ejercicio1 {

    private static final String CONNECTION_URL = "jdbc:mysql://localhost/dbnotas";
    private static final String USERNAME = "birt";
    private static final String PASSWORD = "birt";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD)) {
            fetchAndPrintStudentData(connection);
        } catch (SQLException e) {
            System.err.println("Error al conectarse a la base de datos: " + e.getMessage());
        }
    }

    private static void fetchAndPrintStudentData(Connection connection) {
        String studentQuery = "SELECT dni, apenom FROM alumnos ORDER BY apenom DESC";

        try (PreparedStatement studentStmt = connection.prepareStatement(studentQuery);
             ResultSet studentRs = studentStmt.executeQuery()) {

            while (studentRs.next()) {
                String dni = studentRs.getString("dni");
                System.out.println("\n" + studentRs.getString("apenom"));
                System.out.println("--------------------------------------------------");
                printStudentGrades(connection, dni);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener datos de los estudiantes: " + e.getMessage());
        }
    }

    private static void printStudentGrades(Connection connection, String dni) {
        String gradeQuery = "SELECT abreviatura, nota FROM notas n, asignaturas a WHERE dni= ? and a.cod=n.cod";

        try (PreparedStatement gradeStmt = connection.prepareStatement(gradeQuery)) {
            gradeStmt.setString(1, dni);
            try (ResultSet gradeRs = gradeStmt.executeQuery()) {
                while (gradeRs.next()) {
                    System.out.println(gradeRs.getString(1) + "\t\t" + gradeRs.getInt(2));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las notas del estudiante con DNI " + dni + ": " + e.getMessage());
        }
    }
}