package Dottore_Denti;
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
public class Orario_Dottore_Denti extends JFrame {
	private static final String INSERT_SQL="INSERT INTO orario_dottore_denti(COGNOME, NOME, EMAIL , NUMERO_DI_TELEFONO, ORARIO) VALUES (?, ?, ?, ?, ?)";
	JTable Tabel;
    DefaultTableModel tableModel;
    JPanel PanelA = new JPanel(null);
    JPanel Panela = new JPanel(null);
    JButton Button_add = new JButton("+");
    JButton Button_cancl = new JButton("DELETE");
    JButton Button_ADD = new JButton("ADD");
    private int lastAddedRow = -1;
	public Orario_Dottore_Denti() {
		this.setTitle("I Paziente Denti");
        this.setSize(800, 770);
        this.add(PanelA);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        String[] columnNames = {"COGNOME", "NOME", "EMAIL ","NUMERO DI TELEFONO ","ORARIO"};
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
                    // Prendi l'email dalla riga selezionata per eliminarla dal database
                    String email = (String) tableModel.getValueAt(selectedRows[i], 2);

                    // Elimina dal database
                    deleteFromDatabase(email);

                    // Rimuovi la riga dalla tabella
                    tableModel.removeRow(selectedRows[i]);
                }

                if (lastAddedRow >= 0 && lastAddedRow < tableModel.getRowCount()) {
                    tableModel.removeRow(lastAddedRow);
                    lastAddedRow = -1;
                }

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    tableModel.setValueAt(i+1,i , 0);;
                }
            }
        });
	

        
        Button_ADD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Ottieni l'ultima riga della tabella
                    int lastRowIndex = tableModel.getRowCount() - 1;
                    String COGNOME = (String) tableModel.getValueAt(lastRowIndex, 0);
                    String NOME = (String) tableModel.getValueAt(lastRowIndex, 1);
                    String EMAIL = (String) tableModel.getValueAt(lastRowIndex, 2);
                    String NUMERO_DI_TELEFONO = (String) tableModel.getValueAt(lastRowIndex, 3);
                    String ORARIO = (String) tableModel.getValueAt(lastRowIndex, 4);

                    // Stampa i valori per debug
                    System.out.println("Valori inseriti: COGNOME: " + COGNOME + ", NOME: " + NOME + 
                                       ", EMAIL: " + EMAIL + ", NUMERO_DI_TELEFONO: " + NUMERO_DI_TELEFONO + 
                                       ", ORARIO: " + ORARIO);

                    // Verifica che nessuno dei campi sia vuoto
                    if (COGNOME.isEmpty() || NOME.isEmpty() || EMAIL.isEmpty() || NUMERO_DI_TELEFONO.isEmpty() || ORARIO.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Tutti i campi devono essere compilati!", "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Verifica specifica per il campo ORARIO
                    if (ORARIO == null || ORARIO.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Il campo ORARIO non può essere vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Prova a salvare i dati nel database
                    saveToDatabase(COGNOME, NOME, EMAIL, NUMERO_DI_TELEFONO, ORARIO);
                    JOptionPane.showMessageDialog(null, "Dati registrati con successo", "Successo", JOptionPane.INFORMATION_MESSAGE);

                    // Ricarica i dati nella tabella
                    loadDataFromDatabase();
                } catch (Exception ex) {
                    ex.printStackTrace(); // Stampa l'errore completo nella console
                    JOptionPane.showMessageDialog(null, "Errore durante l'inserimento dei dati: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
	}
	private void saveToDatabase(String cognome, String nome, String email, String numero_di_telefono, String orario) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/progetto", "root", "Moustafa2001");

	  
	        pstmt = conn.prepareStatement(INSERT_SQL);
	        pstmt.setString(1, cognome);
	        pstmt.setString(2, nome);
	        pstmt.setString(3, email);
	        pstmt.setString(4, numero_di_telefono);
	        pstmt.setString(5, orario); // Questo è il quinto parametro

	        // Esegui la query
	        int affectedRows = pstmt.executeUpdate();
	        if (affectedRows > 0) {
	            System.out.println("Record aggiunto con successo.");
	        } else {
	            System.out.println("Inserimento del record fallito.");
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
        	        String query = "SELECT COGNOME, NOME, EMAIL , NUMERO_DI_TELEFONO, ORARIO FROM orario_dottore_denti";
        	        pstmt = conn.prepareStatement(query);
        	        rs = pstmt.executeQuery();

        	        // Pulire la tabella prima di aggiungere i dati
        	        tableModel.setRowCount(0);

        	        
        	        // Aggiungi i dati al modello della tabella
        	        while (rs.next()) {
        	            String cognome = rs.getString("COGNOME");
        	            String nome = rs.getString("NOME");
        	            String email= rs.getString("EMAIL");
        	            String numero_di_telefono = rs.getString("NUMERO_DI_TELEFONO");
        	            String orario = rs.getString("ORARIO");

        	            tableModel.addRow(new String[]{cognome, nome, email, numero_di_telefono, orario});
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
         
         private void deleteFromDatabase(String email) {
        	    Connection conn = null;
        	    PreparedStatement pstmt = null;

        	    try {
        	        // Carica il driver JDBC
        	        Class.forName("com.mysql.cj.jdbc.Driver");

        	        // Connessione al database
        	        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/progetto", "root", "Moustafa2001");

        	        // Query SQL per eliminare la riga dal database basata sull'email
        	        String DELETE_SQL = "DELETE FROM orario_dottore_denti WHERE EMAIL = ?";

        	        // Prepara la query SQL
        	        pstmt = conn.prepareStatement(DELETE_SQL);
        	        pstmt.setString(1, email); 

        	        // Esegui la query
        	        int affectedRows = pstmt.executeUpdate();
        	        if (affectedRows > 0) {
        	            System.out.println("Record eliminato con successo dal database.");
        	        } else {
        	            System.out.println("Errore nell'eliminazione del record dal database.");
        	        }

        	    } catch (SQLException | ClassNotFoundException ex) {
        	        ex.printStackTrace();
        	        JOptionPane.showMessageDialog(null, "Errore durante l'eliminazione dei dati: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        	    } finally {
        	        try {
        	            if (pstmt != null) pstmt.close();
        	            if (conn != null) conn.close();
        	        } catch (SQLException ex) {
        	            ex.printStackTrace();
        	        }
        	    }
        	}



public static void main(String[] args) {
	  JFrame frame = new JFrame("Orario_Dottore_Denti");
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.add(new Orario_Dottore_Denti());
     frame.setSize(800, 700);
     frame.setVisible(true);
   
}



public JPanel getPanelA() {
     return PanelA;
 }
}



