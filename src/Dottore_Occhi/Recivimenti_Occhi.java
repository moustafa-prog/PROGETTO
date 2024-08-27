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
public class Recivimenti_Occhi extends JFrame implements ActionListener {
    private static final String INSERT_SQL = "INSERT INTO recivementi_occhi (COGNOME, NOME, DATA , ORE, CODICE_FISCALE, NOME_CLINICA) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String DELETE_SQL = "DELETE FROM recivementi_occhi WHERE CODICE_FISCALE = ?";
    private static final String SELECT_SQL = "SELECT COGNOME, NOME, DATA, ORE, CODICE_FISCALE, NOME_CLINICA FROM recivementi_occhi";
    private static String tipoUtente;
    private JTable Tabel;
    private DefaultTableModel tableModel;
    private JPanel PanelA = new JPanel(null);
    private JButton Button_add = new JButton("+");
    private JButton Button_cancl = new JButton("DELETE");
    private JButton Button_ADD = new JButton("AGGIUNGE");

    public Recivimenti_Occhi(String tipoUtente) {
        this.setTitle("Recivimenti");
        this.setSize(800, 770);
        this.add(PanelA);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] columnNames = {"COGNOME", "NOME", "DATA", "ORE", "CODICE_FISCALE", "NOME CLINICA"};
        tableModel = new DefaultTableModel(null, columnNames);
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
                tableModel.addRow(new String[]{"", "", "", "", "", ""});
            }
        });

        Button_cancl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = Tabel.getSelectedRows();
                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Nessuna riga selezionata per l'eliminazione.", "Avviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    String codiceFiscale = (String) tableModel.getValueAt(selectedRows[i], 4);
                    deleteFromDatabase(codiceFiscale);
                    tableModel.removeRow(selectedRows[i]);
                }
            }
        });
    }

    private void deleteFromDatabase(String codiceFiscale) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Progetto", "root", "Moustafa2001");
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {

            pstmt.setString(1, codiceFiscale);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Record eliminato con successo dal database.");
            } else {
                System.out.println("Errore nell'eliminazione del record dal database.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'eliminazione dei dati: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
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
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Progetto", "root", "Moustafa2001");
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {

            pstmt.setString(1, cognome);
            pstmt.setString(2, nome);
            pstmt.setString(3, data);
            pstmt.setString(4, ore);
            pstmt.setString(5, codice_fiscale);
            pstmt.setString(6, nome_clinica);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Record aggiunto con successo.");
            } else {
                System.out.println("Impossibile aggiungere il record.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'inserimento dei dati: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDataFromDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Progetto", "root", "Moustafa2001");
             PreparedStatement pstmt = conn.prepareStatement(SELECT_SQL);
             ResultSet rs = pstmt.executeQuery()) {

            tableModel.setRowCount(0);
            while (rs.next()) {
                String cognome = rs.getString("COGNOME");
                String nome = rs.getString("NOME");
                String data = rs.getString("DATA");
                String ore = rs.getString("ORE");
                String codiceFiscale = rs.getString("CODICE_FISCALE");
                String nomeClinica = rs.getString("NOME_CLINICA");
                tableModel.addRow(new String[]{cognome, nome, data, ore, codiceFiscale, nomeClinica});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante il recupero dei dati: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        Recivimenti_Occhi recivimenti_Occhi = new Recivimenti_Occhi("DOTTORE OCCHI");
        recivimenti_Occhi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recivimenti_Occhi.setVisible(true);
    }

    public JPanel getPanelA() {
        return PanelA;
    }

    public static String getTipoUtente() {
        return tipoUtente;
    }

    public void setTipoUtente(String tipoUtente) {
        Recivimenti_Occhi.tipoUtente = tipoUtente;
    }
}