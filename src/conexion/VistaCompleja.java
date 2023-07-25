package conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.*;

public class VistaCompleja extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTable table;

    public VistaCompleja() {
        setTitle("Vista de Apuestas");
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
            String consulta = "SELECT\r\n"
            		+ "  AC.ApuCod AS Apuesta_Cabecera,\r\n"
            		+ "  C.CliNom AS Cliente,\r\n"
            		+ "  A.AfiNom AS Afiliado,\r\n"
            		+ "  AC.ApuFecAño AS Año,\r\n"
            		+ "  AC.ApuFecMes AS Mes,\r\n"
            		+ "  AC.ApuFecDia AS Dia,\r\n"
            		+ "  AC.ApuFecHor AS Hora,\r\n"
            		+ "  AC.ApuFecMin AS Minuto,\r\n"
            		+ "  AC.ApuTip AS Tipo,\r\n"
            		+ "  AD.ApuDetCod AS Apuesta_Detalle,\r\n"
            		+ "  T.TorNom AS Torneo,\r\n"
            		+ "  ELoc.EquNom AS Equipo_Local,\r\n"
            		+ "  EVis.EquNom AS Equipo_Visitante,\r\n"
            		+ "  AD.ApuDetCuo AS Cuota,\r\n"
            		+ "  EGan.EquNom AS Equipo_Ganador\r\n"
            		+ "FROM APUESTA_CAB AS AC\r\n"
            		+ "JOIN APUESTA_DET AS AD ON AC.ApuCod = AD.ApuCabCod\r\n"
            		+ "JOIN CLIENTE AS C ON AC.ApuCliCod = C.CliCod\r\n"
            		+ "LEFT JOIN AFILIADO AS A ON AC.ApuAfiCod = A.AfiCod\r\n"
            		+ "JOIN PARTIDO AS P ON AD.ApuDetParCod = P.ParCod\r\n"
            		+ "JOIN EQUIPO_LOCAL AS EL ON P.ParEquLocCod = EL.EquLocCod\r\n"
            		+ "JOIN EQUIPO AS ELoc ON EL.EquCod = ELoc.EquCod\r\n"
            		+ "JOIN EQUIPO_VISITANTE AS EV ON P.ParEquVisCod = EV.EquVisCod\r\n"
            		+ "JOIN EQUIPO AS EVis ON EV.EquCod = EVis.EquCod\r\n"
            		+ "JOIN TORNEO AS T ON P.ParTorCod = T.TorCod\r\n"
            		+ "LEFT JOIN EQUIPO AS EGan ON P.ParGanCod = EGan.EquCod;";

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
            VistaCompleja consultaApuestas = new VistaCompleja();
            consultaApuestas.setVisible(true);
        });
    }
}
