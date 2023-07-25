package conexion;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ApuestaProgramV1 {
    private Connection connection;
    private Statement statement;
    private DefaultTableModel cabeceraTableModel;
    private DefaultTableModel detalleTableModel;
    private JTextField apuCodTextField;
    private JTextField apuCliCodTextField;
    private JTextField apuAfiCodTextField;
    private JTextField apuFecAñoTextField;
    private JTextField apuFecMesTextField;
    private JTextField apuFecDiaTextField;
    private JTextField apuFecHorTextField;
    private JTextField apuFecMinTextField;
    private JTextField apuTipTextField;
    private JTextField apuValApoTextField;
    private JTextField apuEstRegTextField;
    private JTable cabeceraTabla;
    private JTable detalleTabla;
    private JComboBox<String> apuCabCodComboBox;

    public ApuestaProgramV1() throws SQLException {
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
        panel.setBorder(createTitledBorder("Registro de Apuestas - Cabecera"));
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
        JLabel apuFecAñoLabel = new JLabel("ApuFecAño:");
        panel.add(apuFecAñoLabel, constraints);

        constraints.gridx = 1;
        apuFecAñoTextField = new JTextField(10);
        panel.add(apuFecAñoTextField, constraints);

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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(createTitledBorder("Tabla de Apuestas - Cabecera"));

        cabeceraTabla = new JTable();
        JScrollPane scrollPane = new JScrollPane(cabeceraTabla);
        panel.add(scrollPane, BorderLayout.CENTER);

        panel.add(createDetalleTablePanel(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDetalleTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(createTitledBorder("Tabla de Apuestas - Detalle"));

        detalleTabla = new JTable();
        JScrollPane scrollPane = new JScrollPane(detalleTabla);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(createDetalleButtonPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDetalleButtonPanel() {
        JPanel panel = new JPanel();
        JButton relacionarButton = new JButton("Relacionar detalle");
        relacionarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                relacionarDetalle();
            }
        });
        panel.add(relacionarButton);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout());

        JButton addButton = new JButton("Adicionar");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adicionarCabecera();
            }
        });
        panel1.add(addButton);

        JButton updateButton = new JButton("Modificar");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modificarCabecera();
            }
        });
        panel1.add(updateButton);

        JButton deleteButton = new JButton("Eliminar");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarCabecera();
            }
        });
        panel1.add(deleteButton);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelarCabecera();
            }
        });
        panel1.add(cancelButton);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());

        JButton inactiveButton = new JButton("Inactivar");
        inactiveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inactivarCabecera();
            }
        });

        panel2.add(inactiveButton);

        JButton reactivateButton = new JButton("Reactivar");
        reactivateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reactivarCabecera();
            }
        });
        panel2.add(reactivateButton);

        JButton actualizarButton = new JButton("Actualizar");
        actualizarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarCabecera();
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
            detalleTabla.setModel(detalleTableModel);
        } catch (SQLException e) {
            mostrarError("Error al cargar los datos de la tabla APUESTA_DET: " + e.getMessage());
        }
    }

    private void adicionarCabecera() {
        String apuCod = apuCodTextField.getText();
        String apuCliCod = apuCliCodTextField.getText();
        String apuAfiCod = apuAfiCodTextField.getText();
        String apuFecAño = apuFecAñoTextField.getText();
        String apuFecMes = apuFecMesTextField.getText();
        String apuFecDia = apuFecDiaTextField.getText();
        String apuFecHor = apuFecHorTextField.getText();
        String apuFecMin = apuFecMinTextField.getText();
        String apuTip = apuTipTextField.getText();
        String apuValApo = apuValApoTextField.getText();
        String apuEstReg = apuEstRegTextField.getText();

        try {
            String query = "INSERT INTO APUESTA_CAB (ApuCod, ApuCliCod, ApuAfiCod, ApuFecAño, ApuFecMes, ApuFecDia, ApuFecHor, ApuFecMin, ApuTip, ApuValApo, ApuEstReg) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, apuCod);
            preparedStatement.setString(2, apuCliCod);
            preparedStatement.setString(3, apuAfiCod);
            preparedStatement.setString(4, apuFecAño);
            preparedStatement.setString(5, apuFecMes);
            preparedStatement.setString(6, apuFecDia);
            preparedStatement.setString(7, apuFecHor);
            preparedStatement.setString(8, apuFecMin);
            preparedStatement.setString(9, apuTip);
            preparedStatement.setString(10, apuValApo);
            preparedStatement.setString(11, apuEstReg);

            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Actualizar la tabla
            loadCabeceraData();

            // Limpiar campos de texto
            apuCodTextField.setText("");
            apuCliCodTextField.setText("");
            apuAfiCodTextField.setText("");
            apuFecAñoTextField.setText("");
            apuFecMesTextField.setText("");
            apuFecDiaTextField.setText("");
            apuFecHorTextField.setText("");
            apuFecMinTextField.setText("");
            apuTipTextField.setText("");
            apuValApoTextField.setText("");
            apuEstRegTextField.setText("A");
        } catch (SQLException e) {
            mostrarError("Error al adicionar el registro en la tabla APUESTA_CAB: " + e.getMessage());
        }
    }

    private void modificarCabecera() {
        int selectedRow = cabeceraTabla.getSelectedRow();

        if (selectedRow >= 0) {
            String apuCod = (String) cabeceraTableModel.getValueAt(selectedRow, 0);
            String apuCliCod = (String) cabeceraTableModel.getValueAt(selectedRow, 1);
            String apuAfiCod = (String) cabeceraTableModel.getValueAt(selectedRow, 2);
            String apuFecAño = (String) cabeceraTableModel.getValueAt(selectedRow, 3);
            String apuFecMes = (String) cabeceraTableModel.getValueAt(selectedRow, 4);
            String apuFecDia = (String) cabeceraTableModel.getValueAt(selectedRow, 5);
            String apuFecHor = (String) cabeceraTableModel.getValueAt(selectedRow, 6);
            String apuFecMin = (String) cabeceraTableModel.getValueAt(selectedRow, 7);
            String apuTip = (String) cabeceraTableModel.getValueAt(selectedRow, 8);
            String apuValApo = (String) cabeceraTableModel.getValueAt(selectedRow, 9);
            String apuEstReg = (String) cabeceraTableModel.getValueAt(selectedRow, 10);

            apuCodTextField.setText(apuCod);
            apuCodTextField.setEditable(false);
            apuCliCodTextField.setText(apuCliCod);
            apuAfiCodTextField.setText(apuAfiCod);
            apuFecAñoTextField.setText(apuFecAño);
            apuFecMesTextField.setText(apuFecMes);
            apuFecDiaTextField.setText(apuFecDia);
            apuFecHorTextField.setText(apuFecHor);
            apuFecMinTextField.setText(apuFecMin);
            apuTipTextField.setText(apuTip);
            apuValApoTextField.setText(apuValApo);
            apuEstRegTextField.setText(apuEstReg);
        }
    }

    private void eliminarCabecera() {
        int selectedRow = cabeceraTabla.getSelectedRow();
        if (selectedRow >= 0) {
            String apuCod = (String) cabeceraTableModel.getValueAt(selectedRow, 0);
            String apuCliCod = (String) cabeceraTableModel.getValueAt(selectedRow, 1);
            String apuAfiCod = (String) cabeceraTableModel.getValueAt(selectedRow, 2);
            String apuFecAño = (String) cabeceraTableModel.getValueAt(selectedRow, 3);
            String apuFecMes = (String) cabeceraTableModel.getValueAt(selectedRow, 4);
            String apuFecDia = (String) cabeceraTableModel.getValueAt(selectedRow, 5);
            String apuFecHor = (String) cabeceraTableModel.getValueAt(selectedRow, 6);
            String apuFecMin = (String) cabeceraTableModel.getValueAt(selectedRow, 7);
            String apuTip = (String) cabeceraTableModel.getValueAt(selectedRow, 8);
            String apuValApo = (String) cabeceraTableModel.getValueAt(selectedRow, 9);
            String apuEstReg = (String) cabeceraTableModel.getValueAt(selectedRow, 10);

            apuCodTextField.setText(apuCod);
            apuCodTextField.setEditable(false);
            apuCliCodTextField.setText(apuCliCod);
            apuCliCodTextField.setEditable(false);
            apuAfiCodTextField.setText(apuAfiCod);
            apuFecAñoTextField.setText(apuFecAño);
            apuFecMesTextField.setText(apuFecMes);
            apuFecDiaTextField.setText(apuFecDia);
            apuFecHorTextField.setText(apuFecHor);
            apuFecMinTextField.setText(apuFecMin);
            apuTipTextField.setText(apuTip);
            apuValApoTextField.setText(apuValApo);
            apuEstRegTextField.setText(apuEstReg);
        }
    }

    private void inactivarCabecera() {
        int selectedRow = cabeceraTabla.getSelectedRow();
        if (selectedRow >= 0) {
            String apuCod = (String) cabeceraTableModel.getValueAt(selectedRow, 0);
            String apuCliCod = (String) cabeceraTableModel.getValueAt(selectedRow, 1);
            String apuAfiCod = (String) cabeceraTableModel.getValueAt(selectedRow, 2);
            String apuFecAño = (String) cabeceraTableModel.getValueAt(selectedRow, 3);
            String apuFecMes = (String) cabeceraTableModel.getValueAt(selectedRow, 4);
            String apuFecDia = (String) cabeceraTableModel.getValueAt(selectedRow, 5);
            String apuFecHor = (String) cabeceraTableModel.getValueAt(selectedRow, 6);
            String apuFecMin = (String) cabeceraTableModel.getValueAt(selectedRow, 7);
            String apuTip = (String) cabeceraTableModel.getValueAt(selectedRow, 8);
            String apuValApo = (String) cabeceraTableModel.getValueAt(selectedRow, 9);
            String apuEstReg = (String) cabeceraTableModel.getValueAt(selectedRow, 10);

            apuCodTextField.setText(apuCod);
            apuCodTextField.setEditable(false);
            apuCliCodTextField.setText(apuCliCod);
            apuCliCodTextField.setEditable(false);
            apuAfiCodTextField.setText(apuAfiCod);
            apuAfiCodTextField.setEditable(false);
            apuFecAñoTextField.setText(apuFecAño);
            apuFecAñoTextField.setEditable(false);
            apuFecMesTextField.setText(apuFecMes);
            apuFecMesTextField.setEditable(false);
            apuFecDiaTextField.setText(apuFecDia);
            apuFecDiaTextField.setEditable(false);
            apuFecHorTextField.setText(apuFecHor);
            apuFecHorTextField.setEditable(false);
            apuFecMinTextField.setText(apuFecMin);
            apuFecMinTextField.setEditable(false);
            apuTipTextField.setText(apuTip);
            apuTipTextField.setEditable(false);
            apuValApoTextField.setText(apuValApo);
            apuValApoTextField.setEditable(false);
            apuEstRegTextField.setText("I");
        }
    }

    private void reactivarCabecera() {
        int selectedRow = cabeceraTabla.getSelectedRow();
        if (selectedRow >= 0) {
            String apuCod = (String) cabeceraTableModel.getValueAt(selectedRow, 0);
            String apuCliCod = (String) cabeceraTableModel.getValueAt(selectedRow, 1);
            String apuAfiCod = (String) cabeceraTableModel.getValueAt(selectedRow, 2);
            String apuFecAño = (String) cabeceraTableModel.getValueAt(selectedRow, 3);
            String apuFecMes = (String) cabeceraTableModel.getValueAt(selectedRow, 4);
            String apuFecDia = (String) cabeceraTableModel.getValueAt(selectedRow, 5);
            String apuFecHor = (String) cabeceraTableModel.getValueAt(selectedRow, 6);
            String apuFecMin = (String) cabeceraTableModel.getValueAt(selectedRow, 7);
            String apuTip = (String) cabeceraTableModel.getValueAt(selectedRow, 8);
            String apuValApo = (String) cabeceraTableModel.getValueAt(selectedRow, 9);
            String apuEstReg = (String) cabeceraTableModel.getValueAt(selectedRow, 10);

            apuCodTextField.setText(apuCod);
            apuCodTextField.setEditable(false);
            apuCliCodTextField.setText(apuCliCod);
            apuCliCodTextField.setEditable(false);
            apuAfiCodTextField.setText(apuAfiCod);
            apuAfiCodTextField.setEditable(false);
            apuFecAñoTextField.setText(apuFecAño);
            apuFecAñoTextField.setEditable(false);
            apuFecMesTextField.setText(apuFecMes);
            apuFecMesTextField.setEditable(false);
            apuFecDiaTextField.setText(apuFecDia);
            apuFecDiaTextField.setEditable(false);
            apuFecHorTextField.setText(apuFecHor);
            apuFecHorTextField.setEditable(false);
            apuFecMinTextField.setText(apuFecMin);
            apuFecMinTextField.setEditable(false);
            apuTipTextField.setText(apuTip);
            apuTipTextField.setEditable(false);
            apuValApoTextField.setText(apuValApo);
            apuValApoTextField.setEditable(false);
            apuEstRegTextField.setText("A");
        }
    }

    private void actualizarCabecera() {
        String apuCod = apuCodTextField.getText();
        String apuCliCod = apuCliCodTextField.getText();
        String apuAfiCod = apuAfiCodTextField.getText();
        String apuFecAño = apuFecAñoTextField.getText();
        String apuFecMes = apuFecMesTextField.getText();
        String apuFecDia = apuFecDiaTextField.getText();
        String apuFecHor = apuFecHorTextField.getText();
        String apuFecMin = apuFecMinTextField.getText();
        String apuTip = apuTipTextField.getText();
        String apuValApo = apuValApoTextField.getText();
        String apuEstReg = apuEstRegTextField.getText();

        try {
            String query = "UPDATE APUESTA_CAB SET ApuCliCod = ?, ApuAfiCod = ?, ApuFecAño = ?, ApuFecMes = ?, ApuFecDia = ?, ApuFecHor = ?, ApuFecMin = ?, ApuTip = ?, ApuValApo = ?, ApuEstReg = ? WHERE ApuCod = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, apuCliCod);
            preparedStatement.setString(2, apuAfiCod);
            preparedStatement.setString(3, apuFecAño);
            preparedStatement.setString(4, apuFecMes);
            preparedStatement.setString(5, apuFecDia);
            preparedStatement.setString(6, apuFecHor);
            preparedStatement.setString(7, apuFecMin);
            preparedStatement.setString(8, apuTip);
            preparedStatement.setString(9, apuValApo);
            preparedStatement.setString(10, apuEstReg);
            preparedStatement.setString(11, apuCod);

            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Actualizar la tabla
            loadCabeceraData();

            // Limpiar campos de texto
            apuCodTextField.setText("");
            apuCodTextField.setEditable(true);
            apuCliCodTextField.setText("");
            apuCliCodTextField.setEditable(true);
            apuAfiCodTextField.setText("");
            apuFecAñoTextField.setText("");
            apuFecMesTextField.setText("");
            apuFecDiaTextField.setText("");
            apuFecHorTextField.setText("");
            apuFecMinTextField.setText("");
            apuTipTextField.setText("");
            apuValApoTextField.setText("");
            apuEstRegTextField.setText("");
        } catch (SQLException e) {
            mostrarError("Error al actualizar el registro en la tabla APUESTA_CAB: " + e.getMessage());
        }
    }

    private void cancelarCabecera() {
        apuCodTextField.setText("");
        apuCodTextField.setEditable(true);
        apuCliCodTextField.setText("");
        apuCliCodTextField.setEditable(true);
        apuAfiCodTextField.setText("");
        apuFecAñoTextField.setText("");
        apuFecMesTextField.setText("");
        apuFecDiaTextField.setText("");
        apuFecHorTextField.setText("");
        apuFecMinTextField.setText("");
        apuTipTextField.setText("");
        apuValApoTextField.setText("");
        apuEstRegTextField.setText("");
    }

    private void relacionarDetalle() {
        int selectedRow = cabeceraTabla.getSelectedRow();
        if (selectedRow >= 0) {
            String apuCod = (String) cabeceraTableModel.getValueAt(selectedRow, 0);

            // Ejemplo de cómo relacionar el detalle a la cabecera usando apuCod
            // ...
        }
    }

    private void salirPrograma() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            mostrarError("Error al cerrar la conexión: " + e.getMessage());
        }

        System.exit(0);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    ApuestaProgramV1 programa = new ApuestaProgramV1();
                    programa.createAndShowGUI();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
