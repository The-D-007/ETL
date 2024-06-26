package dataProvider;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class TablesToInsert extends JFrame implements ActionListener {
    
    JLabel files;
    JTextField filesNumber;
    JButton okay;

    public TablesToInsert() {
        
        files = new JLabel("How many files you want to insert? ");
        files.setFont(new Font("Raleway", Font.BOLD, 20));
        files.setForeground(Color.WHITE);
        files.setBounds(100, 130, 350, 30);
        add(files);

        filesNumber = new JTextField(20);
        filesNumber.setFont(new Font("Raleway", Font.BOLD, 20));
        filesNumber.setBounds(470, 130, 150, 30);
        add(filesNumber);

        okay = new JButton("Okay");
        okay.setFont(new Font("Arial", Font.BOLD, 14));
        okay.setBackground(Color.BLACK);
        okay.setForeground(Color.WHITE);
        okay.setBounds(470, 185, 150, 30);
        okay.addActionListener(this);
        add(okay);

        
        setLayout(null);
        setSize(850, 480);
        setLocation(450, 200);
        getContentPane().setBackground(Color.BLACK);
        setVisible(true);

    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String fileNumber = filesNumber.getText();
            int i = 1;
            if (containsOnlyNumbers(fileNumber) == true) {
                int times = Integer.parseInt(fileNumber);
                while (i <= times) {
                    new SelectingFiles();
                    i++;
                }
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Enter only Integer.");
            }
        } catch (NumberFormatException E) {
            E.printStackTrace();
        }
    }
    public boolean containsOnlyNumbers(String input) {
        return input.matches("^[0-9]+$");
    }
       
    public static void main(String[] args) {
        new TablesToInsert();
    }
    
}