package conexion;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableProgram {
	private Connection connection;
	private Statement statement;
	private DefaultTableModel tableModel;
	private JTextField[] textFields;
	private JTable tablaItems;
	private String selectedTable;
	private String tablaEstReg;
	private String columnEstReg;

	public TableProgram(String table) throws SQLException {
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/casaapuestas", "root", "admin");
		tablaEstReg = "estado_registro";
		columnEstReg = "EstReg";
		selectedTable = table;
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
	    panel.setBorder(createTitledBorder("Registro de Estados de registros"));
	    GridBagConstraints constraints = new GridBagConstraints();
	    constraints.gridx = 0;
	    constraints.gridy = 0;
	    constraints.anchor = GridBagConstraints.WEST;
	    constraints.insets = new Insets(5, 5, 5, 5);
	    List<String> columnNames = getTableColumnNames(selectedTable);
	    textFields = new JTextField[columnNames.size()];
	    JComboBox<String> comboBox = null;
	    for (int i = 0; i < columnNames.size(); i++) {
	        String columnName = columnNames.get(i);
	        JLabel label = new JLabel(columnName + ":");
	        panel.add(label, constraints);
	        constraints.gridx = 1;
	        	
	        JTextField textField = new JTextField(10);
	        textFields[i] = textField;
	        panel.add(textField, constraints);
	        if (i >= columnNames.size() - 1) {
	        	textField.setText("A");
	        	textField.setEditable(false);
	        }
	        constraints.gridy++;
	        constraints.gridx = 0;
	    }
	    return panel;
	}
	
	private boolean isForeignKey(String columnName) {
	    boolean isForeignKey = false;
	    try {
	        DatabaseMetaData metaData = connection.getMetaData();
	        ResultSet foreignKeys = metaData.getImportedKeys(null, null, selectedTable);
	        while (foreignKeys.next()) {
	            String foreignKeyColumnName = foreignKeys.getString("FKCOLUMN_NAME");
	            if (foreignKeyColumnName.equals(columnName)) {
	                isForeignKey = true;
	                break;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return isForeignKey;
	}

	private String[] getForeignKeyValues(String columnName) {
	    List<String> values = new ArrayList<>();
	    try {
	        Statement statement = connection.createStatement();
	        ResultSet resultSet = statement.executeQuery("SELECT DISTINCT " + columnName + " FROM " + tablaEstReg);
	        while (resultSet.next()) {
	            values.add(resultSet.getString(columnName));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return values.toArray(new String[values.size()]);
	}

	private JPanel createTablePanel() {
		JPanel panel = new JPanel(new FlowLayout());

		panel.setBorder(createTitledBorder("Tabla estados de registros"));

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

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(panel1, BorderLayout.NORTH);
		panel.add(panel2, BorderLayout.SOUTH);

		return panel;
	}

	private void adicionarRegistro() {
		String tableName = selectedTable;
		List<String> columnNames = getTableColumnNames(tableName);
		List<String> values = new ArrayList<>();

		for (int i = 0; i < columnNames.size(); i++) {
			String value = textFields[i].getText();
			values.add(value);
		}

		try {
			String query = "INSERT INTO " + tableName + " (";
			for (int i = 0; i < columnNames.size(); i++) {
				query += columnNames.get(i);
				if (i != columnNames.size() - 1) {
					query += ",";
				}
			}
			query += ") VALUES (";
			for (int i = 0; i < values.size(); i++) {
				query += "'" + values.get(i) + "'";
				if (i != values.size() - 1) {
					query += ",";
				}
			}
			query += ")";
			statement.executeUpdate(query);
			loadData();
			clearForm();
		} catch (SQLException e) {
			 showError("Error al adicionar: " + e.getMessage());
		}
	}

	private void modificarRegistro() {
		int selectedRow = tablaItems.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(null, "Selecciona un registro para modificar.");
			return;
		}
		String tableName = selectedTable;
		List<String> columnNames = getTableColumnNames(tableName);
		List<String> values = new ArrayList<>();

		for (int i = 0; i < columnNames.size(); i++) {
			String value = textFields[i].getText();
			values.add(value);
		}

		String idColumnName = (String) "" + columnNames.get(0);
		String idValue = (String) "" + values.get(0);

		try {
			String query = "UPDATE " + tableName + " SET ";
			for (int i = 1; i < columnNames.size(); i++) {
				query += columnNames.get(i) + "='" + values.get(i) + "'";
				if (i != columnNames.size() - 1) {
					query += ",";
				}
			}
			query += " WHERE " + idColumnName + "='" + idValue + "'";
			statement.executeUpdate(query);
			loadData();
			clearForm();
		} catch (SQLException e) {
			 showError("Error al modificar: " + e.getMessage());
		}
	}

	private void eliminarRegistro() {
		int selectedRow = tablaItems.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(null, "Selecciona un registro para eliminar.");
			return;
		}
		String tableName = selectedTable;
		List<String> columnNames = getTableColumnNames(tableName);
		String idColumnName = (String) "" + columnNames.get(0);
		String idValue = (String) " " + tablaItems.getValueAt(selectedRow, 0);

		try {
			String query = "DELETE FROM " + tableName + " WHERE " + idColumnName + "='" + idValue + "'";
			statement.executeUpdate(query);
			loadData();
			clearForm();
		} catch (SQLException e) {
			 showError("Error al eliminar: " + e.getMessage());
		}
	}

	private void cancelarRegistro() {
		clearForm();
	}

	private void inactivarRegistro() {
		int selectedRow = tablaItems.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(null, "Selecciona un registro para inactivar.");
			return;
		}
		String tableName = selectedTable;
		List<String> columnNames = getTableColumnNames(tableName);
		String idColumnName = (String) "" + columnNames.get(0);
		String idValue = (String) "" + tablaItems.getValueAt(selectedRow, 0);

		try {
			String query = "UPDATE " + tableName + " SET estado='inactivo' WHERE " + idColumnName + "='" + idValue + "'";
			statement.executeUpdate(query);
			loadData();
			clearForm();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void reactivarRegistro() {
		int selectedRow = tablaItems.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(null, "Selecciona un registro para activar.");
			return;
		}
		String tableName = selectedTable;
		List<String> columnNames = getTableColumnNames(tableName);
		String idColumnName = (String) "" + columnNames.get(0);
		String idValue = (String) "" + tablaItems.getValueAt(selectedRow, 0);
		String idEstReg = (String) "" + columnNames.get(columnNames.size() - 1);

		try {
			String query = "UPDATE " + tableName + " SET " + idEstReg + "='A' WHERE " + idColumnName + "='" + idValue + "'";
			statement.executeUpdate(query);
			loadData();
			clearForm();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void actualizarRegistro() {
    	String codigo = null;
        String descripcion = null;
        String estReg = null;
        
    	/*try {
        	codigo = codigoTextField.getText();
            descripcion = descripcionTextField.getText();
            estReg = estRegTextField.getText();
            
            String query = "UPDATE estado_registro SET EstRegDes = ?, estRegEstReg = ? WHERE EstReg = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, descripcion);
            preparedStatement.setString(2, estReg);
            preparedStatement.setString(3, codigo);

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
            showError("Error al actualizar el registro: " + e.getMessage());
        }*/
    }

    private void salirPrograma() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            showError("Error al desconectarse: " + e.getMessage());
        }

        System.exit(0);
    }
	
	private void clearForm() {
		for (JTextField textField : textFields) {
			textField.setText("");
		}
		tablaItems.clearSelection();
	}
	
	private static void showError(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void loadData() {
		String tableName = selectedTable;
		List<String> columnNames = getTableColumnNames(tableName);
		tableModel = new DefaultTableModel();
		
		for (int i = 0; i < columnNames.size(); i++) {
            tableModel.addColumn(columnNames.get(i));
        }
		
		try {
			String query = "SELECT * FROM " + tableName;
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				Object[] row = new Object[columnNames.size()];
				for (int i = 0; i < columnNames.size(); i++) {
					row[i] = resultSet.getObject(columnNames.get(i));
				}
				tableModel.addRow(row);
			}
			tablaItems.setModel(tableModel);
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	private List<String> getTableColumnNames(String tableName) {
		List<String> columnNames = new ArrayList<>();
	    try {
	    	ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
	        for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnLabel(i));
            }
	    } catch (SQLException e) {
	    	showError("Error al obtener registros: " + e.getMessage());
	    }
	    return columnNames;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					TableProgram cargoProgram = new TableProgram("equipo");
					cargoProgram.createAndShowGUI();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					 showError("Error al conectarse: " + e.getMessage());
				}
			}
		});
	}
}


