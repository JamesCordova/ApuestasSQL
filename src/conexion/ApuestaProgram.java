package conexion;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;

public class ApuestaProgram {
    private Connection connection;
    private Statement statement;
    private DefaultTableModel cabeceraTableModel;
    private DefaultTableModel detalleTableModel;
    private JTextField apuCodTextField;
    private JTextField apuCliCodTextField;
    private JTextField apuAfiCodTextField;
    private JTextField apuFecAñoMesDiaTextField;
    private JTextField apuFecHorMinTextField;
    private JTextField apuTipTextField;
    private JTextField apuValApoTextField;
    private JTextField apuEstRegTextField;
    private JTable cabeceraTablaItems;
    private JTable detalleTablaItems;

    public ApuestaProgram() throws SQLException {
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
        JLabel apuFecAñoMesDiaLabel = new JLabel("ApuFecAñoMesDia (AAAA-MM-DD):");
        panel.add(apuFecAñoMesDiaLabel, constraints);

        constraints.gridx = 1;
        // Máscara para año, mes y día
        try {
            MaskFormatter fechaMask = new MaskFormatter("####-##-##");
            fechaMask.setPlaceholderCharacter('_');
            apuFecAñoMesDiaTextField = new JFormattedTextField(fechaMask);
        } catch (ParseException e) {
            apuFecAñoMesDiaTextField = new JFormattedTextField();
        }
        apuFecAñoMesDiaTextField.setColumns(10);
        
        panel.add(apuFecAñoMesDiaTextField, constraints);

        constraints.gridy = 4;
        constraints.gridx = 0;
        JLabel apuFecHorMinLabel = new JLabel("ApuFecHorMin (HH:MM):");
        panel.add(apuFecHorMinLabel, constraints);

        constraints.gridx = 1;
        // Máscara para hora y minutos
        try {
            MaskFormatter horaMask = new MaskFormatter("##:##");
            horaMask.setPlaceholderCharacter('_');
            apuFecHorMinTextField = new JFormattedTextField(horaMask);
        } catch (ParseException e) {
            apuFecHorMinTextField = new JFormattedTextField();
        }
        apuFecHorMinTextField.setColumns(10);
        
        panel.add(apuFecHorMinTextField, constraints);

        constraints.gridy = 5;
        constraints.gridx = 0;
        JLabel apuTipLabel = new JLabel("ApuTip:");
        panel.add(apuTipLabel, constraints);

        constraints.gridx = 1;
        apuTipTextField = new JTextField(10);
        panel.add(apuTipTextField, constraints);

        constraints.gridy = 6;
        constraints.gridx = 0;
        JLabel apuValApoLabel = new JLabel("ApuValApo:");
        panel.add(apuValApoLabel, constraints);

        constraints.gridx = 1;
        apuValApoTextField = new JTextField(10);
        panel.add(apuValApoTextField, constraints);

        constraints.gridy = 7;
        constraints.gridx = 0;
        JLabel apuEstRegLabel = new JLabel("ApuEstReg:");
        panel.add(apuEstRegLabel, constraints);

        constraints.gridx = 1;
        apuEstRegTextField = new JTextField(10);
        panel.add(apuEstRegTextField, constraints);
        // Valores por defecto
        apuEstRegTextField.setEditable(false);
        apuEstRegTextField.setText("A");
        
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
            ResultSet resultSet = statement.executeQuery("SELECT * FROM APUESTA_DET");
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
        String apuFecAñoMesDia = apuFecAñoMesDiaTextField.getText();
        String apuFecAño = apuFecAñoMesDia.substring(0,4);
        String apuFecMes = apuFecAñoMesDia.substring(5,7);
        String apuFecDia = apuFecAñoMesDia.substring(8);
        String apuFecHorMin = apuFecHorMinTextField.getText();
        String apuFecHor = apuFecHorMin.substring(0,2);
        String apuFecMin = apuFecHorMin.substring(3);
        String apuTip = apuTipTextField.getText();
        String apuValApo = apuValApoTextField.getText();
        String apuEstReg = apuEstRegTextField.getText();

        // Verificar que los campos obligatorios no estén vacíos
        if (apuCod.isEmpty() || apuCliCod.isEmpty()) {
            mostrarError("ApuCod y ApuCliCod son obligatorios.");
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
            cabeceraStatement.setString(4, apuFecAño);
            cabeceraStatement.setString(5, apuFecMes);
            cabeceraStatement.setString(6, apuFecDia);
            cabeceraStatement.setString(7, apuFecHor);
            cabeceraStatement.setString(8, apuFecMin);
            cabeceraStatement.setString(9, apuTip);
            cabeceraStatement.setString(10, apuValApo);
            cabeceraStatement.setString(11, apuEstReg); // EstReg por defecto en "A"

            cabeceraStatement.executeUpdate();
            cabeceraStatement.close();

            // Actualizar la tabla de APUESTA_CAB
            loadCabeceraData();

            // Limpiar los campos de texto
            cancelarRegistro();
        } catch (SQLException e) {
            mostrarError("Error al adicionar el registro en APUESTA_CAB: " + e.getMessage());
        }
    }

