package conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.*;

public class ConsultaCombinada extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTable table;

    public ConsultaCombinada() {
        setTitle("Consulta de Apuestas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        try {
            // Establecer la conexión a la base de datos
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CasaApuestas", "root", "admin");

            // Crear la consulta SQL
            String consulta = "SELECT C.*, D.ApuDetParCod, D.ApuDetCuo " +
                    "FROM APUESTA_CAB C INNER JOIN APUESTA_DET D ON C.ApuCod = 1390001230 " +
                    "WHERE D.ApuCabCod = C.ApuCod";

            // Ejecutar la consulta
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(consulta);

            // Crear el modelo de tabla
            DefaultTableModel model = new DefaultTableModel();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Obtener los nombres de las columnas
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                model.addColumn(metaData.getColumnLabel(columnIndex));
            }

            // Obtener los datos de las filas
            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    row[columnIndex - 1] = resultSet.getObject(columnIndex);
                }
                model.addRow(row);
            }

            // Establecer el modelo de tabla en la JTable
            table.setModel(model);

            // Cerrar la conexión a la base de datos
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConsultaCombinada consultaApuestas = new ConsultaCombinada();
            consultaApuestas.setVisible(true);
        });
    }
}
