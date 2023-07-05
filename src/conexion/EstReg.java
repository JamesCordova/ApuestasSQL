package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.sql.SQLException;

public class EstReg {
	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/casaapuestas";
		String usuario = "root";
		String contraseña = "admin";
		Connection conexion = null;

		try {
			// Cargar el controlador JDBC
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Establecer la conexión
			conexion = DriverManager.getConnection(url, usuario, contraseña);

			System.out.println("Base de Datos conectada.");

			// Cerrar la conexión
			// conexion.close();

		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			System.out.println("Errores de carga del controlador.");
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println("Error de conexión.");
		}

		PreparedStatement stmt4 = null;
		try {
			stmt4 = conexion.prepareStatement("DELETE FROM estado_registro WHERE EstReg = ?");
			stmt4.setString(1, "I");
			int count3 = stmt4.executeUpdate();
			System.out.println("Registros eliminados: " + count3);
			stmt4.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
