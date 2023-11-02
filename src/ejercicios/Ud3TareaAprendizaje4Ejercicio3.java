package ejercicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Ud3TareaAprendizaje4Ejercicio3 {

    public static void main(String[] args) {
        try (Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/dbnotas", "birt", "birt")) {

            System.out.println("Escribe el DNI del alumno al quieres calificar");
            String dni = Consola.readString();

            displayStudentName(conexion, dni);
            displaySubjectList(conexion);

            System.out.println("Escribe el código de la asignatura a evaluar:");
            int cod = Consola.readInt();

            System.out.println("Escribe la nota del alumno:");
            int nota = Consola.readInt();

            handleGrading(conexion, dni, cod, nota);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayStudentName(Connection conexion, String dni) throws SQLException {
        String sql = "SELECT apenom FROM alumnos WHERE dni= ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, dni);
            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    System.out.println(rs.getString("apenom"));
                }
            }
        }
    }

    private static void displaySubjectList(Connection conexion) throws SQLException {
        String sql = "SELECT cod, nombre, abreviatura from asignaturas";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet rs = sentencia.executeQuery()) {
            while (rs.next()) {
                System.out.println(rs.getInt(1) + "-. " + rs.getString(2) + " (" + rs.getString(3) + ")");
            }
        }
    }

    private static void handleGrading(Connection conexion, String dni, int cod, int nota) throws SQLException {
        String sql = "SELECT nota from notas WHERE dni=? and cod=?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, dni);
            sentencia.setInt(2, cod);
            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    updateGrade(conexion, dni, cod, nota);
                } else {
                    addGrade(conexion, dni, cod, nota);
                }
            }
        }
    }

    private static void updateGrade(Connection conexion, String dni, int cod, int nota) throws SQLException {
        String sql = "UPDATE notas SET nota =  ? WHERE dni= ? and cod = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, nota);
            sentencia.setString(2, dni);
            sentencia.setInt(3, cod);
            sentencia.executeUpdate();
            System.out.println("La nota se ha modificado.");
        }
    }

    private static void addGrade(Connection conexion, String dni, int cod, int nota) throws SQLException {
        String sql = "INSERT INTO notas (dni, cod, nota) VALUES (?,?,?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, dni);
            sentencia.setInt(2, cod);
            sentencia.setInt(3, nota);
            sentencia.executeUpdate();
            System.out.println("La nota se ha añadido.");
        }
    }
}