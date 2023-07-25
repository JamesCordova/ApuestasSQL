package conexion;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ApuestaProgramV4 {
    private Connection connection;
    private Statement statement;
    private DefaultTableModel cabeceraTableModel;
    private DefaultTableModel detalleTableModel;
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
    private JTable cabeceraTablaItems;
    private JTable detalleTablaItems;

    public ApuestaProgramV4() throws SQLException {
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

        loadCabeceraData();
        loadDetalleData();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(createTitledBorder("Registro de Apuestas"));
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
        apuFecAnioTextField = new JTextField(10);
        panel.add(apuFecAnioTextField, constraints);

        constraints.gridy = 4;
        constraints.gridx = 0;
        JLabel apuFecMesLabel = new JLabel("ApuFecMes:");
        panel.add(apuFecMesLabel, constraints);

        constraints.gridx = 1;
        apuFecMesTextField = new JTextField(10);
        panel.add(apuFecMesTextField, constraints);

        constraints.gridy = 5;
        constraints.gridx = 0;
        JLabel apuFecDiaLabel = new JLabel("ApuFecDia:");
        panel.add(apuFecDiaLabel, constraints);

        constraints.gridx = 1;
        apuFecDiaTextField = new JTextField(10);
        panel.add(apuFecDiaTextField, constraints);

        constraints.gridy = 6;
        constraints.gridx = 0;
        JLabel apuFecHorLabel = new JLabel("ApuFecHor:");
        panel.add(apuFecHorLabel, constraints);

        constraints.gridx = 1;
        apuFecHorTextField = new JTextField(10);
        panel.add(apuFecHorTextField, constraints);

        constraints.gridy = 7;
        constraints.gridx = 0;
        JLabel apuFecMinLabel = new JLabel("ApuFecMin:");
        panel.add(apuFecMinLabel, constraints);

        constraints.gridx = 1;
        apuFecMinTextField = new JTextField(10);
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

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setBorder(createTitledBorder("Tabla de Apuestas"));

        cabeceraTablaItems = new JTable();
        JScrollPane cabeceraScrollPane = new JScrollPane(cabeceraTablaItems);
        panel.add(cabeceraScrollPane);

        detalleTablaItems = new JTable();
        JScrollPane detalleScrollPane = new JScrollPane(detalleTablaItems);
        panel.add(detalleScrollPane);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        
        JButton adicionarButton = new JButton("Adicionar");
        adicionarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adicionarRegistro();
            }
        });
        panel.add(adicionarButton);

        JButton modificarButton = new JButton("Modificar");
        modificarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modificarRegistro();
            }
        });
        panel.add(modificarButton);
        
        JButton eliminarButton = new JButton("Eliminar");
        eliminarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarRegistro();
            }
        });
        panel.add(eliminarButton);
        
        JButton relacionarButton = new JButton("Relacionar detalle");
        relacionarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                relacionarDetalle();
            }
        });
        panel.add(relacionarButton);
        
        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelarRegistro();
            }
        });
        panel.add(cancelarButton);
        
        JButton inactivarButton = new JButton("Inactivar");
        inactivarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inactivarRegistro();
            }
        });
        panel.add(inactivarButton);
        
        JButton reactivarButton = new JButton("Reactivar");
        reactivarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reactivarRegistro();
            }
        });
        panel.add(reactivarButton);
        
        JButton actualizarButton = new JButton("Actualizar");
        actualizarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarRegistro();
            }
        });
        panel.add(actualizarButton);
        
        JButton salirButton = new JButton("Salir");
        salirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salirPrograma();
            }
        });
        panel.add(salirButton);

        return panel;
    }

    private void loadCabeceraData() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT ApuCod, ApuCliCod, ApuFecAño, ApuFecMes, ApuFecDia, ApuFecHor, ApuFecMin, ApuValApo, ApuEstReg FROM APUESTA_CAB");
            ResultSetMetaData metaData = resultSet.getMetaData();

            // Obtener la cantidad de columnas
            int columnCount = metaData.getColumnCount();

            // Crear el modelo de la tabla
            cabeceraTableModel = new DefaultTableModel();

            // Agregar los nombres de las columnas al modelo
            for (int i = 1; i <= columnCount; i++) {
                cabeceraTableModel.addColumn(metaData.getColumnLabel(i));
            }

            // Agregar los datos al modelo
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                cabeceraTableModel.addRow(rowData);
            }

            // Asignar el modelo a la tabla
            cabeceraTablaItems.setModel(cabeceraTableModel);
        } catch (SQLException e) {
            mostrarError("Error al cargar los datos de APUESTA_CAB: " + e.getMessage());
        }
    }
    
    private void loadDetalleData() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT ApuDetCod, ApuCabCod, ApuDetCuo, ApuEstReg FROM APUESTA_DET");
            ResultSetMetaData metaData = resultSet.getMetaData();

            // Obtener la cantidad de columnas
            int columnCount = metaData.getColumnCount();

            // Crear el modelo de la tabla
            detalleTableModel = new DefaultTableModel();

            // Agregar los nombres de las columnas al modelo
            for (int i = 1; i <= columnCount; i++) {
                detalleTableModel.addColumn(metaData.getColumnLabel(i));
            }

            // Agregar los datos al modelo
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                detalleTableModel.addRow(rowData);
            }

            // Asignar el modelo a la tabla
            detalleTablaItems.setModel(detalleTableModel);
        } catch (SQLException e) {
            mostrarError("Error al cargar los datos de APUESTA_DET: " + e.getMessage());
        }
    }

    private void adicionarRegistro() {
        // Obtener los valores de los campos de texto
        String apuCod = apuCodTextField.getText();
        String apuCliCod = apuCliCodTextField.getText();
        String apuAfiCod = apuAfiCodTextField.getText();
        String apuFecAnio = apuFecAnioTextField.getText();
        String apuFecMes = apuFecMesTextField.getText();
        String apuFecDia = apuFecDiaTextField.getText();
        String apuFecHor = apuFecHorTextField.getText();
        String apuFecMin = apuFecMinTextField.getText();
        String apuTip = apuTipTextField.getText();
        String apuValApo = apuValApoTextField.getText();

        // Verificar que los campos obligatorios no estén vacíos
        if (apuCod.isEmpty() || apuCliCod.isEmpty() || apuAfiCod.isEmpty() || apuFecAnio.isEmpty() ||
                apuFecMes.isEmpty() || apuFecDia.isEmpty() || apuFecHor.isEmpty() || apuFecMin.isEmpty() ||
                apuTip.isEmpty() || apuValApo.isEmpty()) {
            mostrarError("Todos los campos son obligatorios.");
            return;
        }

        try {
            // Crear el registro en la tabla APUESTA_CAB
            String cabeceraQuery = "INSERT INTO APUESTA_CAB (ApuCod, ApuCliCod, ApuAfiCod, ApuFecAño, ApuFecMes, ApuFecDia, ApuFecHor, ApuFecMin, ApuTip, ApuValApo, ApuEstReg) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement cabeceraStatement = connection.prepareStatement(cabeceraQuery);
            cabeceraStatement.setString(1, apuCod);
            cabeceraStatement.setString(2, apuCliCod);
            cabeceraStatement.setString(3, apuAfiCod);
            cabeceraStatement.setString(4, apuFecAnio);
            cabeceraStatement.setString(5, apuFecMes);
            cabeceraStatement.setString(6, apuFecDia);
            cabeceraStatement.setString(7, apuFecHor);
            cabeceraStatement.setString(8, apuFecMin);
            cabeceraStatement.setString(9, apuTip);
            cabeceraStatement.setString(10, apuValApo);
            cabeceraStatement.setString(11, "A"); // EstReg por defecto en "A"

            cabeceraStatement.executeUpdate();
            cabeceraStatement.close();

            // Actualizar la tabla de APUESTA_CAB
            loadCabeceraData();

            // Limpiar los campos de texto
            apuCodTextField.setText("");
            apuCliCodTextField.setText("");
            apuAfiCodTextField.setText("");
            apuFecAnioTextField.setText("");
            apuFecMesTextField.setText("");
            apuFecDiaTextField.setText("");
            apuFecHorTextField.setText("");
            apuFecMinTextField.setText("");
            apuTipTextField.setText("");
            apuValApoTextField.setText("");
        } catch (SQLException e) {
            mostrarError("Error al adicionar el registro en APUESTA_CAB: " + e.getMessage());
        }
    }

    private void modificarRegistro() {
        int selectedRow = cabeceraTablaItems.getSelectedRow();
        if (selectedRow >= 0) {
            // Obtener los valores del registro seleccionado
            String apuCod = (String) cabeceraTableModel.getValueAt(selectedRow, 0);
            String apuCliCod = (String) cabeceraTableModel.getValueAt(selectedRow, 1);
            String apuAfiCod = (String) cabeceraTableModel.getValueAt(selectedRow, 2);
            String apuFecAnio = (String) cabeceraTableModel.getValueAt(selectedRow, 3);
            String apuFecMes = (String) cabeceraTableModel.getValueAt(selectedRow, 4);
            String apuFecDia = (String) cabeceraTableModel.getValueAt(selectedRow, 5);
            String apuFecHor = (String) cabeceraTableModel.getValueAt(selectedRow, 6);
            String apuFecMin = (String) cabeceraTableModel.getValueAt(selectedRow, 7);
            String apuTip = (String) cabeceraTableModel.getValueAt(selectedRow, 8);
            String apuValApo = (String) cabeceraTableModel.getValueAt(selectedRow, 9);
            String apuEstReg = (String) cabeceraTableModel.getValueAt(selectedRow, 10);

            // Actualizar los campos de texto con los valores del registro seleccionado
            apuCodTextField.setText(apuCod);
            apuCliCodTextField.setText(apuCliCod);
            apuAfiCodTextField.setText(apuAfiCod);
            apuFecAnioTextField.setText(apuFecAnio);
            apuFecMesTextField.setText(apuFecMes);
            apuFecDiaTextField.setText(apuFecDia);
            apuFecHorTextField.setText(apuFecHor);
            apuFecMinTextField.setText(apuFecMin);
            apuTipTextField.setText(apuTip);
            apuValApoTextField.setText(apuValApo);
        }
    }

    private void eliminarRegistro() {
        int selectedRow = cabeceraTablaItems.getSelectedRow();
        if (selectedRow >= 0) {
            // Obtener el código de la apuesta seleccionada
            String apuCod = (String) cabeceraTableModel.getValueAt(selectedRow, 0);

            try {
                // Eliminar el registro de APUESTA_CAB
                String cabeceraQuery = "DELETE FROM APUESTA_CAB WHERE ApuCod = ?";
                PreparedStatement cabeceraStatement = connection.prepareStatement(cabeceraQuery);
                cabeceraStatement.setString(1, apuCod);

                cabeceraStatement.executeUpdate();
                cabeceraStatement.close();

                // Actualizar la tabla de APUESTA_CAB
                loadCabeceraData();
            } catch (SQLException e) {
                mostrarError("Error al eliminar el registro de APUESTA_CAB: " + e.getMessage());
            }
        }
    }

    private void relacionarDetalle() {
        int selectedRow = cabeceraTablaItems.getSelectedRow();
        if (selectedRow >= 0) {
            // Obtener el código de la apuesta seleccionada
            String apuCod = (String) cabeceraTableModel.getValueAt(selectedRow, 0);

            // Verificar que el código de apuesta no esté vacío
            if (apuCod.isEmpty()) {
                mostrarError("No se ha seleccionado una apuesta válida.");
                return;
            }

            // Actualizar el ApuCabCod en la tabla APUESTA_DET con el valor de ApuCod
            try {
                String detalleQuery = "UPDATE APUESTA_DET SET ApuCabCod = ? WHERE ApuCabCod IS NULL";
                PreparedStatement detalleStatement = connection.prepareStatement(detalleQuery);
                detalleStatement.setString(1, apuCod);

                detalleStatement.executeUpdate();
                detalleStatement.close();

                // Actualizar la tabla de APUESTA_DET
                loadDetalleData();
            } catch (SQLException e) {
                mostrarError("Error al relacionar el detalle de la apuesta: " + e.getMessage());
            }
        }
    }

    private void cancelarRegistro() {
        apuCodTextField.setText("");
        apuCliCodTextField.setText("");
        apuAfiCodTextField.setText("");
        apuFecAnioTextField.setText("");
        apuFecMesTextField.setText("");
        apuFecDiaTextField.setText("");
        apuFecHorTextField.setText("");
        apuFecMinTextField.setText("");
        apuTipTextField.setText("");
        apuValApoTextField.setText("");
    }

    private void inactivarRegistro() {
        int selectedRow = cabeceraTablaItems.getSelectedRow();
        if (selectedRow >= 0) {
            // Obtener el código de la apuesta seleccionada
            String apuCod = (String) cabeceraTableModel.getValueAt(selectedRow, 0);

            try {
                // Actualizar el estado de registro a "I" en APUESTA_CAB
                String cabeceraQuery = "UPDATE APUESTA_CAB SET ApuEstReg = ? WHERE ApuCod = ?";
                PreparedStatement cabeceraStatement = connection.prepareStatement(cabeceraQuery);
                cabeceraStatement.setString(1, "I");
                cabeceraStatement.setString(2, apuCod);

                cabeceraStatement.executeUpdate();
                cabeceraStatement.close();

                // Actualizar la tabla de APUESTA_CAB
                loadCabeceraData();
            } catch (SQLException e) {
                mostrarError("Error al inactivar el registro de APUESTA_CAB: " + e.getMessage());
            }
        }
    }

    private void reactivarRegistro() {
        int selectedRow = cabeceraTablaItems.getSelectedRow();
        if (selectedRow >= 0) {
            // Obtener el código de la apuesta seleccionada
            String apuCod = (String) cabeceraTableModel.getValueAt(selectedRow, 0);

            try {
                // Actualizar el estado de registro a "A" en APUESTA_CAB
                String cabeceraQuery = "UPDATE APUESTA_CAB SET ApuEstReg = ? WHERE ApuCod = ?";
                PreparedStatement cabeceraStatement = connection.prepareStatement(cabeceraQuery);
                cabeceraStatement.setString(1, "A");
                cabeceraStatement.setString(2, apuCod);

                cabeceraStatement.executeUpdate();
                cabeceraStatement.close();

                // Actualizar la tabla de APUESTA_CAB
                loadCabeceraData();
            } catch (SQLException e) {
                mostrarError("Error al reactivar el registro de APUESTA_CAB: " + e.getMessage());
            }
        }
    }

    private void actualizarRegistro() {
        // Obtener los valores de los campos de texto
        String apuCod = apuCodTextField.getText();
        String apuCliCod = apuCliCodTextField.getText();
        String apuAfiCod = apuAfiCodTextField.getText();
        String apuFecAnio = apuFecAnioTextField.getText();
        String apuFecMes = apuFecMesTextField.getText();
        String apuFecDia = apuFecDiaTextField.getText();
        String apuFecHor = apuFecHorTextField.getText();
        String apuFecMin = apuFecMinTextField.getText();
        String apuTip = apuTipTextField.getText();
        String apuValApo = apuValApoTextField.getText();

        // Verificar que los campos obligatorios no estén vacíos
        if (apuCod.isEmpty() || apuCliCod.isEmpty() || apuAfiCod.isEmpty() || apuFecAnio.isEmpty() ||
                apuFecMes.isEmpty() || apuFecDia.isEmpty() || apuFecHor.isEmpty() || apuFecMin.isEmpty() ||
                apuTip.isEmpty() || apuValApo.isEmpty()) {
            mostrarError("Todos los campos son obligatorios.");
            return;
        }

        try {
            // Actualizar el registro en la tabla APUESTA_CAB
            String cabeceraQuery = "UPDATE APUESTA_CAB SET ApuCliCod = ?, ApuAfiCod = ?, ApuFecAño = ?, ApuFecMes = ?, " +
                    "ApuFecDia = ?, ApuFecHor = ?, ApuFecMin = ?, ApuTip = ?, ApuValApo = ? WHERE ApuCod = ?";
            PreparedStatement cabeceraStatement = connection.prepareStatement(cabeceraQuery);
            cabeceraStatement.setString(1, apuCliCod);
            cabeceraStatement.setString(2, apuAfiCod);
            cabeceraStatement.setString(3, apuFecAnio);
            cabeceraStatement.setString(4, apuFecMes);
            cabeceraStatement.setString(5, apuFecDia);
            cabeceraStatement.setString(6, apuFecHor);
            cabeceraStatement.setString(7, apuFecMin);
            cabeceraStatement.setString(8, apuTip);
            cabeceraStatement.setString(9, apuValApo);
            cabeceraStatement.setString(10, apuCod);

            cabeceraStatement.executeUpdate();
            cabeceraStatement.close();

            // Actualizar la tabla de APUESTA_CAB
            loadCabeceraData();

            // Limpiar los campos de texto
            apuCodTextField.setText("");
            apuCliCodTextField.setText("");
            apuAfiCodTextField.setText("");
            apuFecAnioTextField.setText("");
            apuFecMesTextField.setText("");
            apuFecDiaTextField.setText("");
            apuFecHorTextField.setText("");
            apuFecMinTextField.setText("");
            apuTipTextField.setText("");
            apuValApoTextField.setText("");
        } catch (SQLException e) {
            mostrarError("Error al actualizar el registro en APUESTA_CAB: " + e.getMessage());
        }
    }

    private void salirPrograma() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            mostrarError("Error al cerrar la conexión con la base de datos: " + e.getMessage());
        }
        System.exit(0);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            ApuestaProgramV4 programa = new ApuestaProgramV4();
            programa.createAndShowGUI();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
