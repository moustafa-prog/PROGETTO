
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
public class Recivimenti_Denti extends JFrame implements ActionListener {
    private static final String INSERT_SQL = "INSERT INTO recivementi_denti (COGNOME, NOME, DATA, ORE, CODICE_FISCALE) VALUES (?, ?, ?, ?, ?)";
    JTable Tabel;

    private static String tipoUtente;
    DefaultTableModel tableModel;
    JPanel PanelA = new JPanel(null);
    JPanel Panela = new JPanel(null);
    JButton Button_add = new JButton("+");
    JButton Button_cancl = new JButton("DELETE");
    public JButton Button_ADD = new JButton("ADD");

    @SuppressWarnings("static-access")
	public Recivimenti_Denti(String tipoUtente) {
        this.setTipoUtente(tipoUtente);
        this.setTitle("Recivimenti");
        this.setSize(800, 770);
        this.add(PanelA);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] columnNames = {"COGNOME", "NOME", "DATA", "ORE", "CODICE_FISCALE",};
        String[][] data = {{"", "", "", "", ""}};
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
        Button_ADD.addActionListener(this);

        Button_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.addRow(new String[]{"", "", "", "", ""});
            }
        });

        Button_cancl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = Tabel.getSelectedRows();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    String codiceFiscale = (String) tableModel.getValueAt(selectedRows[i], 4); // Colonna 4 per CODICE_FISCALE
                    deleteFromDatabase(codiceFiscale);
                    tableModel.removeRow(selectedRows[i]);
                }
            }
        });
    }

    private void deleteFromDatabase(String codiceFiscale) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Progetto", "root", "Moustafa2001");
            String DELETE_SQL = "DELETE FROM recivementi_denti WHERE CODICE_FISCALE = ?";
            pstmt = conn.prepareStatement(DELETE_SQL);
            pstmt.setString(1, codiceFiscale);
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
            // Ottieni l'ultima riga aggiunta
            int lastRowIndex = tableModel.getRowCount() - 1;
            String COGNOME = (String) tableModel.getValueAt(lastRowIndex, 0);
            String NOME = (String) tableModel.getValueAt(lastRowIndex, 1);
            String DATA = (String) tableModel.getValueAt(lastRowIndex, 2);
            String ORE = (String) tableModel.getValueAt(lastRowIndex, 3);
            String CODICE_FISCALE = (String) tableModel.getValueAt(lastRowIndex, 4);

            // Validazione dei dati
            if (COGNOME.isEmpty() || NOME.isEmpty() || DATA.isEmpty() || ORE.isEmpty() || CODICE_FISCALE.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Tutti i campi devono essere compilati!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                // Salva solo l'ultima riga
                saveToDatabase(COGNOME, NOME, DATA, ORE, CODICE_FISCALE);
                JOptionPane.showMessageDialog(null, "Dati aggiunti con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                
                // Ricarica i dati dal database
                loadDataFromDatabase();  
            }
        }
    }

    private void saveToDatabase(String cognome, String nome, String data, String ore, String codice_fiscale) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Progetto", "root", "Moustafa2001");

            pstmt = conn.prepareStatement(INSERT_SQL);
            pstmt.setString(1, cognome);
            pstmt.setString(2, nome);
            pstmt.setString(3, data);
            pstmt.setString(4, ore);
            pstmt.setString(5, codice_fiscale);
           

            // Debug: stampa la query che verrà eseguita
            System.out.println("Eseguendo query: " + pstmt);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Record aggiunto con successo.");
            } else {
                System.out.println("Inserimento fallito.");
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
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Progetto", "root", "Moustafa2001");
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT R.COGNOME, R.NOME, R.DATA, R.ORE, R.CODICE_FISCALE " +
                 "FROM progetto.recivementi_denti R " +
                 "JOIN user U ON R.Tipo_di_utente = U.Tipo_di_utente " +
                 "WHERE R.Tipo_di_utente = 'DOTTORE DENTI'")) {

            try (ResultSet rs = pstmt.executeQuery()) {
                tableModel.setRowCount(0); // Clear existing rows

                while (rs.next()) {
                    String cognome = rs.getString("COGNOME");
                    String nome = rs.getString("NOME");
                    String data = rs.getString("DATA");
                    String ore = rs.getString("ORE");
                    String codiceFiscale = rs.getString("CODICE_FISCALE");
              

                    // Debug: stampa i dati recuperati dal database
              
                    tableModel.addRow(new Object[]{cognome, nome, data, ore, codiceFiscale});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static String getTipoUtente() {
        return tipoUtente;
    }

    public static void setTipoUtente(String tipoUtente) {
        Recivimenti_Denti.tipoUtente = tipoUtente;
    }

	public JPanel getPanelA() {
		
		return PanelA;
	}
}
