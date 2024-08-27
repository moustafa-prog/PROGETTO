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
public class PazienteOcchi extends JFrame {
    private static final String INSERT_SQL = "INSERT INTO paziente_occhi(COGNOME, NOME, DATA_DI_NASCITA, CODICE_FISCALE, NOME_DOTTORE) VALUES (?, ?, ?, ?, ?)";
    JTable Tabel;
    DefaultTableModel tableModel;
    JPanel PanelA = new JPanel(null);
    JButton Button_add = new JButton("+");
    JButton Button_cancl = new JButton("DELETE");
    JButton Button_ADD = new JButton("ADD");

    public PazienteOcchi() {
        this.setTitle("I DOTTORE");
        this.setSize(800, 770);
        this.add(PanelA);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        String[] columnNames = {"COGNOME", "NOME", "DATA_DI_NASCITA", "CODICE_FISCALE", "NOME_DOTTORE"};
        tableModel = new DefaultTableModel(columnNames, 0);
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
                tableModel.addRow(new String[]{"", "", "", "", ""});
            }
        });
        
        Button_cancl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = Tabel.getSelectedRows();
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    tableModel.removeRow(selectedRows[i]);
                }
            }
        });
        
        Button_ADD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int lastRowIndex = tableModel.getRowCount() - 1;
                if (lastRowIndex >= 0) {
                    String COGNOME = (String) tableModel.getValueAt(lastRowIndex, 0);
                    String NOME = (String) tableModel.getValueAt(lastRowIndex, 1);
                    String DATA_DI_NASCITA = (String) tableModel.getValueAt(lastRowIndex, 2);
                    String CODICE_FISCALE = (String) tableModel.getValueAt(lastRowIndex, 3);
                    String NOME_DOTTORE = (String) tableModel.getValueAt(lastRowIndex, 4);

                    saveToDatabase(COGNOME, NOME, DATA_DI_NASCITA, CODICE_FISCALE, NOME_DOTTORE);
                    JOptionPane.showMessageDialog(null, "GIA REGISTRATO", "Successo", JOptionPane.YES_OPTION);
                } else {
                    JOptionPane.showMessageDialog(null, "Nessuna riga da salvare", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void saveToDatabase(String cognome, String nome, String data_di_nascita, String codice_fiscale, String nome_dottore) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/progetto", "root", "Moustafa2001");
            pstmt = conn.prepareStatement(INSERT_SQL);
            pstmt.setString(1, cognome);
            pstmt.setString(2, nome);
            pstmt.setString(3, data_di_nascita);
            pstmt.setString(4, codice_fiscale);
            pstmt.setString(5, nome_dottore);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Record aggiunto con successo.");
            } else {
                System.out.println("Impossibile aggiungere il record.");
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
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/progetto", "root", "Moustafa2001");
            String query = "SELECT COGNOME, NOME, DATA_DI_NASCITA, CODICE_FISCALE, NOME_DOTTORE FROM paziente_occhi";
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();

            tableModel.setRowCount(0);

            while (rs.next()) {
                String cognome = rs.getString("COGNOME");
                String nome = rs.getString("NOME");
                String data_di_nascita = rs.getString("DATA_DI_NASCITA");
                String codiceFiscale = rs.getString("CODICE_FISCALE");
                String nomeDottore = rs.getString("NOME_DOTTORE");

                tableModel.addRow(new String[]{cognome, nome, data_di_nascita, codiceFiscale, nomeDottore});
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
        JFrame frame = new JFrame("I_Paziente_Occhi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new PazienteOcchi());
        frame.setSize(800, 700);
        frame.setVisible(true);
    }

    public JPanel getPanelA() {
        return PanelA;
    }

}



