	package conexion;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;

public class PartidoProgram {
    private Connection connection;
    private Statement statement;
    private DefaultTableModel tableModel;
    private JTextField codigoTextField;
    private JComboBox<String> torneoComboBox;
    private JComboBox<String> equipoLocalComboBox;
    private JComboBox<String> equipoVisitanteComboBox;
    private JTextField fechaTextField;
    private JTextField tiempoTextField;
    private JComboBox<String> equipoGanadorComboBox;
    private JTextField estRegTextField;
    private JTable tablaItems;

    public PartidoProgram() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/casaapuestas", "root", "admin");
        statement = connection.createStatement();
    }

    private Border createTitledBorder(String title) {
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        return BorderFactory.createTitledBorder(border, title);
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Partido");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(createFormPanel(), BorderLayout.NORTH);
        panel.add(createTablePanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);

        loadData();
        loadTorneos();
        loadEquiposLocales();
        loadEquiposVisitantes();
        loadEquiposGanadores();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(createTitledBorder("Registro de Partido"));
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
        JLabel torneoLabel = new JLabel("Torneo:");
        panel.add(torneoLabel, constraints);

        constraints.gridx = 1;
        torneoComboBox = new JComboBox<>();
        panel.add(torneoComboBox, constraints);

        constraints.gridy = 2;
        constraints.gridx = 0;
        JLabel equipoLocalLabel = new JLabel("Equipo Local:");
        panel.add(equipoLocalLabel, constraints);

        constraints.gridx = 1;
        equipoLocalComboBox = new JComboBox<>();
        panel.add(equipoLocalComboBox, constraints);

        constraints.gridy = 3;
        constraints.gridx = 0;
        JLabel equipoVisitanteLabel = new JLabel("Equipo Visitante:");
        panel.add(equipoVisitanteLabel, constraints);

        constraints.gridx = 1;
        equipoVisitanteComboBox = new JComboBox<>();
        panel.add(equipoVisitanteComboBox, constraints);

        constraints.gridy = 4;
        constraints.gridx = 0;
        JLabel fechaLabel = new JLabel("Fecha (Año/Mes/Día):");
        panel.add(fechaLabel, constraints);

        constraints.gridx = 1;
        // Máscara para año, mes y día
        try {
            MaskFormatter fechaMask = new MaskFormatter("####-##-##");
            fechaMask.setPlaceholderCharacter('_');
            fechaTextField = new JFormattedTextField(fechaMask);
        } catch (ParseException e) {
            fechaTextField = new JFormattedTextField();
        }
        fechaTextField.setColumns(10);
        panel.add(fechaTextField, constraints);;

        constraints.gridy = 5;
        constraints.gridx = 0;
        JLabel horaLabel = new JLabel("Hora (HH:MM):");
        panel.add(horaLabel, constraints);

        constraints.gridx = 1;
        // Máscara para hora y minutos
        try {
            MaskFormatter horaMask = new MaskFormatter("##:##");
            horaMask.setPlaceholderCharacter('_');
            tiempoTextField = new JFormattedTextField(horaMask);
        } catch (ParseException e) {
            tiempoTextField = new JFormattedTextField();
        }
        tiempoTextField.setColumns(10);
        
        panel.add(tiempoTextField, constraints);

        constraints.gridy = 6;
        constraints.gridx = 0;
        JLabel equipoGanadorLabel = new JLabel("Equipo Ganador:");
        panel.add(equipoGanadorLabel, constraints);

        constraints.gridx = 1;
        equipoGanadorComboBox = new JComboBox<>();
        panel.add(equipoGanadorComboBox, constraints);

        constraints.gridy = 7;
        constraints.gridx = 0;
        JLabel estRegLabel = new JLabel("Estado Registro:");
        panel.add(estRegLabel, constraints);

        constraints.gridy = 7;
        constraints.gridx = 1;
        estRegTextField = new JTextField(2);
        estRegTextField.setText("A");
        estRegTextField.setEditable(false);
        panel.add(estRegTextField, constraints);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new FlowLayout());

        panel.setBorder(createTitledBorder("Tabla de Partidos"));

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
            ResultSet resultSet = statement.executeQuery("SELECT * FROM partido");
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

    private void loadTorneos() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM torneo");
            while (resultSet.next()) {
                String torneoNom = resultSet.getString("TorNom");
                torneoComboBox.addItem(torneoNom);
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar los torneos: " + e.getMessage());
        }
    }

    private void loadEquiposLocales() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT EquNom FROM equipo_local INNER JOIN equipo ON equipo_local.EquCod = equipo.EquCod");
            while (resultSet.next()) {
                String equipoNom = resultSet.getString("EquNom");
                equipoLocalComboBox.addItem(equipoNom);
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar los equipos locales: " + e.getMessage());
        }
    }

    private void loadEquiposVisitantes() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT EquNom FROM equipo_visitante INNER JOIN equipo ON equipo_visitante.EquCod = equipo.EquCod");
            while (resultSet.next()) {
                String equipoNom = resultSet.getString("EquNom");
                equipoVisitanteComboBox.addItem(equipoNom);
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar los equipos visitantes: " + e.getMessage());
        }
    }

    private void loadEquiposGanadores() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT EquNom FROM equipo");
            while (resultSet.next()) {
                String equipoNom = resultSet.getString("EquNom");
                equipoGanadorComboBox.addItem(equipoNom);
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar los equipos ganadores: " + e.getMessage());
        }
    }

    private String getTorneoCod(String torneoNom) throws SQLException {
        String torneoCod = null;
        String query = "SELECT TorCod FROM torneo WHERE TorNom = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, torneoNom);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            torneoCod = resultSet.getString("TorCod");
        }
        preparedStatement.close();
        return torneoCod;
    }
    
    private String getTorneoNom(String torneoCod) throws SQLException {
        String torneoNom = null;
        String query = "SELECT TorNom FROM torneo WHERE TorCod = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, torneoCod);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            torneoNom = resultSet.getString("TorNom");
        }
        preparedStatement.close();
        return torneoNom;
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
    
    private String getEquipoLocalCod(String equipoNom) throws SQLException {
        String equipoCod = null;
        String query = "SELECT ev.EquLocCod FROM EQUIPO_LOCAL AS ev JOIN EQUIPO AS e ON ev.EquCod = e.EquCod WHERE e.EquNom = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, equipoNom);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            equipoCod = resultSet.getString("EquLocCod");
        }
        preparedStatement.close();
        return equipoCod;
    }
    
    private String getEquipoVisitanteCod(String equipoNom) throws SQLException {
        String equipoCod = null;
        String query = "SELECT ev.EquVisCod FROM EQUIPO_VISITANTE AS ev JOIN EQUIPO AS e ON ev.EquCod = e.EquCod WHERE e.EquNom = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, equipoNom);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            equipoCod = resultSet.getString("EquVisCod");
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

    private String getEquipoLocalNom(String equipoCod) throws SQLException {
        String equipoNom = null;
        String query = "SELECT e.EquNom FROM equipo_local ev JOIN equipo e ON ev.EquCod = e.EquCod WHERE ev.EquLocCod = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, equipoCod);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            equipoNom = resultSet.getString("EquNom");
        }
        preparedStatement.close();
        return equipoNom;
    }

    private String getEquipoVisitanteNom(String equipoCod) throws SQLException {
        String equipoNom = null;
        String query = "SELECT e.EquNom FROM equipo_visitante ev JOIN equipo e ON ev.EquCod = e.EquCod WHERE ev.EquVisCod = ?";
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
        String torneoNom = (String) torneoComboBox.getSelectedItem();
        String equipoLocalNom = (String) equipoLocalComboBox.getSelectedItem();
        String equipoVisitanteNom = (String) equipoVisitanteComboBox.getSelectedItem();
        String fecha = fechaTextField.getText();
        String fechaAño = fecha.substring(0,4);
        String fechaMes = fecha.substring(5,7);
        String fechaDia = fecha.substring(8);
        String tiempo = tiempoTextField.getText();
        String fechaHora = tiempo.substring(0,2);
        String fechaMinuto = tiempo.substring(3);
        String equipoGanadorNom = (String) "" + equipoGanadorComboBox.getSelectedItem();
        String estReg = estRegTextField.getText();

        if (codigo.isEmpty() || codigo.isBlank()) {
            mostrarError("El código está en blanco o no es válido");
            return;
        }

        try {
            String torneoCod = getTorneoCod(torneoNom);
            String equipoLocalCod = getEquipoLocalCod(equipoLocalNom);
            String equipoVisitanteCod = getEquipoVisitanteCod(equipoVisitanteNom);
            String equipoGanadorCod = getEquipoCod(equipoGanadorNom);

            String query = "INSERT INTO partido (ParCod, ParTorCod, ParEquLocCod, ParEquVisCod, ParFecAño, ParFecMes, ParFecDia, ParFecHor, ParFecMin, ParGanCod, ParEstReg) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, codigo);
            preparedStatement.setString(2, torneoCod);
            preparedStatement.setString(3, equipoLocalCod);
            preparedStatement.setString(4, equipoVisitanteCod);
            preparedStatement.setString(5, fechaAño);
            preparedStatement.setString(6, fechaMes);
            preparedStatement.setString(7, fechaDia);
            preparedStatement.setString(8, fechaHora);
            preparedStatement.setString(9, fechaMinuto);
            preparedStatement.setString(10, equipoGanadorCod);
            preparedStatement.setString(11, estReg);

            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Actualizar la tabla
            loadData();

            // Limpiar campos de texto
            cancelarRegistro();
        } catch (SQLException e) {
            mostrarError("Error al adicionar el registro: " + e.getMessage());
        }
    }

    private void modificarRegistro() throws SQLException {
        int selectedRow = tablaItems.getSelectedRow();

        if (selectedRow >= 0) {
            String codigo = (String) "" + tableModel.getValueAt(selectedRow, 0);
            setFormulario(codigo);
            // Campos a editar
            torneoComboBox.setEditable(true);
            equipoLocalComboBox.setEditable(true);
            equipoVisitanteComboBox.setEditable(true);
            fechaTextField.setEditable(true);
            tiempoTextField.setEditable(true);
            equipoGanadorComboBox.setEditable(true);
        }
    }

    private void eliminarRegistro() throws SQLException {
        int selectedRow = tablaItems.getSelectedRow();
        if (selectedRow >= 0) {
            String codigo = (String) "" + tableModel.getValueAt(selectedRow, 0);
            setFormulario(codigo);
            // eliminar
            estRegTextField.setText("*");
        }
    }

    private void inactivarRegistro() throws SQLException {
        int selectedRow = tablaItems.getSelectedRow();
        if (selectedRow >= 0) {
            String codigo = (String) "" + tableModel.getValueAt(selectedRow, 0);
            setFormulario(codigo);
            // Inactivar
            estRegTextField.setText("I");
        }
    }

    private void reactivarRegistro() throws SQLException {
        int selectedRow = tablaItems.getSelectedRow();
        if (selectedRow >= 0) {
            String codigo = (String) "" + tableModel.getValueAt(selectedRow, 0);
            setFormulario(codigo);
            // Reactivar
            estRegTextField.setText("A");
        }
    }

    private void actualizarRegistro() {
        
        String codigo = codigoTextField.getText();
        String torneoNom = (String) torneoComboBox.getSelectedItem();
        String equipoLocalNom = (String) equipoLocalComboBox.getSelectedItem();
        String equipoVisitanteNom = (String) equipoVisitanteComboBox.getSelectedItem();
        String fecha = fechaTextField.getText();
        String fechaAño = fecha.substring(0,4);
        String fechaMes = fecha.substring(5,7);
        String fechaDia = fecha.substring(8);
        String tiempo = tiempoTextField.getText();
        String fechaHora = tiempo.substring(0,2);
        String fechaMinuto = tiempo.substring(3);
        String equipoGanadorNom = (String) "" + equipoGanadorComboBox.getSelectedItem();
        String estReg = estRegTextField.getText();

        if (codigo.isEmpty() || codigo.isBlank()) {
            mostrarError("El código está en blanco o no es válido");
            return;
        }

        try {
            String torneoCod = getTorneoCod(torneoNom);
            String equipoLocalCod = getEquipoLocalCod(equipoLocalNom);
            String equipoVisitanteCod = getEquipoVisitanteCod(equipoVisitanteNom);
            String equipoGanadorCod = getEquipoCod(equipoGanadorNom);

            String query = "UPDATE partido SET ParTorCod = ?, ParEquLocCod = ?, ParEquVisCod = ?, ParFecAño = ?, ParFecMes = ?, ParFecDia = ?, ParFecHor = ?, ParFecMin = ?, ParGanCod = ?, ParEstReg = ? WHERE ParCod = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, torneoCod);
            preparedStatement.setString(2, equipoLocalCod);
            preparedStatement.setString(3, equipoVisitanteCod);
            preparedStatement.setString(4, fechaAño);
            preparedStatement.setString(5, fechaMes);
            preparedStatement.setString(6, fechaDia);
            preparedStatement.setString(7, fechaHora);
            preparedStatement.setString(8, fechaMinuto);
            preparedStatement.setString(9, equipoGanadorCod);
            preparedStatement.setString(10, estReg);
            preparedStatement.setString(11, codigo);

            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Actualizar la tabla
            loadData();

            // Limpiar campos de texto
            cancelarRegistro();
        } catch (SQLException e) {
            mostrarError("Error al modificar el registro: " + e.getMessage());
        }
    }
    
    private void setFormulario(String parCod) {
      	 try {
               // Obtener los campos relacionados de la base de datos
               String query = "SELECT ParTorCod, ParEquLocCod, ParEquVisCod, ParFecAño, ParFecMes, ParFecDia, ParFecHor, ParFecMin, ParGanCod FROM PARTIDO WHERE ParCod = ?";
               PreparedStatement statement = connection.prepareStatement(query);
               statement.setString(1, parCod);
               ResultSet resultSet = statement.executeQuery();

               if (resultSet.next()) {
                   String parTorCod = resultSet.getString("ParTorCod");
                   String parEquLocCod = resultSet.getString("ParEquLocCod");
                   String parEquVisCod = resultSet.getString("parEquVisCod");
                   String parFecAño = String.format("%02d", Integer.parseInt(resultSet.getString("ParFecAño")));
                   String parFecMes = String.format("%02d", Integer.parseInt(resultSet.getString("ParFecMes")));
                   String parFecDia = String.format("%02d", Integer.parseInt(resultSet.getString("ParFecDia")));
                   String parFecAñoMesDia = parFecAño + parFecMes + parFecDia;
                   String parFecHor = String.format("%02d", Integer.parseInt(resultSet.getString("ParFecHor")));
                   String parFecMin = String.format("%02d", Integer.parseInt(resultSet.getString("ParFecMin")));
                   String parFecHorMin = parFecHor + parFecMin;
                   String parGanCod = resultSet.getString("ParGanCod");
                   // Establecer valores
                   codigoTextField.setText(parCod);
                   codigoTextField.setEditable(false);
                   torneoComboBox.setSelectedItem(getTorneoNom(parTorCod));
                   torneoComboBox.setEditable(false);
                   equipoLocalComboBox.setSelectedItem(getEquipoLocalNom(parEquLocCod));
                   equipoLocalComboBox.setEditable(false);
                   equipoVisitanteComboBox.setSelectedItem(getEquipoVisitanteNom(parEquVisCod));
                   equipoVisitanteComboBox.setEditable(false);
                   fechaTextField.setText(parFecAñoMesDia);
                   fechaTextField.setEditable(false);
                   tiempoTextField.setText(parFecHorMin);
                   tiempoTextField.setEditable(false);
                   equipoGanadorComboBox.setSelectedItem(getEquipoNom(parGanCod));
                   equipoGanadorComboBox.setEditable(false);
              }
      	 }
      	 catch (SQLException e) {
      		 
      		 mostrarError("Error al obtener los campos relacionados: " + e.getMessage());
      	 }
      }

    private void cancelarRegistro() {
        codigoTextField.setText("");
        codigoTextField.setEditable(true);
        torneoComboBox.setSelectedIndex(0);
        equipoLocalComboBox.setSelectedIndex(0);
        equipoVisitanteComboBox.setSelectedIndex(0);
        fechaTextField.setText("");
        tiempoTextField.setText("");
        equipoGanadorComboBox.setSelectedIndex(0);
        estRegTextField.setText("A");
        estRegTextField.setEditable(false);
    }

    private void salirPrograma() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            mostrarError("Error al cerrar la conexión: " + e.getMessage());
        }
        System.exit(0);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            PartidoProgram programa = new PartidoProgram();
            programa.createAndShowGUI();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al establecer la conexión con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
