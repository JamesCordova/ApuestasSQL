package conexion;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ApuestaProgramV3 {
    private Connection connection;
    private Statement statement;
    private DefaultTableModel cabTableModel;
    private DefaultTableModel detTableModel;
    private JTextField apuCodTextField;
    private JTextField apuCliCodTextField;
    private JTextField apuAfiCodTextField;
    private JTextField apuFecAnioTextField;
    private JTextField apuFecMesTextField;
    private JTextField apuFecDiaTextField;
    private JTextField apuFecHorTextField;
    private JTextField apuFecMinTextField;
    private JTextField apuTipTextField;
    private JTextField apuValApoTextField;
    private JTextField apuEstRegTextField;
    private JTable cabTablaItems;
    private JTable detTablaItems;

    public ApuestaProgramV3() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/casaapuestas", "root", "admin");
        statement = connection.createStatement();
    }

    private Border createTitledBorder(String title) {
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        return BorderFactory.createTitledBorder(border, title);
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Apuesta administrador");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(createFormPanel(), BorderLayout.NORTH);
        panel.add(createTablePanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);

        loadData();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(createTitledBorder("Registro de Apuestas (Cabecera)"));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel apuCodLabel = new JLabel("ApuCod:");
        panel.add(apuCodLabel, constraints);

        constraints.gridx = 1;
        apuCodTextField = new JTextField(10);
        panel.add(apuCodTextField, constraints);

        constraints.gridy = 1;
        constraints.gridx = 0;
        JLabel apuCliCodLabel = new JLabel("ApuCliCod:");
        panel.add(apuCliCodLabel, constraints);

        constraints.gridx = 1;
        apuCliCodTextField = new JTextField(10);
        panel.add(apuCliCodTextField, constraints);

        constraints.gridy = 2;
        constraints.gridx = 0;
        JLabel apuAfiCodLabel = new JLabel("ApuAfiCod:");
        panel.add(apuAfiCodLabel, constraints);

        constraints.gridx = 1;
        apuAfiCodTextField = new JTextField(10);
        panel.add(apuAfiCodTextField, constraints);

        constraints.gridy = 3;
        constraints.gridx = 0;
        JLabel apuFecAnioLabel = new JLabel("ApuFecAño:");
        panel.add(apuFecAnioLabel, constraints);

        constraints.gridx = 1;
        apuFecAnioTextField = new JTextField(4);
        panel.add(apuFecAnioTextField, constraints);

        constraints.gridy = 4;
        constraints.gridx = 0;
        JLabel apuFecMesLabel = new JLabel("ApuFecMes:");
        panel.add(apuFecMesLabel, constraints);

        constraints.gridx = 1;
        apuFecMesTextField = new JTextField(2);
        panel.add(apuFecMesTextField, constraints);

        constraints.gridy = 5;
        constraints.gridx = 0;
        JLabel apuFecDiaLabel = new JLabel("ApuFecDia:");
        panel.add(apuFecDiaLabel, constraints);

        constraints.gridx = 1;
        apuFecDiaTextField = new JTextField(2);
        panel.add(apuFecDiaTextField, constraints);

        constraints.gridy = 6;
        constraints.gridx = 0;
        JLabel apuFecHorLabel = new JLabel("ApuFecHor:");
        panel.add(apuFecHorLabel, constraints);

        constraints.gridx = 1;
        apuFecHorTextField = new JTextField(2);
        panel.add(apuFecHorTextField, constraints);

        constraints.gridy = 7;
        constraints.gridx = 0;
        JLabel apuFecMinLabel = new JLabel("ApuFecMin:");
        panel.add(apuFecMinLabel, constraints);

        constraints.gridx = 1;
        apuFecMinTextField = new JTextField(2);
        panel.add(apuFecMinTextField, constraints);

        constraints.gridy = 8;
        constraints.gridx = 0;
        JLabel apuTipLabel = new JLabel("ApuTip:");
        panel.add(apuTipLabel, constraints);

        constraints.gridx = 1;
        apuTipTextField = new JTextField(10);
        panel.add(apuTipTextField, constraints);

        constraints.gridy = 9;
        constraints.gridx = 0;
        JLabel apuValApoLabel = new JLabel("ApuValApo:");
        panel.add(apuValApoLabel, constraints);

        constraints.gridx = 1;
        apuValApoTextField = new JTextField(10);
        panel.add(apuValApoTextField, constraints);

        constraints.gridy = 10;
        constraints.gridx = 0;
        JLabel apuEstRegLabel = new JLabel("ApuEstReg:");
        panel.add(apuEstRegLabel, constraints);

        constraints.gridx = 1;
        apuEstRegTextField = new JTextField(2);
        apuEstRegTextField.setText("A");
        apuEstRegTextField.setEditable(false);
        panel.add(apuEstRegTextField, constraints);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));

        panel.add(createCabTablePanel());
        panel.add(createDetTablePanel());

        return panel;
    }

    private JPanel createCabTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(createTitledBorder("Tabla de Apuestas (Cabecera)"));

        cabTablaItems = new JTable();
        JScrollPane scrollPane = new JScrollPane(cabTablaItems);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDetTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(createTitledBorder("Tabla de Apuestas (Detalle)"));

        detTablaItems = new JTable();
        JScrollPane scrollPane = new JScrollPane(detTablaItems);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton relacionarButton = new JButton("Relacionar detalle");
        relacionarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                relacionarDetalle();
            }
        });
        panel.add(relacionarButton);

        return panel;
    }

    private void loadData() {
        try {
            ResultSet cabResultSet = statement.executeQuery("SELECT * FROM APUESTA_CAB");
            ResultSetMetaData cabMetaData = cabResultSet.getMetaData();

            // Obtener la cantidad de columnas de la tabla APUESTA_CAB
            int cabColumnCount = cabMetaData.getColumnCount();

            // Crear el modelo de la tabla de la cabecera
            cabTableModel = new DefaultTableModel();

            // Agregar los nombres de las columnas al modelo de la cabecera
            for (int i = 1; i <= cabColumnCount; i++) {
                cabTableModel.addColumn(cabMetaData.getColumnLabel(i));
            }

            // Agregar los datos al modelo de la cabecera
            while (cabResultSet.next()) {
                Object[] rowData = new Object[cabColumnCount];
                for (int i = 1; i <= cabColumnCount; i++) {
                    rowData[i - 1] = cabResultSet.getObject(i);
                }
                cabTableModel.addRow(rowData);
            }

            // Asignar el modelo de la cabecera a la tabla
            cabTablaItems.setModel(cabTableModel);

            // Cargar los datos de la tabla APUESTA_DET relacionados
            cargarDetalle();
        } catch (SQLException e) {
            mostrarError("Error al cargar los datos: " + e.getMessage());
        }
    }

    private void cargarDetalle() {
        int selectedRow = cabTablaItems.getSelectedRow();

        if (selectedRow >= 0) {
            String apuCabCod = cabTablaItems.getValueAt(selectedRow, 0).toString();

            try {
                ResultSet detResultSet = statement.executeQuery("SELECT * FROM APUESTA_DET");
                ResultSetMetaData detMetaData = detResultSet.getMetaData();

                // Obtener la cantidad de columnas de la tabla APUESTA_DET
                int detColumnCount = detMetaData.getColumnCount();

                // Crear el modelo de la tabla del detalle
                detTableModel = new DefaultTableModel();

                // Agregar los nombres de las columnas al modelo del detalle
                for (int i = 1; i <= detColumnCount; i++) {
                    detTableModel.addColumn(detMetaData.getColumnLabel(i));
                }

                // Agregar los datos al modelo del detalle
                while (detResultSet.next()) {
                    Object[] rowData = new Object[detColumnCount];
                    for (int i = 1; i <= detColumnCount; i++) {
                        rowData[i - 1] = detResultSet.getObject(i);
                    }
                    detTableModel.addRow(rowData);
                }

                // Asignar el modelo del detalle a la tabla
                detTablaItems.setModel(detTableModel);
            } catch (SQLException e) {
                mostrarError("Error al cargar los datos del detalle: " + e.getMessage());
            }
        }
    }

    private void relacionarDetalle() {
        int selectedRow = cabTablaItems.getSelectedRow();

        if (selectedRow >= 0) {
            String apuCabCod = cabTablaItems.getValueAt(selectedRow, 0).toString();

            int detSelectedRow = detTablaItems.getSelectedRow();

            if (detSelectedRow >= 0) {
                String apuDetCod = detTablaItems.getValueAt(detSelectedRow, 0).toString();

                try {
                    String query = "UPDATE APUESTA_DET SET ApuCabCod = ? WHERE ApuDetCod = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, apuCabCod);
                    preparedStatement.setString(2, apuDetCod);

                    preparedStatement.executeUpdate();
                    preparedStatement.close();

                    // Volver a cargar los datos del detalle
                    cargarDetalle();
                } catch (SQLException e) {
                    mostrarError("Error al relacionar el detalle: " + e.getMessage());
                }
            } else {
                mostrarError("Por favor, selecciona un registro de la tabla del detalle.");
            }
        } else {
            mostrarError("Por favor, selecciona un registro de la tabla de la cabecera.");
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ApuestaProgramV3 apuestaProgram = new ApuestaProgramV3();
                apuestaProgram.createAndShowGUI();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
