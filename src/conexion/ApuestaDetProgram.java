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

public class ApuestaDetProgram {
	private Connection connection;
	private Statement statement;
	private DefaultTableModel tableModel;
	private JTextField codigoTextField;
	private JComboBox<String> cabeceraComboBox;
	private JComboBox<String> partidoComboBox;
	private JTextField cuotaTextField;
	private JTextField estRegTextField;
	private JTable tablaItems;

	public ApuestaDetProgram() throws SQLException {
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/casaapuestas", "root", "admin");
		statement = connection.createStatement();
	}

	private Border createTitledBorder(String title) {
		Border border = BorderFactory.createLineBorder(Color.GRAY);
		return BorderFactory.createTitledBorder(border, title);
	}

	public void createAndShowGUI() {
		JFrame frame = new JFrame("Apuesta Detalle");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new BorderLayout());

		panel.add(createFormPanel(), BorderLayout.NORTH);
		panel.add(createTablePanel(), BorderLayout.CENTER);
		panel.add(createButtonPanel(), BorderLayout.SOUTH);

		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);

		loadData();
		loadCabeceras();
		loadPartidos();
	}

	private JPanel createFormPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(createTitledBorder("Registro de Apuesta Detalle"));
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
		JLabel cabeceraLabel = new JLabel("Cabecera:");
		panel.add(cabeceraLabel, constraints);

		constraints.gridx = 1;
		cabeceraComboBox = new JComboBox<>();
		panel.add(cabeceraComboBox, constraints);

		constraints.gridy = 2;
		constraints.gridx = 0;
		JLabel partidoLabel = new JLabel("Partido:");
		panel.add(partidoLabel, constraints);

		constraints.gridx = 1;
		partidoComboBox = new JComboBox<>();
		panel.add(partidoComboBox, constraints);

		constraints.gridy = 3;
		constraints.gridx = 0;
		JLabel cuotaLabel = new JLabel("Cuota:");
		panel.add(cuotaLabel, constraints);

		constraints.gridx = 1;
		cuotaTextField = new JTextField(10);
		panel.add(cuotaTextField, constraints);

		constraints.gridy = 4;
		constraints.gridx = 0;
		JLabel estRegLabel = new JLabel("Estado Registro:");
		panel.add(estRegLabel, constraints);

		constraints.gridy = 4;
		constraints.gridx = 1;
		estRegTextField = new JTextField(2);
		estRegTextField.setText("A");
		estRegTextField.setEditable(false);
		panel.add(estRegTextField, constraints);

		return panel;
	}

	private JPanel createTablePanel() {
		JPanel panel = new JPanel(new FlowLayout());

		panel.setBorder(createTitledBorder("Tabla de Apuestas Detalle"));

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
			ResultSet resultSet = statement.executeQuery("SELECT * FROM apuesta_det");
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
			mostrarError("Error al acceder a los datos: " + e.getMessage());
		}
	}
	private void loadCabeceras() {
		try {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM APUESTA_CAB");
			while (resultSet.next()) {
				String codigo = resultSet.getString("ApuCod");
				cabeceraComboBox.addItem(codigo);
			}
		} catch (SQLException e) {
			mostrarError("Error al cargar las cabeceras: " + e.getMessage());
		}
	}

	private void loadPartidos() {
		try {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM partido");
			while (resultSet.next()) {
				String codigo = resultSet.getString("ParCod");
				partidoComboBox.addItem(codigo);
			}
		} catch (SQLException e) {
			mostrarError("Error al cargar los partidos: " + e.getMessage());
		}
	}

	private void adicionarRegistro() {
		try {
			String codigo = codigoTextField.getText();
			String cabecera = cabeceraComboBox.getSelectedItem().toString();
			String partido = partidoComboBox.getSelectedItem().toString();
			String cuota = cuotaTextField.getText();
			String estReg = estRegTextField.getText();

			if (codigo.isEmpty() || cabecera.isEmpty() || partido.isEmpty()) {
				mostrarError("Campos son obligatorios");
				return;
			}

			String query = "INSERT INTO APUESTA_DET (ApuDetCod, ApuCabCod, ApuDetParCod, ApuDetCuo, ApuDetEstReg) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, codigo);
			preparedStatement.setString(2, cabecera);
			preparedStatement.setString(3, partido);
			preparedStatement.setString(4, cuota);
			preparedStatement.setString(5, estReg);
			preparedStatement.executeUpdate();

			// Recargar datos
			loadData();
			// limpiar formulario
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
			cabeceraComboBox.setEditable(true);
			partidoComboBox.setEditable(true);
			cuotaTextField.setEditable(true);
		}

	}

	private void eliminarRegistro() throws SQLException {
		int selectedRow = tablaItems.getSelectedRow();
		if (selectedRow >= 0) {
			String codigo = (String) "" + tableModel.getValueAt(selectedRow, 0);
			setFormulario(codigo);
			// Eliminar
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
		try {
			String codigo = codigoTextField.getText();
			String cabecera = cabeceraComboBox.getSelectedItem().toString();
			String partido = partidoComboBox.getSelectedItem().toString();
			String cuota = cuotaTextField.getText();
			String estReg = estRegTextField.getText();

			if (codigo.isEmpty() || cabecera.isEmpty() || partido.isEmpty()) {
				mostrarError("Campos obligatorios");
				return;
			}

			String query = "UPDATE APUESTA_DET SET ApuCabCod = ?, ApuDetParCod = ?, ApuDetCuo = ?, ApuDetEstReg = ? WHERE ApuDetCod = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, cabecera);
			preparedStatement.setString(2, partido);
			preparedStatement.setString(3, cuota);
			preparedStatement.setString(4, estReg);
			preparedStatement.setString(5, codigo);
			preparedStatement.executeUpdate();

			// Recargar datos
			loadData();
			// limpiar formulario
			cancelarRegistro();
		} catch (SQLException e) {
			mostrarError("Error al actualizar el registro: " + e.getMessage());
		}
	}

	private void salirPrograma() {
		System.exit(0);
	}

	private void limpiarCampos() {
		codigoTextField.setText("");
		codigoTextField.setEditable(true);
		cabeceraComboBox.setSelectedIndex(0);
		cabeceraComboBox.setEditable(true);
		partidoComboBox.setSelectedIndex(0);
		partidoComboBox.setEditable(true);
		cuotaTextField.setText("");
		cuotaTextField.setEditable(true);
		estRegTextField.setText("A");
	}

	private void setFormulario(String apuDetCod) {
		try {
			// Obtener los campos relacionados de la base de datos
			String query = "SELECT ApuCabCod, ApuDetParCod, ApuDetCuo, ApuDetEstReg FROM APUESTA_DET WHERE ApuDetCod = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, apuDetCod);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				String apuCabCod = resultSet.getString("ApuCabCod");
				String apuDetParCod = resultSet.getString("ApuDetParCod");
				String apuDetCuo = resultSet.getString("ApuDetCuo");
				String apuDetEstReg = resultSet.getString("ApuDetEstReg");
				// Establecer valores
				codigoTextField.setText(apuDetCod);
				codigoTextField.setEditable(false);
				cabeceraComboBox.setSelectedItem(apuCabCod);
				cabeceraComboBox.setEditable(false);
				partidoComboBox.setSelectedItem(apuDetParCod);
				partidoComboBox.setEditable(false);
				cuotaTextField.setText(apuDetCuo);
				cuotaTextField.setEditable(false);
				estRegTextField.setText(apuDetEstReg);
				estRegTextField.setEditable(false);
			}
		}
		catch (SQLException e) {

			mostrarError("Error al obtener los campos relacionados: " + e.getMessage());
		}
	}

	private void cancelarRegistro() {
		limpiarCampos();
		tablaItems.clearSelection();
	}


	private void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					ApuestaDetProgram program = new ApuestaDetProgram();
					program.createAndShowGUI();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}

