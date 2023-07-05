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
    private JTable tablaEstadoRegistro;
    private String selectedTable;
    DatabaseMetaData metaData;

    public TableProgram() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/casaapuestas", "root", "admin");
        metaData = connection.getMetaData();
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

        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i);
            JLabel label = new JLabel(columnName + ":");
            panel.add(label, constraints);

            constraints.gridx = 1;
            JTextField textField = new JTextField(10);
            textFields[i] = textField;
            panel.add(textField, constraints);

            constraints.gridy++;
            constraints.gridx = 0;
        }

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new FlowLayout());

        panel.setBorder(createTitledBorder("Tabla estados de registros"));

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

        	JButton activeButton = new JButton("Activar");
        	activeButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	activarRegistro();
        	}
        	});
        	panel2.add(activeButton);

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
        	    e.printStackTrace();
        	}
        	}

        	private void modificarRegistro() {
        	int selectedRow = tablaEstadoRegistro.getSelectedRow();
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

        	String idColumnName = columnNames.get(0);
        	String idValue = values.get(0);

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
        	    e.printStackTrace();
        	}
        	}

        	private void eliminarRegistro() {
        	int selectedRow = tablaEstadoRegistro.getSelectedRow();
        	if (selectedRow == -1) {
        	JOptionPane.showMessageDialog(null, "Selecciona un registro para eliminar.");
        	return;
        	}
        	String tableName = selectedTable;
        	List<String> columnNames = getTableColumnNames(tableName);
        	String idColumnName = columnNames.get(0);
        	String idValue = (String) tablaEstadoRegistro.getValueAt(selectedRow, 0);

        	try {
        	    String query = "DELETE FROM " + tableName + " WHERE " + idColumnName + "='" + idValue + "'";
        	    statement.executeUpdate(query);
        	    loadData();
        	    clearForm();
        	} catch (SQLException e) {
        	    e.printStackTrace();
        	}
        	}

        	private void cancelarRegistro() {
        	clearForm();
        	}

        	private void inactivarRegistro() {
        	int selectedRow = tablaEstadoRegistro.getSelectedRow();
        	if (selectedRow == -1) {
        	JOptionPane.showMessageDialog(null, "Selecciona un registro para inactivar.");
        	return;
        	}
        	String tableName = selectedTable;
        	List<String> columnNames = getTableColumnNames(tableName);
        	String idColumnName = columnNames.get(0);
        	String idValue = (String) tablaEstadoRegistro.getValueAt(selectedRow, 0);

        	try {
        	    String query = "UPDATE " + tableName + " SET estado='inactivo' WHERE " + idColumnName + "='" + idValue + "'";
        	    statement.executeUpdate(query);
        	    loadData();
        	    clearForm();
        	} catch (SQLException e) {
        	    e.printStackTrace();
        	}
        	}

        	private void activarRegistro() {
        	int selectedRow = tablaEstadoRegistro.getSelectedRow();
        	if (selectedRow == -1) {
        	JOptionPane.showMessageDialog(null, "Selecciona un registro para activar.");
        	return;
        	}
        	String tableName = selectedTable;
        	List<String> columnNames = getTableColumnNames(tableName);
        	String idColumnName = columnNames.get(0);
        	String idValue = (String) tablaEstadoRegistro.getValueAt(selectedRow, 0);

        	try {
        	    String query = "UPDATE " + tableName + " SET estado='activo' WHERE " + idColumnName + "='" + idValue + "'";
        	    statement.executeUpdate(query);
        	    loadData();
        	    clearForm();
        	} catch (SQLException e) {
        	    e.printStackTrace();
        	}
        	}
        	private void clearForm() {
        	    for (JTextField textField : textFields) {
        	        textField.setText("");
        	    }
        	    tablaEstadoRegistro.clearSelection();
        	}

        	private void loadData() {
        	    String tableName = selectedTable;
        	    List<String> columnNames = getTableColumnNames(tableName);
        	    DefaultTableModel model = (DefaultTableModel) tablaEstadoRegistro.getModel();
        	    model.setRowCount(0);

        	    try {
        	        String query = "SELECT * FROM " + tableName;
        	        ResultSet resultSet = statement.executeQuery(query);
        	        while (resultSet.next()) {
        	            Object[] row = new Object[columnNames.size()];
        	            for (int i = 0; i < columnNames.size(); i++) {
        	                row[i] = resultSet.getObject(columnNames.get(i));
        	            }
        	            model.addRow(row);
        	        }
        	        resultSet.close();
        	    } catch (SQLException e) {
        	        e.printStackTrace();
        	    }
        	}

        	private List<String> getTableColumnNames(String tableName) {
        	    List<String> columnNames = new ArrayList<>();
        	    try {
        	        ResultSet resultSet = metaData.getColumns(null, null, tableName, null);
        	        while (resultSet.next()) {
        	            String columnName = resultSet.getString("COLUMN_NAME");
        	            columnNames.add(columnName);
        	        }
        	        resultSet.close();
        	    } catch (SQLException e) {
        	        e.printStackTrace();
        	    }
        	    return columnNames;
        	}
        	private void initializeDatabase() {
        	    try {
        	        Class.forName("com.mysql.jdbc.Driver");
        	        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/casaapuestas", "root", "admin");
        	        statement = connection.createStatement();
        	        metaData = connection.getMetaData();
        	    } catch (ClassNotFoundException | SQLException e) {
        	        e.printStackTrace();
        	    }
        	}

        	public static void main(String[] args) {
        	    SwingUtilities.invokeLater(new Runnable() {
        	        public void run() {
        	            try {
        	            	TableProgram cargoProgram = new TableProgram();
        	                cargoProgram.createAndShowGUI();
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        	        }
        	    });
        	}
}


