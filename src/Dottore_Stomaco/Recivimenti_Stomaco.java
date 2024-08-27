package Dottore_Stomaco;

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
public class Recivimenti_Stomaco extends JFrame implements ActionListener {
	private static final String INSERT_SQL = "INSERT INTO recivementi_stomaco (COGNOME, NOME, DATA , ORE,CODICE_FISCALE,NOME_CLINICA) VALUES (?, ?, ?, ?, ?, ?)";
	JTable Tabel;

	private static String tipoUtente;
    DefaultTableModel tableModel;
    JPanel PanelA = new JPanel(null);
    JPanel Panela = new JPanel(null);
    JButton Button_add = new JButton("+");
    JButton Button_cancl = new JButton("DELETE");
    JButton Button_ADD = new JButton("ADD");
   
	public Recivimenti_Stomaco(String tipoUtente) {
	 
		this.setTipoUtente(tipoUtente);
		  this.setTitle("Recivimenti");
	        this.setSize(800, 770);
	        this.add(PanelA);
	        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	        
		 String[] columnNames = {"COGNOME", "NOME", "DATA ", "ORE","CODICE_FISCALE", "NOME CLINICA"};
	        String[][] data = {{"", "","","","",""}};
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
	        tableModel.fireTableDataChanged();
	        Button_ADD.addActionListener(this);

	        Button_add.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                tableModel.addRow(new String[]{"", "", "", ""});
	            }
	        });
	        Button_cancl.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                int[] selectedRows = Tabel.getSelectedRows();
	                
	                for (int i = selectedRows.length - 1; i >= 0; i--) {
	                    // Ottieni il codice fiscale della riga selezionata per cancellarlo dal database
	                    String codiceFiscale = (String) tableModel.getValueAt(selectedRows[i], 4); // Colonna 4 per CODICE_FISCALE
	                    
	                    // Chiama il metodo per eliminare la riga dal database
	                    deleteFromDatabase(codiceFiscale);
	                    
	                    // Rimuovi la riga dalla tabella
	                    tableModel.removeRow(selectedRows[i]);
	                }

	                // Aggiorna gli indici della tabella (se necessario)
	                for (int i = 0; i < tableModel.getRowCount(); i++) {
	                    tableModel.setValueAt(i + 1, i, 0);
	                }
	            }
	        });
	
	}
	

			private void deleteFromDatabase(String codiceFiscale) {
	            Connection conn = null;
	            PreparedStatement pstmt = null;

	            try {
	                // Carica il driver JDBC
	                Class.forName("com.mysql.cj.jdbc.Driver");

	                // Connessione al database
	                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/progetto", "root", "Moustafa2001");

	                // Query SQL per eliminare la riga dal database basata sul codice fiscale
	                String DELETE_SQL = "DELETE FROM recivementi_stomaco WHERE CODICE_FISCALE = ?";

	                // Prepara la query SQL
	                pstmt = conn.prepareStatement(DELETE_SQL);
	                pstmt.setString(1, codiceFiscale);

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
	      
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                if (e.getSource() == Button_ADD) {
	                    int rowCount = tableModel.getRowCount();
	                    boolean validData = true;
	                    for (int i = 0; i < rowCount; i++) {
	                        String COGNOME = (String) tableModel.getValueAt(i, 0);
	                        String NOME = (String) tableModel.getValueAt(i, 1);
	                        String DATA = (String) tableModel.getValueAt(i, 2);
	                        String ORE = (String) tableModel.getValueAt(i, 3);
	                        String CODICE_FISCALE = (String) tableModel.getValueAt(i, 4);
	                        String NOME_CLINICA = (String) tableModel.getValueAt(i, 5);

	                        {
	                            saveToDatabase(COGNOME, NOME, DATA, ORE, CODICE_FISCALE, NOME_CLINICA);
	                        }
	                    }
	                    if (validData) {
	                        JOptionPane.showMessageDialog(null, "Dati aggiunti con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
	                        loadDataFromDatabase(); // Ricarica i dati per riflettere le modifiche
	                    }
	                }
	            }

	
	     
			private void saveToDatabase(String cognome, String nome, String data, String ore, String codice_fiscale, String nome_clinica) {
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
	                pstmt.setString(3, data);
	                pstmt.setString(4, ore);
	                pstmt.setString(5, codice_fiscale);
	                pstmt.setString(6, nome_clinica);

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
				try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Progetto", "root", "Moustafa2001")) {
				    if (conn != null) {
				        System.out.println("Connessione stabilita correttamente");
				    } else {
				        System.out.println("Connessione fallita");
				    }
				} catch (SQLException ex) {
				    ex.printStackTrace();
				}

			    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Progetto", "root", "Moustafa2001");
			         PreparedStatement pstmt = conn.prepareStatement(
			            "SELECT COGNOME, NOME, DATA, ORE, CODICE_FISCALE, NOME_CLINICA FROM progetto.recivementi_stomaco")) {

			        try (ResultSet rs = pstmt.executeQuery()) {
			            tableModel.setRowCount(0); // Svuota le righe esistenti nel modello della tabella

			            while (rs.next()) {
			                String cognome = rs.getString("COGNOME");
			                String nome = rs.getString("NOME");
			                String data = rs.getString("DATA");
			                String ore = rs.getString("ORE");
			                String codiceFiscale = rs.getString("CODICE_FISCALE");
			                String nomeClinica = rs.getString("NOME_CLINICA");

			                tableModel.addRow(new String[]{cognome, nome, data, ore, codiceFiscale, nomeClinica});
			            }
			        }
			    } catch (SQLException ex) {
			        ex.printStackTrace();
			        JOptionPane.showMessageDialog(null, "Errore durante il recupero dei dati: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
			    }
			}

			public static void main(String[] args) {
			    String tipoUtente = "DOTTORE STOMACO";  // Set the type of doctor
			    Recivimenti_Stomaco recivimenti_Stomaco = new Recivimenti_Stomaco(tipoUtente);
			    JFrame frame = new JFrame("Recivimenti");
			    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    frame.add(recivimenti_Stomaco.getPanelA());
			    frame.setSize(800, 770);
			    frame.setVisible(true);
			}




	 public JPanel getPanelA() {
	        return PanelA;
	    }
	public static String getTipoUtente() {
		return tipoUtente;
	}
	@SuppressWarnings("static-access")
	public void setTipoUtente(String tipoUtente) {
		this.tipoUtente = tipoUtente;
	}
}
     