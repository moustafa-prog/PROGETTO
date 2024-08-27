
package Dottore_Occhi;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Dottore.ImagePanel;
import Dottore_Denti.I_DottoreDenti;

import Dottore_Denti.PazienteDenti;

import Dottore_Denti.ServicePanelDenti;
import Dottore_Denti.Successivi_Denti;

@SuppressWarnings("serial")
public class InterfacciaPrincipaleOcchi extends JFrame implements ActionListener {
	 
    JMenuBar Bar1  = new JMenuBar();
    JMenuItem Edit1 =  new JMenuItem("RICEVIMENTI");
    JMenu Edit = new JMenu("EDIT");
    JMenu File = new JMenu("FILE");
    JMenu Opizione = new JMenu("OPIZIONE");
    JMenu Aiutarsi = new JMenu("AIUTO");
    JMenu Account = new JMenu("ACCOUNTS");
    JLabel Dottore =new JLabel("DOTTORE");
    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel(); 
    ImagePanel panel3;  // Cambiato in ImagePanel
    JButton num1 = new JButton("RICEVIMENTI");		//الاستقبال
    JButton num2 = new JButton("SUCCESSIVO");		//التالي
    JButton num3 = new JButton("SERVIZI");			//الخدمات
    JButton num4 = new JButton("I DOTTORI");		//الاطباء
    JButton num9 = new JButton("ASSISTENTE");		//مساعده
    JButton num10 = new JButton(" ORARIO");			//المستخدمين
    JButton num11 = new JButton("I PAZIENTI");		//المرضي
    JButton num12 = new JButton("USTA");	//خروج
		
    public InterfacciaPrincipaleOcchi() {
        this.setTitle("UTENTE");
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(1006, 900);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.add(Bar1);
        this.add(panel1);
        this.add(panel2);
       panel3 = new ImagePanel("C:\\Users\\peschiera721\\OneDrive\\Immagini\\Catture di schermata\\Screenshot 2024-08-08 142007.png");
        this.add(panel3);  

        // Aggiungi ActionListener ai bottoni
        num1.addActionListener(this);
        num2.addActionListener(this);
        num3.addActionListener(this);
        num4.addActionListener(this);
        num11.addActionListener(this);
        num10.addActionListener(this);
        num12.addActionListener(this);

        // Dimensioni dei pannelli e bar
        Bar1.setBounds(0,0,1000,20);
        panel1.setBounds(0, 20,1000,75);
        panel2.setBounds(800, 30,200,900);
        panel3.setBounds(0, 95,800,900);
        panel3.setLayout(new BorderLayout());

        // Colore di background
        panel1.setBackground(Color.WHITE);
     	panel2.setBackground(Color.decode("#89CFF0"));
     			num1.setBackground(Color.WHITE);
     				num2.setBackground(Color.WHITE);
     					num3.setBackground(Color.WHITE);
     						num4.setBackground(Color.WHITE); 
     							num9.setBackground(Color.WHITE);
     								num10.setBackground(Color.WHITE);
     									num11.setBackground(Color.WHITE);
     										num12.setBackground(Color.WHITE);
        
        // Dimensioni di font
        Dottore.setFont(new Font("ALGERIAN", Font.BOLD, 50));
        num1.setFont(new Font("Congenial black", Font.BOLD, 19));
        num2.setFont(new Font("Congenial black", Font.BOLD, 18));
        num3.setFont(new Font("Congenial black", Font.BOLD, 20));
        num4.setFont(new Font("Congenial black", Font.BOLD, 20));
        num9.setFont(new Font("Congenial black", Font.BOLD, 18));
        num10.setFont(new Font("Congenial black", Font.BOLD, 20));
        num11.setFont(new Font("Congenial black", Font.BOLD, 20));
        num12.setFont(new Font("Congenial black", Font.BOLD, 20));

        // Aggiungi i componenti al menu e ai pannelli
        Edit.add(Edit1);
        Bar1.add(Edit);
        Bar1.add(File);
        Bar1.add(Opizione);
        Bar1.add(Aiutarsi);
        panel1.add(Dottore);
        
        // Imposta layout e aggiungi i bottoni
        panel2.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 60, 10);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 1;
        panel2.add(num1, gbc);
        gbc.gridy = 2;
        panel2.add(num2, gbc);
        gbc.gridy = 3;
        panel2.add(num3, gbc);
        gbc.gridy = 4;
        panel2.add(num4, gbc);
        gbc.gridy = 9;
        panel2.add(num9, gbc);
        gbc.gridy = 10;
        panel2.add(num10, gbc);
        gbc.gridy = 11;
        panel2.add(num11, gbc);
        gbc.gridy = 12;
        panel2.add(num12, gbc);
    }

    public JPanel getpanel3() {
        return panel3;
    }

    public static void main(String[] args) {
        new InterfacciaPrincipaleOcchi().setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == num1) {
            Recivimenti_Occhi ricivimenti = new Recivimenti_Occhi(null);
            panel3.removeAll();
            panel3.revalidate();
            panel3.repaint();
            panel3.add(ricivimenti.getPanelA(), BorderLayout.CENTER);
        } else if (e.getSource() == num2) {
            Successivi_Denti successivi_Denti = new Successivi_Denti();
            panel3.removeAll();
            panel3.revalidate();
            panel3.repaint();
            panel3.add(successivi_Denti.getPanelA(), BorderLayout.CENTER);
        } else if (e.getSource() == num3) {
            ServicePanelDenti servicePanelDenti = new ServicePanelDenti();
            panel3.removeAll();
            panel3.revalidate();
            panel3.repaint();
            panel3.add(servicePanelDenti.getPanelA(), BorderLayout.CENTER);
        } else if (e.getSource() == num4) {
            I_DottoreDenti i_dottore = new I_DottoreDenti();
            panel3.removeAll();
            panel3.revalidate();
            panel3.repaint();
            panel3.add(i_dottore.getPanelA(), BorderLayout.CENTER);
            }else if (e.getSource() == num11) {
                PazienteDenti pazienteDenti = new PazienteDenti();
                panel3.removeAll();
                panel3.revalidate();
                panel3.repaint();
                panel3.add(pazienteDenti.getPanelA(), BorderLayout.CENTER);
            }else if(e.getSource() == num12) {
                int c = JOptionPane.showConfirmDialog(null, "VOUI CHIUDERE L'APP", "SCELTA",JOptionPane.YES_NO_OPTION);
                if(c == 0) {
                    System.exit(0);
                }
            }
            else if (e.getSource() == num10) {
                	Orario_Dottore_Occhi Occhi = new Orario_Dottore_Occhi();
                    panel3.removeAll();
                    panel3.revalidate();
                    panel3.repaint();
                    panel3.add(Occhi.getPanelA(), BorderLayout.CENTER);
                }
        }
    }
