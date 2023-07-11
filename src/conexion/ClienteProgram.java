package conexion;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ClienteProgram {
    private Connection connection;
    private Statement statement;
    private DefaultTableModel tableModel;
    private JTextField codigoTextField;
    private JTextField dniTextField;
    private JTextField nombreTextField;
    private JTextField estRegTextField;
    private JTable tablaItems;

    public ClienteProgram() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/casaapuestas", "root", "admin");
        statement = connection.createStatement();
    }

    private Border createTitledBorder(String title) {
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        return BorderFactory.createTitledBorder(border, title);
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Cliente administrador");
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
        panel.setBorder(createTitledBorder("Registro de Clientes"));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel codigoLabel = new JLabel("CliCod:");
        panel.add(codigoLabel, constraints);

        constraints.gridx = 1;
        codigoTextField = new JTextField(10);
        panel.add(codigoTextField, constraints);

        constraints.gridy = 1;
        constraints.gridx = 0;
        JLabel dniLabel = new JLabel("CliDni:");
        panel.add(dniLabel, constraints);

        constraints.gridx = 1;
        dniTextField = new JTextField(8);
        panel.add(dniTextField, constraints);

        constraints.gridy = 2;
        constraints.gridx = 0;
        JLabel nombreLabel = new JLabel("CliNom:");
        panel.add(nombreLabel, constraints);

        constraints.gridx = 1;
        nombreTextField = new JTextField(40);
        panel.add(nombreTextField, constraints);

        constraints.gridy = 3;
        constraints.gridx = 0;
        JLabel estRegLabel = new JLabel("CliEstReg:");
        panel.add(estRegLabel, constraints);

        constraints.gridy = 3;
        constraints.gridx = 1;
        estRegTextField = new JTextField(2);
        estRegTextField.setText("A");
        estRegTextField.setEditable(false);
        panel.add(estRegTextField, constraints);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new FlowLayout());

        panel.setBorder(createTitledBorder("Tabla de Clientes"));

        tablaItems = new JTable();
        JScrollPane scrollPane = new JScrollPane(tablaItems);
        panel.add(scrollPane, BorderLayout.CENTER);

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

        JButton inactiveButton = new JButton("Inactivar");
        inactiveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inactivarRegistro();
            }
        });

        panel2.add(inactiveButton);

        JButton reactivateButton = new JButton("Reactivar");
        reactivateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reactivarRegistro();
            }
        });
        panel2.add(reactivateButton);

        JButton actualizarButton = new JButton("Actualizar");
        actualizarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarRegistro();
            }
        });
        panel2.add(actualizarButton);

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

    private void loadData() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM cliente");
            ResultSetMetaData metaData = resultSet.getMetaData();

            // Obtener la cantidad de columnas
            int columnCount = metaData.getColumnCount();

            // Crear el modelo de la tabla
            tableModel = new DefaultTableModel();

            // Agregar los nombres de las columnas al modelo
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnLabel(i));
            }

            // Agregar los datos al modelo
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            // Asignar el modelo a la tabla
            tablaItems.setModel(tableModel);
        } catch (SQLException e) {
            mostrarError("Error al adicionar el registro: " + e.getMessage());
        }
    }

    private void adicionarRegistro() {
        String codigo = codigoTextField.getText();
        String dni = dniTextField.getText();
        String nombre = nombreTextField.getText();
        String estReg = estRegTextField.getText();

        if (codigo.isEmpty() || codigo.isBlank()) {
            mostrarError("El código está en blanco o no es válido");
        }

        try {
            String query = "INSERT INTO cliente (CliCod, CliDni, CliNom, CliEstReg) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, codigo);
            preparedStatement.setString(2, dni);
            preparedStatement.setString(3, nombre);
            preparedStatement.setString(4, estReg);

            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Actualizar la tabla
            loadData();

            // Limpiar campos de texto
            codigoTextField.setText("");
            dniTextField.setText("");
            nombreTextField.setText("");
            estRegTextField.setText("A");
        } catch (SQLException e) {
            mostrarError("Error al adicionar el registro: " + e.getMessage());
        }
    }

    private void modificarRegistro() {
        int selectedRow = tablaItems.getSelectedRow();

        if (selectedRow >= 0 && codigoTextField.isEditable()) {
            String codigo = (String) "" + tableModel.getValueAt(selectedRow, 0);
            String dni = (String) "" + tableModel.getValueAt(selectedRow, 1);
            String nombre = (String) "" + tableModel.getValueAt(selectedRow, 2);
            String estReg = (String) "" + tableModel.getValueAt(selectedRow, 3);

            codigoTextField.setText(codigo);
            codigoTextField.setEditable(false);
            dniTextField.setText(dni);
            nombreTextField.setText(nombre);
            estRegTextField.setText(estReg);
        } else {
            try {
                String codigo = codigoTextField.getText();
                String dni = dniTextField.getText();
                String nombre = nombreTextField.getText();
                String estReg = estRegTextField.getText();

                String query = "UPDATE cliente SET CliDni = ?, CliNom = ?, CliEstReg = ? WHERE CliCod = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, dni);
                preparedStatement.setString(2, nombre);
                preparedStatement.setString(3, estReg);
                preparedStatement.setString(4, codigo);

                preparedStatement.executeUpdate();
                preparedStatement.close();

                // Actualizar la tabla
                loadData();

                // Limpiar campos de texto
                codigoTextField.setText("");
                codigoTextField.setEditable(true);
                dniTextField.setText("");
                nombreTextField.setText("");
                estRegTextField.setText("A");
            } catch (SQLException e) {
                mostrarError("Error al modificar el registro: " + e.getMessage());
            }
        }
    }

    private void eliminarRegistro() {
        int selectedRow = tablaItems.getSelectedRow();
        if (selectedRow >= 0) {
            String codigo = (String) "" + tableModel.getValueAt(selectedRow, 0);
            String dni = (String) "" + tableModel.getValueAt(selectedRow, 1);
            String nombre = (String) "" + tableModel.getValueAt(selectedRow, 2);
            String estReg = (String) "" + tableModel.getValueAt(selectedRow, 3);

            codigoTextField.setText(codigo);
            codigoTextField.setEditable(false);
            dniTextField.setText(dni);
            dniTextField.setEditable(false);
            nombreTextField.setText(nombre);
            nombreTextField.setEditable(false);
            estRegTextField.setText("*");
        }
    }

    private void inactivarRegistro() {
        int selectedRow = tablaItems.getSelectedRow();
        if (selectedRow >= 0) {
            String codigo = (String) "" + tableModel.getValueAt(selectedRow, 0);
            String dni = (String) "" + tableModel.getValueAt(selectedRow, 1);
            String nombre = (String) "" + tableModel.getValueAt(selectedRow, 2);
            String estReg = (String) "" + tableModel.getValueAt(selectedRow, 3);

            codigoTextField.setText(codigo);
            codigoTextField.setEditable(false);
            dniTextField.setText(dni);
            dniTextField.setEditable(false);
            nombreTextField.setText(nombre);
            nombreTextField.setEditable(false);
            estRegTextField.setText("I");
        }
    }

    private void reactivarRegistro() {
        int selectedRow = tablaItems.getSelectedRow();
        if (selectedRow >= 0) {
            String codigo = (String) "" + tableModel.getValueAt(selectedRow, 0);
            String dni = (String) "" + tableModel.getValueAt(selectedRow, 1);
            String nombre = (String) "" + tableModel.getValueAt(selectedRow, 2);
            String estReg = (String) "" + tableModel.getValueAt(selectedRow, 3);

            codigoTextField.setText(codigo);
            codigoTextField.setEditable(false);
            dniTextField.setText(dni);
            dniTextField.setEditable(false);
            nombreTextField.setText(nombre);
            nombreTextField.setEditable(false);
            estRegTextField.setText("A");
        }
    }

    private void actualizarRegistro() {
        try {
            String codigo = codigoTextField.getText();
            String dni = dniTextField.getText();
            String nombre = nombreTextField.getText();
            String estReg = estRegTextField.getText();

            String query = "UPDATE cliente SET CliDni = ?, CliNom = ?, CliEstReg = ? WHERE CliCod = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, dni);
            preparedStatement.setString(2, nombre);
            preparedStatement.setString(3, estReg);
            preparedStatement.setString(4, codigo);

            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Actualizar la tabla
            loadData();

            // Limpiar campos de texto
            codigoTextField.setText("");
            codigoTextField.setEditable(true);
            dniTextField.setText("");
            dniTextField.setEditable(true);
            nombreTextField.setText("");
            nombreTextField.setEditable(true);
            estRegTextField.setText("A");
        } catch (SQLException e) {
            mostrarError("Error al actualizar el registro: " + e.getMessage());
        }
    }

    private void cancelarRegistro() {
        codigoTextField.setText("");
        codigoTextField.setEditable(true);
        dniTextField.setText("");
        dniTextField.setEditable(true);
        nombreTextField.setText("");
        nombreTextField.setEditable(true);
        estRegTextField.setText("A");
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
                ClienteProgram clienteProgram = new ClienteProgram();
                clienteProgram.createAndShowGUI();
            } catch (SQLException e) {
                mostrarError("Error al adicionar el registro: " + e.getMessage());
            }
        });
    }
}