    private void modificarRegistro() {
        int selectedRow = cabeceraTablaItems.getSelectedRow();
        if (selectedRow >= 0) {
            // Obtener los valores del registro seleccionado
            String apuCod = (String) "" + cabeceraTableModel.getValueAt(selectedRow, 0);
            setFormulario(apuCod);
            String apuEstReg = (String) "" + cabeceraTableModel.getValueAt(selectedRow, cabeceraTableModel.getColumnCount() - 1);
            apuEstRegTextField.setText(apuEstReg);
            // Habilitando edición
            apuCliCodTextField.setEditable(true);
            apuAfiCodTextField.setEditable(true);
            apuFecAñoMesDiaTextField.setEditable(true);
            apuFecHorMinTextField.setEditable(true);
            apuTipTextField.setEditable(true);
            apuValApoTextField.setEditable(true);

        }
    }

    private void eliminarRegistro() {
        int selectedRow = cabeceraTablaItems.getSelectedRow();
        if (selectedRow >= 0) {
            // Obtener el código de la apuesta seleccionada
            String apuCod = (String) "" + cabeceraTableModel.getValueAt(selectedRow, 0);
            setFormulario(apuCod);
            apuEstRegTextField.setText("*");
        }
    }

    private void relacionarDetalle() {
        int selectedRowCab = cabeceraTablaItems.getSelectedRow();
        int selectedRowDet = detalleTablaItems.getSelectedRow();
        if (selectedRowCab >= 0 && selectedRowDet >= 0) {
            // Obtener el código de la apuesta seleccionada
            String apuCod = (String) "" + cabeceraTableModel.getValueAt(selectedRowCab, 0);
            String apuDetCod = (String) "" + detalleTableModel.getValueAt(selectedRowDet, 0);

            // Verificar que el código de apuesta no esté vacío
            if (apuCod.isEmpty()) {
                mostrarError("No se ha seleccionado una apuesta válida.");
                return;
            }

            // Actualizar el ApuCabCod en la tabla APUESTA_DET con el valor de ApuCod
            try {
                String detalleQuery = "UPDATE APUESTA_DET SET ApuCabCod = ? WHERE ApuDetCod = ?";
                PreparedStatement detalleStatement = connection.prepareStatement(detalleQuery);
                detalleStatement.setString(1, apuCod);
                detalleStatement.setString(2, apuDetCod);

                detalleStatement.executeUpdate();
                detalleStatement.close();

                // Actualizar la tabla de APUESTA_DET
                loadDetalleData();
            } catch (SQLException e) {
            	e.printStackTrace();
                mostrarError("Error al relacionar el detalle de la apuesta: " + e.getMessage());
            }
        }
    }

    private void cancelarRegistro() {
    	apuCodTextField.setText("");
        apuCliCodTextField.setText("");
        apuAfiCodTextField.setText("");
        apuFecAñoMesDiaTextField.setText("");
        apuFecHorMinTextField.setText("");
        apuTipTextField.setText("");
        apuValApoTextField.setText("");
        apuEstRegTextField.setText("A");
    }

    private void inactivarRegistro() {
        int selectedRow = cabeceraTablaItems.getSelectedRow();
        if (selectedRow >= 0) {
            // Obtener el código de la apuesta seleccionada
            String apuCod = (String) "" + cabeceraTableModel.getValueAt(selectedRow, 0);
            setFormulario(apuCod);
            apuEstRegTextField.setText("I");
        }
    }

    private void reactivarRegistro() {
        int selectedRow = cabeceraTablaItems.getSelectedRow();
        if (selectedRow >= 0) {
            // Obtener el código de la apuesta seleccionada
            String apuCod = (String) "" + cabeceraTableModel.getValueAt(selectedRow, 0);
            setFormulario(apuCod);
            apuEstRegTextField.setText("A");
        }
    }
    
