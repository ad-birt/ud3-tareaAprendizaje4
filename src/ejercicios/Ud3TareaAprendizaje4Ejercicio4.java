package ejercicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;

public class Ud3TareaAprendizaje4Ejercicio4 {

    private static final String DB_URL = "jdbc:mysql://localhost/dbnotas";
    private static final String DB_USER = "birt";
    private static final String DB_PASS = "birt";

    public static void main(String[] args) {
        try (Connection conexion = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String dni = getDni();
            showStudentName(conexion, dni);
            showAvailableSubjects(conexion);
            int cod = getSubjectCode();
            int nota = getStudentGrade();
            callInsertNoteFunction(conexion, cod, dni, nota);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    private static String getDni() {
        System.out.println("Escribe el DNI del alumno al quieres calificar");
        return Consola.readString();
    }

    private static void showStudentName(Connection conexion, String dni) throws SQLException {
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

    private static void showAvailableSubjects(Connection conexion) throws SQLException {
        String sql = "SELECT cod, nombre, abreviatura from asignaturas";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet rs = sentencia.executeQuery()) {
            while (rs.next()) {
                System.out.println(rs.getInt(1) + "-. " + rs.getString(2) + " (" + rs.getString(3) + ")");
            }
        }
    }

    private static int getSubjectCode() {
        System.out.println("Escribe el código de la asignatura a evaluar:");
        return Consola.readInt();
    }

    private static int getStudentGrade() {
        System.out.println("Escribe la nota del alumno:");
        return Consola.readInt();
    }

    private static void callInsertNoteFunction(Connection conexion, int cod, String dni, int nota) throws SQLException {
        String sqlFunction = "{? = call insertar_nota(?,?,?)}";
        try (CallableStatement llamada = conexion.prepareCall(sqlFunction)) {
            llamada.registerOutParameter(1, java.sql.Types.VARCHAR);
            llamada.setInt(2, cod);
            llamada.setString(3, dni);
            llamada.setInt(4, nota);
            llamada.execute();
            String texto = llamada.getString(1);
            System.out.println(texto);
        }
    }
}