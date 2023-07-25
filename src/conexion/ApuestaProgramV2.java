package conexion;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ApuestaProgramV2 {
    private Connection connection;
    private Statement statement;
    private DefaultTableModel cabeceraTableModel;
    private DefaultTableModel detalleTableModel;
    private JTextField codigoTextField;
    private JTextField clienteTextField;
    private JTextField afiliadoTextField;
    private JTextField fechaTextField;
    private JTextField horaTextField;
    private JTextField tipoTextField;
    private JTextField valorApostadoTextField;
    private JTable cabeceraTabla;
    private JTable detalleTabla;

    public ApuestaProgramV2() throws SQLException {
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

        panel.add(createCabeceraFormPanel(), BorderLayout.NORTH);
        panel.add(createDetalleFormPanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);

        loadCabeceraData();
        loadDetalleData();
    }

    private JPanel createCabeceraFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(createTitledBorder("Registro de Apuesta Cabecera"));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel codigoLabel = new JLabel("Código:");
        panel.add(codigoLabel, constraints);

        constraints.gridx = 1;
        codigoTextField = new JTextField(10);
        panel.add(codigoTextField, constraints);

        constraints.gridy = 1;
        constraints.gridx = 0;
        JLabel clienteLabel = new JLabel("Cliente:");
        panel.add(clienteLabel, constraints);

        constraints.gridx = 1;
        clienteTextField = new JTextField(10);
        panel.add(clienteTextField, constraints);

        constraints.gridy = 2;
        constraints.gridx = 0;
        JLabel afiliadoLabel = new JLabel("Afiliado:");
        panel.add(afiliadoLabel, constraints);

        constraints.gridx = 1;
        afiliadoTextField = new JTextField(10);
        panel.add(afiliadoTextField, constraints);

        constraints.gridy = 3;
        constraints.gridx = 0;
        JLabel fechaLabel = new JLabel("Fecha (YYYY-MM-DD):");
        panel.add(fechaLabel, constraints);

        constraints.gridx = 1;
        fechaTextField = new JTextField(10);
        panel.add(fechaTextField, constraints);

        constraints.gridy = 4;
        constraints.gridx = 0;
        JLabel horaLabel = new JLabel("Hora (HH:MM):");
        panel.add(horaLabel, constraints);

        constraints.gridx = 1;
        horaTextField = new JTextField(10);
        panel.add(horaTextField, constraints);

        constraints.gridy = 5;
        constraints.gridx = 0;
        JLabel tipoLabel = new JLabel("Tipo:");
        panel.add(tipoLabel, constraints);

        constraints.gridx = 1;
        tipoTextField = new JTextField(10);
        panel.add(tipoTextField, constraints);

        constraints.gridy = 6;
        constraints.gridx = 0;
        JLabel valorApostadoLabel = new JLabel("Valor Apostado:");
        panel.add(valorApostadoLabel, constraints);

        constraints.gridx = 1;
        valorApostadoTextField = new JTextField(10);
        panel.add(valorApostadoTextField, constraints);

        return panel;
    }

    private JPanel createDetalleFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(createTitledBorder("Registro de Apuesta Detalle"));

        detalleTableModel = new DefaultTableModel();
        detalleTableModel.addColumn("Código");
        detalleTableModel.addColumn("Cuota");
        detalleTabla = new JTable(detalleTableModel);

        JScrollPane scrollPane = new JScrollPane(detalleTabla);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton relacionarButton = new JButton("Relacionar Detalle");
        relacionarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                relacionarDetalle();
            }
        });
        panel.add(relacionarButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout());
        JButton addButton = new JButton("Adicionar");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adicionarRegistro();
            }
        });
        panel1.add(addButton);

        JButton updateButton = new JButton("Modificar");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modificarRegistro();
            }
        });
        panel1.add(updateButton);

        JButton deleteButton = new JButton("Eliminar");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarRegistro();
            }
        });
        panel1.add(deleteButton);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelarRegistro();
            }
        });
        panel1.add(cancelButton);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());

        JButton quitButton = new JButton("Salir");
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salirPrograma();
            }
        });
        panel2.add(quitButton);

        JPanel finalPanel = new JPanel(new BorderLayout());
        finalPanel.add(panel1, BorderLayout.NORTH);
        finalPanel.add(panel2, BorderLayout.SOUTH);
        return finalPanel;
    }

    private void loadCabeceraData() {
        try {
        	ResultSet resultSet = statement.executeQuery("SELECT * FROM APUESTA_CAB");
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
            cabeceraTabla.setModel(cabeceraTableModel);
        } catch (SQLException e) {
            mostrarError("Error al cargar los datos de la tabla APUESTA_CAB: " + e.getMessage());
        }
    }

    private void loadDetalleData() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM APUESTA_DET");
            ResultSetMetaData metaData = resultSet.getMetaData();

            int columnCount = metaData.getColumnCount();

            detalleTableModel.setRowCount(0);

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                detalleTableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar los datos de la tabla APUESTA_DET: " + e.getMessage());
        }
    }

    private void adicionarRegistro() {
        String codigo = codigoTextField.getText();
        String cliente = clienteTextField.getText();
        String afiliado = afiliadoTextField.getText();
        String fecha = fechaTextField.getText();
        String hora = horaTextField.getText();
        String tipo = tipoTextField.getText();
        String valorApostado = valorApostadoTextField.getText();

        try {
            String query = "INSERT INTO APUESTA_CAB (ApuCod, ApuCliCod, ApuAfiCod, ApuFecAño, ApuFecMes, ApuFecDia, ApuFecHor, ApuFecMin, ApuTip, ApuValApo, ApuEstReg) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, codigo);
            preparedStatement.setString(2, cliente);
            preparedStatement.setString(3, afiliado);
            preparedStatement.setString(4, fecha.substring(0, 4));
            preparedStatement.setString(5, fecha.substring(5, 7));
            preparedStatement.setString(6, fecha.substring(8, 10));
            preparedStatement.setString(7, hora.substring(0, 2));
            preparedStatement.setString(8, hora.substring(3, 5));
            preparedStatement.setString(9, tipo);
            preparedStatement.setString(10, valorApostado);
            preparedStatement.setString(11, "A");

            preparedStatement.executeUpdate();
            preparedStatement.close();

            loadCabeceraData();

            codigoTextField.setText("");
            clienteTextField.setText("");
            afiliadoTextField.setText("");
            fechaTextField.setText("");
            horaTextField.setText("");
            tipoTextField.setText("");
            valorApostadoTextField.setText("");
        } catch (SQLException e) {
            mostrarError("Error al adicionar el registro: " + e.getMessage());
        }
    }

    private void modificarRegistro() {
        int selectedRow = cabeceraTabla.getSelectedRow();

        if (selectedRow >= 0) {
            String codigo = (String) cabeceraTableModel.getValueAt(selectedRow, 0);
            String cliente = (String) cabeceraTableModel.getValueAt(selectedRow, 1);
            String afiliado = (String) cabeceraTableModel.getValueAt(selectedRow, 2);
            String fecha = (String) cabeceraTableModel.getValueAt(selectedRow, 3) + "-" + cabeceraTableModel.getValueAt(selectedRow, 4) + "-" + cabeceraTableModel.getValueAt(selectedRow, 5);
            String hora = cabeceraTableModel.getValueAt(selectedRow, 6) + ":" + cabeceraTableModel.getValueAt(selectedRow, 7);
            String tipo = (String) cabeceraTableModel.getValueAt(selectedRow, 8);
            String valorApostado = String.valueOf(cabeceraTableModel.getValueAt(selectedRow, 9));

            codigoTextField.setText(codigo);
            clienteTextField.setText(cliente);
            afiliadoTextField.setText(afiliado);
            fechaTextField.setText(fecha);
            horaTextField.setText(hora);
            tipoTextField.setText(tipo);
            valorApostadoTextField.setText(valorApostado);
        }
    }

    private void eliminarRegistro() {
        int selectedRow = cabeceraTabla.getSelectedRow();

        if (selectedRow >= 0) {
            String codigo = (String) cabeceraTableModel.getValueAt(selectedRow, 0);
            String cliente = (String) cabeceraTableModel.getValueAt(selectedRow, 1);
            String afiliado = (String) cabeceraTableModel.getValueAt(selectedRow, 2);
            String fecha = (String) cabeceraTableModel.getValueAt(selectedRow, 3) + "-" + cabeceraTableModel.getValueAt(selectedRow, 4) + "-" + cabeceraTableModel.getValueAt(selectedRow, 5);
            String hora = cabeceraTableModel.getValueAt(selectedRow, 6) + ":" + cabeceraTableModel.getValueAt(selectedRow, 7);
            String tipo = (String) cabeceraTableModel.getValueAt(selectedRow, 8);
            String valorApostado = String.valueOf(cabeceraTableModel.getValueAt(selectedRow, 9));

            codigoTextField.setText(codigo);
            clienteTextField.setText(cliente);
            afiliadoTextField.setText(afiliado);
            fechaTextField.setText(fecha);
            horaTextField.setText(hora);
            tipoTextField.setText(tipo);
            valorApostadoTextField.setText(valorApostado);
        }
    }

    private void relacionarDetalle() {
        int selectedRow = cabeceraTabla.getSelectedRow();
        if (selectedRow >= 0) {
            String codigoCabecera = (String) cabeceraTableModel.getValueAt(selectedRow, 0);
            DefaultTableModel model = (DefaultTableModel) detalleTabla.getModel();
            int[] selectedRows = detalleTabla.getSelectedRows();

            if (selectedRows.length > 0) {
                for (int i : selectedRows) {
                    String codigoDetalle = (String) model.getValueAt(i, 0);
                    relacionarCabeceraDetalle(codigoCabecera, codigoDetalle);
                }
            } else {
                mostrarError("Por favor, selecciona al menos una fila en la tabla de detalle.");
            }
        } else {
            mostrarError("Por favor, selecciona una fila en la tabla de cabecera.");
        }
    }

    private void relacionarCabeceraDetalle(String codigoCabecera, String codigoDetalle) {
        try {
            String query = "UPDATE APUESTA_DET SET ApuCabCod = ? WHERE ApuDetCod = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, codigoCabecera);
            preparedStatement.setString(2, codigoDetalle);

            preparedStatement.executeUpdate();
            preparedStatement.close();

            loadDetalleData();
        } catch (SQLException e) {
            mostrarError("Error al relacionar el detalle: " + e.getMessage());
        }
    }

    private void cancelarRegistro() {
        codigoTextField.setText("");
        clienteTextField.setText("");
        afiliadoTextField.setText("");
        fechaTextField.setText("");
        horaTextField.setText("");
        tipoTextField.setText("");
        valorApostadoTextField.setText("");
    }

    private void salirPrograma() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            mostrarError("Error al adicionar el registro: " + e.getMessage());
        }

        System.exit(0);
    }

    private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ApuestaProgramV2 apuestaProgram = new ApuestaProgramV2();
                apuestaProgram.createAndShowGUI();
            } catch (SQLException e) {
                mostrarError("Error al adicionar el registro: " + e.getMessage());
            }
        });
    }
}
