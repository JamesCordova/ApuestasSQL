package conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.sql.SQLException;


public class Conexion {

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

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Errores de carga del controlador.");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error de conexión.");
		}

		// CREATE crear registros en clientes
		PreparedStatement stmt1 = null;
		try {
			stmt1 = conexion.prepareStatement("INSERT INTO cliente (CliCod, CliDni, CliNom, EstReg) VALUES (?, ?, ?, ?)");
			stmt1.setString(1, "1882238");
			stmt1.setString(2, "61933001");
			stmt1.setString(3, "Jhon");
			stmt1.setString(4, "I");
		} catch (SQLException e) {

			e.printStackTrace();
		}


		int count;
		try {
			count = stmt1.executeUpdate();
			System.out.println("Cantidad Insertada: "+count);
			stmt1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// READ ver tabla de clientes
		PreparedStatement stmt2 = null;
		try {
			stmt2 = conexion.prepareStatement("SELECT * FROM cliente");
		} catch (SQLException e) {

			e.printStackTrace();
		}


		ResultSet list;
		try {
			list = stmt2.executeQuery();
			while(list.next()) {
				System.out.println("Codigo: " + list.getString("CliCod"));
				System.out.println("DNi: " + list.getString("CliDni"));
				System.out.println("Nombre: " + list.getString("CliNom"));
				System.out.println("Estado: " + list.getString("EstReg"));
				System.out.println();
			}
			list.close();
			stmt2.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// UPDATE actualizar registros activos por inactivos
		PreparedStatement stmt3 = null;
		try {
			stmt3 = conexion.prepareStatement("UPDATE cliente SET EstReg = ? WHERE EstReg = ?");
			stmt3.setString(1, "I");
			stmt3.setString(2, "A");
			int count2 = stmt3.executeUpdate();
			System.out.println("Registros Actualizados: " + count2);
			stmt3.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// DELETE eliminar registros con nombre "Roberto"
		PreparedStatement stmt4 = null;
		try {
			stmt4 = conexion.prepareStatement("DELETE FROM cliente WHERE CliNom = ?");
			stmt4.setString(1, "Roberto");
			int count3 = stmt4.executeUpdate();
			System.out.println("Registros eliminados: " + count3);
			stmt4.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		


	}
}