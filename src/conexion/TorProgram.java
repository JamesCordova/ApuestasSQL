package conexion;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TorProgram {
    private Connection connection;
    private Statement statement;
    private DefaultTableModel tableModel;
    private JTextField codigoTextField;
    private JTextField descripcionTextField;
    private JTextField estRegTextField;
    private JTable tablaEstadoRegistro;

    public TorProgram() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/casaapuestas", "root", "gimGonza");
        statement = connection.createStatement();
    }

    private Border createTitledBorder(String title) {
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        return BorderFactory.createTitledBorder(border, title);
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Estado Registro administrador");
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
        panel.setBorder(createTitledBorder("Registro de Torneos"));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel codigoLabel = new JLabel("Código:");
        panel.add(codigoLabel, constraints);

        constraints.gridx = 1;
        codigoTextField = new JTextField(8);
        panel.add(codigoTextField, constraints);

        constraints.gridy = 1;
        constraints.gridx = 0;
        JLabel descripcionLabel = new JLabel("Nombre: ");
        panel.add(descripcionLabel, constraints);

        constraints.gridx = 1;
        descripcionTextField = new JTextField(30);
        panel.add(descripcionTextField, constraints);

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
        JPanel panel = new JPanel(new BorderLayout());

        panel.setBorder(createTitledBorder("Tabla de torneos"));

        tablaEstadoRegistro = new JTable();
        JScrollPane scrollPane = new JScrollPane(tablaEstadoRegistro);
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
                modificarRegistro();
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
            ResultSet resultSet = statement.executeQuery("SELECT * FROM torneo");
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
            tablaEstadoRegistro.setModel(tableModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Comando Adicionar; se seleccionará el comando Adicionar y se blanqueará las cajas de texto
    del área de Registro, procediendo a ingresar los datos de código y de descripción (el dato estado
    de registro se mostrará por defecto la letra A (Activo) y no podrá ser modificado, estando
    protegido de ello.
    Se coloca el valor de “1” en el flag o bandera de actualizar (nombre del flag CarFlaAct); que nos
    indicará que se actualizará un registro en la base de datos.
    Se selecciona el comando Actualizar para grabar en la tabla de la base de datos y se carga el
    registro adicionado (código, descripción y estado del registro) en la grilla.
    Si no se desea Actualizar se selecciona comando Cancelar, se borra los datos del área de Registro
    y se inactiva el adicionar. Se coloca el flag o bandera de actualizar en valor de “0” no realizará
    la función de actualizar. */



    private void adicionarRegistro() {
        String codigo = codigoTextField.getText();
        String descripcion = descripcionTextField.getText();
        String estReg = estRegTextField.getText();

        try {
            String query = "INSERT INTO torneo (TorCod, TorNom, TorEstReg) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, codigo);
            preparedStatement.setString(2, descripcion);
            preparedStatement.setString(3, estReg);

            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Actualizar la tabla
            loadData();

            // Limpiar campos de texto
            codigoTextField.setText("");
            descripcionTextField.setText("");
            estRegTextField.setText("A");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* Comando Modificar; se selecciona con un click el registro de la grilla que se desea modificar,
se seleccionará el comando Modificar y se carga los datos del registro seleccionado que se desea
modificar a las cajas de texto del área de Registro. (sólo se puede modificar la descripción,
protegiendo el dato código y estado de registro). */

    private void modificarRegistro() {
        int selectedRow = tablaEstadoRegistro.getSelectedRow();

        if (selectedRow >= 0 && codigoTextField.isEditable() == true) {
        	
        	int codigo = (int) tableModel.getValueAt(selectedRow, 0);
            String descripcion = (String) tableModel.getValueAt(selectedRow, 1);
            String estReg = (String) tableModel.getValueAt(selectedRow, 2);

            codigoTextField.setText(Integer.toString(codigo));
            codigoTextField.setEditable(false);
            descripcionTextField.setText(descripcion);
            estRegTextField.setText(estReg);
            
            
        }
        else{
            try {
            	int codigo = Integer.parseInt(codigoTextField.getText());
                String descripcion = descripcionTextField.getText();//Nombre del torneo
                String estReg = estRegTextField.getText();
                
                String query = "UPDATE TORNEO SET TorCod = ?, TorNom = ?, TorEstReg = ? WHERE TorCod = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, codigo);
                preparedStatement.setString(2, descripcion);
                preparedStatement.setString(3, estReg);
                preparedStatement.setInt(4, codigo);

                preparedStatement.executeUpdate();
                preparedStatement.close();

                // Actualizar la tabla
                loadData();

                // Limpiar campos de texto
                codigoTextField.setText("");
                codigoTextField.setEditable(true);
                descripcionTextField.setText("");
                descripcionTextField.setEditable(true);
                estRegTextField.setText("A");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /* Comando Eliminar; se selecciona con un click el registro de la grilla que se desea eliminar, se
seleccionará el comando Eliminar, y se carga los datos del registro seleccionado que se desea
eliminar a las cajas de texto del área de Registro. (No se puede modificar ningún dato;
protegiendo el dato código, descripción y estado de registro).*/

    private void eliminarRegistro() {
        int selectedRow = tablaEstadoRegistro.getSelectedRow();
        if (selectedRow >= 0) {
            int codigo = (int) tableModel.getValueAt(selectedRow, 0);
            String descripcion = (String) tableModel.getValueAt(selectedRow, 1);

            String codigoStr = String.valueOf(codigo);

            codigoTextField.setText(codigoStr);
            codigoTextField.setEditable(false);
            descripcionTextField.setText(descripcion);
            descripcionTextField.setEditable(false);
            estRegTextField.setText("*");
        }
    }

    /* Comando Inactivar; se selecciona con un click el registro de la grilla que se desea inactivar; se
seleccionará el comando Inactivar, y se carga los datos del registro seleccionado que se desea
inactivar a las cajas de texto del área de Registro. (No se puede modificar ningún dato;
protegiendo el dato código, descripción y estado de registro). */

    private void inactivarRegistro() {
        int selectedRow = tablaEstadoRegistro.getSelectedRow();
        if (selectedRow >= 0) {
            int codigo = (int) tableModel.getValueAt(selectedRow, 0);
            String descripcion = (String) tableModel.getValueAt(selectedRow, 1);

            String codigoStr = String.valueOf(codigo);

            codigoTextField.setText(codigoStr);
            codigoTextField.setEditable(false);
            descripcionTextField.setText(descripcion);
            descripcionTextField.setEditable(false);
            estRegTextField.setText("I");

        }
    }

    /* Comando Reactivar; se selecciona con un click el registro de la grilla que se desea reactivar; se
seleccionará el comando Reactivar, y se carga los datos del registro seleccionado que se desea
reactivar a las cajas de texto del área de Registro. (No se puede modificar ningún dato;
protegiendo el dato código, descripción y estado de registro). */

    private void reactivarRegistro() {
        int selectedRow = tablaEstadoRegistro.getSelectedRow();
        if (selectedRow >= 0) {
            int codigo = (int) tableModel.getValueAt(selectedRow, 0);
            String descripcion = (String) tableModel.getValueAt(selectedRow, 1);

            String codigoStr = String.valueOf(codigo);

            codigoTextField.setText(codigoStr);
            codigoTextField.setEditable(false);
            descripcionTextField.setText(descripcion);
            descripcionTextField.setEditable(false);
            estRegTextField.setText("A");
        }
    }

    private void cancelarRegistro() {
        codigoTextField.setText("");
        codigoTextField.setEditable(true);
        descripcionTextField.setText("");
        descripcionTextField.setEditable(true);
        estRegTextField.setText("A");
    }

    private void salirPrograma() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                TorProgram cargoProgram = new TorProgram();
                cargoProgram.createAndShowGUI();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}