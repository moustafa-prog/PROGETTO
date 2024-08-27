

package Dottore_Denti;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class ServicePanelDenti extends JFrame {
    private static final String INSERT_SQL = "INSERT INTO servicepanel_denti (NOME_DOTTORE, PREZZO, NOME_DELLA_MALATTIA, TRATTAMENTO, CODICE_FISCAILE) VALUES (?, ?, ?, ?, ?)";
    JPanel PanelA = new JPanel(null);
    JPanel Panela = new JPanel(null);
    JLabel Label = new JLabel("NOME_DOTTORE");
    JLabel Label2 = new JLabel("PREZZO DI RILEVAMENTO");
    JLabel ServicePanelform = new JLabel("servizi");
    JTextField label1 = new JTextField();
    JTextField label2 = new JTextField();
    JTable Tabel;
    DefaultTableModel tableModel;
    JButton Button1 = new JButton("DELETE");
    JButton Button2 = new JButton("ADD");
    JButton Button4 = new JButton("+");
    JButton Button5 = new JButton("");
    private int lastAddedRow = -1;

    public ServicePanelDenti() {
        this.setTitle("ServicePanel");
        this.setSize(800, 770);
        this.add(PanelA);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        PanelA.setBounds(0, 0, 811, 770);
        Panela.setBounds(0, 0, 811, 150);

        Label.setBounds(0, 50, 190, 35);
        label1.setBounds(170, 55, 120, 25);
        Label2.setBounds(480, 50, 190, 35);
        label2.setBounds(670, 55, 120, 25);
        ServicePanelform.setBounds(250, 160, 600, 50);
        Button1.setBounds(50, 600, 130, 35);
        Button2.setBounds(650, 600, 100, 35);
        Button4.setBounds(0, 300, 50, 35);
        Button5.setBounds(0, 350, 50, 35);

        String[] columnNames = {"NOME_DELLA_MALATTIA", "TRATTAMENTO ",  "CODICE_FISCAILE"};
        String[][] data = {};

        tableModel = new DefaultTableModel(data, columnNames);
        Tabel = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(Tabel);
        scrollPane.setBounds(50, 230, 700, 300);

        PanelA.setBackground(Color.decode("#89CFF0"));
        Panela.setBackground(Color.decode("#89CFF0"));
        Button1.setBackground(Color.WHITE);
        Button2.setBackground(Color.WHITE);

        ServicePanelform.setFont(new Font("ALGERIAN", Font.BOLD, 50));
        Button1.setFont(new Font("Congenial black", Font.BOLD, 20));
        Button2.setFont(new Font("Congenial black", Font.BOLD, 20));
        Label.setFont(new Font("Congenial black", Font.BOLD, 15));
        Label2.setFont(new Font("Congenial black", Font.BOLD, 15));
        ImageIcon icon1 = new ImageIcon("C:\\Users\\peschiera721\\OneDrive\\Immagini\\Catture di schermata\\Screenshot 2024-07-20 132009.png");

        PanelA.add(Panela);
        Panela.add(Label);
        Panela.add(label1);
        Panela.add(Label2);
        Panela.add(label2);
        PanelA.add(ServicePanelform);
        PanelA.add(scrollPane);
        PanelA.add(Button1);
        PanelA.add(Button2);
        PanelA.add(Button4);
        PanelA.add(Button5);
        Button5.setIcon(icon1);

        Button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);
                label1.setText("");
                label2.setText("");
                lastAddedRow = -1;
            }
        });

        Button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check for empty fields
                if (label1.getText().isEmpty() || label2.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "NECESSARIO COMPLETARE LE INFORMAZIONE PER lA NOME DELLA CLINICA E PREZZO", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Get data from text fields
                String nomeDottore = label1.getText();
                String price = label2.getText();

                // Get data from the table
                int rowCount = tableModel.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    String nome_delle_malattia = (String) tableModel.getValueAt(i, 0);
                    String trattamento = (String) tableModel.getValueAt(i, 1);
                    String codice_fiscale = (String) tableModel.getValueAt(i, 2);

                    // Call the method to save data to the database
                    saveToDatabase(nomeDottore, price, nome_delle_malattia, trattamento,codice_fiscale);
                }

                JOptionPane.showMessageDialog(null, "TUTTO APPOSTO", "Successo", JOptionPane.YES_OPTION);
            }
        });

       

        Button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.addRow(new String[]{"", "", "", ""});
            }
        });

        Button5.addActionListener(new ActionListener() {
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
    }

   
    private void saveToDatabase(String nomeDottore, String prezzo, String nome_delle_malattia, String trattamento, String codice_fiscale) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/progetto", "root", "Moustafa2001");

            // Prepare SQL statement
            pstmt = conn.prepareStatement(INSERT_SQL);
            pstmt.setString(1, nomeDottore);
            pstmt.setString(2, prezzo);
            pstmt.setString(3, nome_delle_malattia);
            pstmt.setString(4, trattamento);
            pstmt.setString(5, codice_fiscale);
           

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
    

    public JPanel getPanelA() {
        return PanelA;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ServicePanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ServicePanelDenti());
        frame.setSize(800, 700);
        frame.setVisible(true);
    }
}
