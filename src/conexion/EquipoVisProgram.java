package conexion;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class EquipoVisProgram {
    private Connection connection;
    private Statement statement;
    private DefaultTableModel tableModel;
    private JTextField codigoTextField;
    private JComboBox<String> equipoComboBox;
    private JTextField estRegTextField;
    private JTable tablaItems;

    public EquipoVisProgram() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/casaapuestas", "root", "admin");
        statement = connection.createStatement();
    }
    
    private Border createTitledBorder(String title) {
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        return BorderFactory.createTitledBorder(border, title);
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Equipo Visitante");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(createFormPanel(), BorderLayout.NORTH);
        panel.add(createTablePanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);

        loadData();
        loadEquipos();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(createTitledBorder("Registro de Equipo Visitante"));
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
        JLabel equipoLabel = new JLabel("Equipo:");
        panel.add(equipoLabel, constraints);

        constraints.gridx = 1;
        equipoComboBox = new JComboBox<>();
        panel.add(equipoComboBox, constraints);
        
        constraints.gridy = 2;
        constraints.gridx = 0;
        JLabel estRegLabel = new JLabel("Estado Registro:");
        panel.add(estRegLabel, constraints);
        
        constraints.gridy = 2;
        constraints.gridx = 1;
        estRegTextField = new JTextField(2);
        estRegTextField.setText("A");
        estRegTextField.setEditable(false);
        panel.add(estRegTextField, constraints);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        panel.setBorder(createTitledBorder("Tabla de Equipos Visitantes"));

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
                try {
					modificarRegistro();
				} catch (SQLException e1) {
					mostrarError("Error al acceder a los datos" + e1.getMessage());
				}
            }
        });
        panel1.add(updateButton);
        
        JButton deleteButton = new JButton("Eliminar");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					eliminarRegistro();
				} catch (SQLException e1) {
					mostrarError("Error al acceder a los datos" + e1.getMessage());
				}
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
                try {
					inactivarRegistro();
				} catch (SQLException e1) {
					mostrarError("Error al acceder a los datos" + e1.getMessage());
				}
            }
        });
        
        panel2.add(inactiveButton);
        

        JButton reactivateButton = new JButton("Reactivar");
        reactivateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					reactivarRegistro();
				} catch (SQLException e1) {
					mostrarError("Error al acceder a los datos" + e1.getMessage());
				}
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
            ResultSet resultSet = statement.executeQuery("SELECT * FROM equipo_visitante");
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
            mostrarError("Error al adicionar el registro: " + e.getMessage());;
        }
    }
    
    private void loadEquipos() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM equipo");
            while (resultSet.next()) {
                String equipoNom = resultSet.getString("EquNom");
                equipoComboBox.addItem(equipoNom);
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar los equipos: " + e.getMessage());
        }
    }
    
    private String getEquipoCod(String equipoNom) throws SQLException {
        String equipoCod = null;
        String query = "SELECT EquCod FROM equipo WHERE EquNom = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, equipoNom);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            equipoCod = resultSet.getString("EquCod");
        }
        preparedStatement.close();
        return equipoCod;
    }
    
    private String getEquipoNom(String equipoCod) throws SQLException {
        String equipoNom = null;
        String query = "SELECT EquNom FROM equipo WHERE EquCod = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, equipoCod);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            equipoNom = resultSet.getString("EquNom");
        }
        preparedStatement.close();
        return equipoNom;
    }


    private void adicionarRegistro() {
        String codigo = codigoTextField.getText();
        String equipoNom = (String) equipoComboBox.getSelectedItem();
        String estReg = estRegTextField.getText();
        
        if(codigo.isEmpty() || codigo.isBlank()) {
            mostrarError("El código está en blanco o no es válido");
            return;
        }

        try {
        	
        	String equipoCod = getEquipoCod(equipoNom);
            String query = "INSERT INTO equipo_visitante (EquVisCod, EquCod, EquVisEstReg) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, codigo);
            preparedStatement.setString(2, equipoCod);
            preparedStatement.setString(3, estReg);

            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Actualizar la tabla
            loadData();

            // Limpiar campos de texto
            codigoTextField.setText("");
            equipoComboBox.setSelectedIndex(0);
            estRegTextField.setText("A");
        } catch (SQLException e) {
            mostrarError("Error al adicionar el registro: " + e.getMessage());;
        }
    }

    private void modificarRegistro() throws SQLException {
        int selectedRow = tablaItems.getSelectedRow();

        if (selectedRow >= 0) {
            String codigo = (String) "" +  tableModel.getValueAt(selectedRow, 0);
            String equipoCod = (String) "" + tableModel.getValueAt(selectedRow, 1);
            String estReg = (String) "" + tableModel.getValueAt(selectedRow, 2);

            codigoTextField.setText(codigo);
            codigoTextField.setEditable(false);
            equipoComboBox.setSelectedItem(getEquipoNom(equipoCod));
            estRegTextField.setText(estReg);
            estRegTextField.setEditable(false);
        }
    }

    private void eliminarRegistro() throws SQLException {
        int selectedRow = tablaItems.getSelectedRow();
        if (selectedRow >= 0) {
            String codigo = (String) "" + tableModel.getValueAt(selectedRow, 0);
            String equipoCod = (String) "" + tableModel.getValueAt(selectedRow, 1);
            

            codigoTextField.setText(codigo);
            codigoTextField.setEditable(false);
            equipoComboBox.setSelectedItem(getEquipoNom(equipoCod));
            estRegTextField.setText("*");
        }
    }

    private void inactivarRegistro() throws SQLException {
        int selectedRow = tablaItems.getSelectedRow();
        if (selectedRow >= 0) {
            String codigo = (String) "" + tableModel.getValueAt(selectedRow, 0);
            String equipoCod = (String) "" + tableModel.getValueAt(selectedRow, 1);

            codigoTextField.setText(codigo);
            codigoTextField.setEditable(false);
            equipoComboBox.setSelectedItem(getEquipoNom(equipoCod));
            estRegTextField.setText("I");
        }
    }

    private void reactivarRegistro() throws SQLException {
        int selectedRow = tablaItems.getSelectedRow();
        if (selectedRow >= 0) {
            String codigo = (String) "" + tableModel.getValueAt(selectedRow, 0);
            String equipoCod = (String) "" + tableModel.getValueAt(selectedRow, 1);

            codigoTextField.setText(codigo);
            codigoTextField.setEditable(false);
            equipoComboBox.setSelectedItem(getEquipoNom(equipoCod));
            estRegTextField.setText("A");
        }
    }
    
    private void actualizarRegistro() {
        String codigo = "" + codigoTextField.getText();
        String equipoNom = (String) "" + equipoComboBox.getSelectedItem();
        String estReg = (String) "" + estRegTextField.getText();
        
        if(codigo.isEmpty() || codigo.isBlank()) {
            mostrarError("El código está en blanco o no es válido");
            return;
        }

        try {
        	String equipoCod = getEquipoCod(equipoNom);
            String query = "UPDATE equipo_visitante SET EquCod = ?, EquVisEstReg = ? WHERE EquVisCod = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, equipoCod);
            preparedStatement.setString(2, estReg);
            preparedStatement.setString(3, codigo);

            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Actualizar la tabla
            loadData();

            // Limpiar campos de texto
            codigoTextField.setText("");
            codigoTextField.setEditable(true);
            equipoComboBox.setSelectedIndex(0);
        } catch (SQLException e) {
            mostrarError("Error al actualizar el registro: " + e.getMessage());;
        }
    }

    private void cancelarRegistro() {
        codigoTextField.setText("");
        codigoTextField.setEditable(true);
        equipoComboBox.setSelectedIndex(0);
        estRegTextField.setText("A");
    }

    private void salirPrograma() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            mostrarError("Error al adicionar el registro: " + e.getMessage());;
        }

        System.exit(0);
    }
    
    private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                EquipoVisProgram equipoVisProgram = new EquipoVisProgram();
                equipoVisProgram.createAndShowGUI();
            } catch (SQLException e) {
                mostrarError("Error al adicionar el registro: " + e.getMessage());;
            }
        });
    }
}
