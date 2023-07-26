package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GuiProgram {
	protected Connection connection;
    protected Statement statement;
	
	public GuiProgram() throws SQLException {
        connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/orcl", "casaapuestas", "admin");
        statement = connection.createStatement();
    }

}