    private void setFormulario(String apuCod) {
   	 try {
            // Obtener los campos relacionados de la base de datos
            String query = "SELECT ApuCliCod, ApuAfiCod, ApuFecAño, ApuFecMes, ApuFecDia, ApuFecHor, ApuFecMin, ApuTip, ApuValApo FROM APUESTA_CAB WHERE ApuCod = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, apuCod);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String apuCliCod = resultSet.getString("ApuCliCod");
                String apuAfiCod = resultSet.getString("ApuAfiCod");
                String apuFecAño = String.format("%04d", Integer.parseInt(resultSet.getString("ApuFecAño")));
                String apuFecMes = String.format("%02d", Integer.parseInt(resultSet.getString("ApuFecMes")));
                String apuFecDia = String.format("%02d", Integer.parseInt(resultSet.getString("ApuFecDia")));
                String apuFecAñoMesDia = apuFecAño + apuFecMes + apuFecDia;
                String apuFecHor = String.format("%02d", Integer.parseInt(resultSet.getString("ApuFecHor")));
                String apuFecMin = String.format("%02d", Integer.parseInt(resultSet.getString("ApuFecMin")));
                String apuFecHorMin = apuFecHor + apuFecMin;
                String apuTip = resultSet.getString("ApuTip");
                String apuValApo = resultSet.getString("ApuValApo");
                // Establecer valores
                apuCodTextField.setText(apuCod);
                apuCodTextField.setEditable(false);
                apuCliCodTextField.setText(apuCliCod);
                apuCliCodTextField.setEditable(false);
                apuAfiCodTextField.setText(apuAfiCod);
                apuAfiCodTextField.setEditable(false);
                apuFecAñoMesDiaTextField.setText(apuFecAñoMesDia);
                apuFecAñoMesDiaTextField.setEditable(false);
                apuFecHorMinTextField.setText(apuFecHorMin);
                apuFecHorMinTextField.setEditable(false);
                apuTipTextField.setText(apuTip);
                apuTipTextField.setEditable(false);
                apuValApoTextField.setText(apuValApo);
                apuValApoTextField.setEditable(false);
           }
   	 }
   	 catch (SQLException e) {
   		 mostrarError("Error al obtener los campos relacionados: " + e.getMessage());
   	 }
   }

    private void actualizarRegistro() {
        // Obtener los valores de los campos de texto
    	String apuCod = apuCodTextField.getText();
        String apuCliCod = apuCliCodTextField.getText();
        String apuAfiCod = apuAfiCodTextField.getText();
        String apuFecAñoMesDia = apuFecAñoMesDiaTextField.getText();
        String apuFecAño = apuFecAñoMesDia.substring(0,4);
        String apuFecMes = apuFecAñoMesDia.substring(5,7);
        String apuFecDia = apuFecAñoMesDia.substring(8);
        String apuFecHorMin = apuFecHorMinTextField.getText();
        String apuFecHor = apuFecHorMin.substring(0,2);;
        String apuFecMin = apuFecHorMin.substring(3);
        String apuTip = apuTipTextField.getText();
        String apuValApo = apuValApoTextField.getText();
        String apuEstReg = apuEstRegTextField.getText();

        // Verificar que los campos obligatorios no estén vacíos
        if (apuCod.isEmpty() || apuCliCod.isEmpty()) {
            mostrarError("Llenar ApuCod y CliCod son obligatorios.");
            return;
        }

        try {
            // Actualizar el registro en la tabla APUESTA_CAB
            String cabeceraQuery = "UPDATE APUESTA_CAB SET ApuCliCod = ?, ApuAfiCod = ?, ApuFecAño = ?, ApuFecMes = ?, " +
                    "ApuFecDia = ?, ApuFecHor = ?, ApuFecMin = ?, ApuTip = ?, ApuValApo = ?, ApuEstReg = ? WHERE ApuCod = ?";
            PreparedStatement cabeceraStatement = connection.prepareStatement(cabeceraQuery);
            cabeceraStatement.setString(1, apuCliCod);
            cabeceraStatement.setString(2, apuAfiCod);
            cabeceraStatement.setString(3, apuFecAño);
            cabeceraStatement.setString(4, apuFecMes);
            cabeceraStatement.setString(5, apuFecDia);
            cabeceraStatement.setString(6, apuFecHor);
            cabeceraStatement.setString(7, apuFecMin);
            cabeceraStatement.setString(8, apuTip);
            cabeceraStatement.setString(9, apuValApo);
            cabeceraStatement.setString(10, apuEstReg);
            cabeceraStatement.setString(11, apuCod);

            cabeceraStatement.executeUpdate();
            cabeceraStatement.close();

            // Actualizar la tabla de APUESTA_CAB
            loadCabeceraData();

            // Limpiar los campos de texto
            cancelarRegistro();
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
            ApuestaProgram programa = new ApuestaProgram();
            programa.createAndShowGUI();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
