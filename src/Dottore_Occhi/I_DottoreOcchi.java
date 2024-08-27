
package Dottore_Occhi;

import java.awt.Color;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class I_DottoreOcchi extends JFrame{
	private static final String INSERT_SQL="INSERT INTO dottore_occhi(COGNOME, NOME, ID , CODICE_FISCALE,NOME_CLINICA) VALUES (?, ?, ?, ?, ?)";
	JTable Tabel;
    DefaultTableModel tableModel;
    JPanel PanelA = new JPanel(null);
    JPanel Panela = new JPanel(null);
    JButton Button_add = new JButton("+");
    JButton Button_cancl = new JButton("DELETE");
    JButton Button_ADD = new JButton("ADD");
    private int lastAddedRow = -1;
	public I_DottoreOcchi() {
		this.setTitle("I DOTTORE");
        this.setSize(800, 770);
        this.add(PanelA);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        String[] columnNames = {"COGNOME", "NOME", "ID ","CODICE_FISCALE","NOME_CLINICA"};
        String[][] data = {{"", "","","",""}};
        tableModel = new DefaultTableModel(data, columnNames);
        Tabel = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(Tabel);
        scrollPane.setBounds(0, 20, 800, 670);
        Button_add.setBounds(10, 700, 150, 40);
        Button_cancl.setBounds(650, 700, 150, 40);
        Button_ADD.setBounds(325, 700, 150, 40);
        PanelA.add(scrollPane);
    	PanelA.setBackground(Color.decode("#89CFF0"));
        PanelA.add(Button_add);
        PanelA.add(Button_cancl);
        PanelA.add(Button_ADD);
        Button_add.setFont(new Font("Congenial black", Font.BOLD, 20));
        Button_ADD.setFont(new Font("Congenial black", Font.BOLD, 20));
        Button_cancl.setFont(new Font("Congenial black", Font.BOLD, 20));
        Button_add.setBackground(Color.WHITE);
        Button_ADD.setBackground(Color.WHITE);
        Button_cancl.setBackground(Color.WHITE);
        loadDataFromDatabase();
        Button_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.addRow(new String[]{"", "", "", "",""});
            }
        });
        Button_cancl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = Tabel.getSelectedRows();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    tableModel.removeRow(selectedRows[i]);
                }

                if (lastAddedRow >= 0 && lastAddedRow < tableModel.getRowCount()) {
                    tableModel.removeRow(lastAddedRow);
                    lastAddedRow = -1;
                }

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    tableModel.setValueAt(i + 1, i, 0);
                }
            }
        });
        Button_ADD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
         
         

                // Get data from the table
                int rowCount = tableModel.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    String COGNOME = (String) tableModel.getValueAt(i, 0);
                    String NOME = (String) tableModel.getValueAt(i, 1);
                    String ID = (String) tableModel.getValueAt(i, 2);
                    String CODICE_FISCALE	 = (String) tableModel.getValueAt(i, 3);
                    String NOME_CLINICA = (String) tableModel.getValueAt(i, 3);

                    // Call the method to save data to the database
                    saveToDatabase(COGNOME, NOME, ID,  CODICE_FISCALE, NOME_CLINICA);
                }
                JOptionPane.showMessageDialog(null, "GIA REGISTRATO", "Successo", JOptionPane.YES_OPTION);
            }
        });	
        	
       
}

	 private void saveToDatabase(String cognome, String nome, String id,  String codice_fiscale, String nome_clinica) {
         Connection conn = null;
         PreparedStatement pstmt = null;
         try {
             // Load JDBC driver
             Class.forName("com.mysql.cj.jdbc.Driver");

             // Connect to the database
             conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/progetto", "root", "Moustafa2001");

             // Prepare SQL statement
             pstmt = conn.prepareStatement(INSERT_SQL);
             pstmt.setString(1, cognome);
             pstmt.setString(2, nome);
             pstmt.setString(3, id);
             pstmt.setString(4, codice_fiscale);
             pstmt.setString(5, nome_clinica);

             // Execute the query
             int affectedRows = pstmt.executeUpdate();
             if (affectedRows > 0) {
                 System.out.println("Record added successfully.");
             } else {
                 System.out.println("Failed to add the record.");
             }
         } catch (SQLException | ClassNotFoundException ex) {
             ex.printStackTrace();
             JOptionPane.showMessageDialog(null, "Errore durante l'inserimento dei dati: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
         } finally {
             try {
                 if (pstmt != null) pstmt.close();
                 if (conn != null) conn.close();
             } catch (SQLException ex) {
                 ex.printStackTrace();
             }
             }
         }
        
		private void loadDataFromDatabase() {
        	    Connection conn = null;
        	    PreparedStatement pstmt = null;
        	    ResultSet rs = null;

        	    try {
        	        // Carica il driver JDBC
        	        Class.forName("com.mysql.cj.jdbc.Driver");

        	        // Connessione al database
        	        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/progetto", "root", "Moustafa2001");

        	        // Query per recuperare i dati
        	        String query = "SELECT COGNOME, NOME, ID, CODICE_FISCALE, NOME_CLINICA FROM dottore_occhi";
        	        pstmt = conn.prepareStatement(query);
        	        rs = pstmt.executeQuery();

        	        // Pulire la tabella prima di aggiungere i dati
        	        tableModel.setRowCount(0);

        	        // Aggiungi i dati al modello della tabella
        	        while (rs.next()) {
        	            String cognome = rs.getString("COGNOME");
        	            String nome = rs.getString("NOME");
        	            String id = rs.getString("ID");
        	            String codiceFiscale = rs.getString("CODICE_FISCALE");
        	            String nomeClinica = rs.getString("NOME_CLINICA");

        	            tableModel.addRow(new String[]{cognome, nome, id, codiceFiscale, nomeClinica});
        	        }
        	    } catch (SQLException | ClassNotFoundException ex) {
        	        ex.printStackTrace();
        	        JOptionPane.showMessageDialog(null, "Errore durante il recupero dei dati: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        	    } finally {
        	        try {
        	            if (rs != null) rs.close();
        	            if (pstmt != null) pstmt.close();
        	            if (conn != null) conn.close();
        	        } catch (SQLException ex) {
        	            ex.printStackTrace();
        	        }
        	    }
        	}
	 
     



public static void main(String[] args) {
	  JFrame frame = new JFrame("I_Dottore");
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.add(new I_DottoreOcchi());
     frame.setSize(800, 700);
     frame.setVisible(true);

}



public JPanel getPanelA() {
     return PanelA;
 }


}

