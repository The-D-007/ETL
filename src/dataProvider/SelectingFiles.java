package dataProvider;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class SelectingFiles extends JFrame implements ActionListener {

    JFrame frame;
    JButton openButton;
    File selectedFile;
    JLabel fileName, openFile, data, dataNumber;
    JTextField fileNameText;

    public SelectingFiles() {
        frame = new JFrame("Choose a file");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fileName = new JLabel("Give table name :");
        fileName.setFont(new Font("Raleway", Font.BOLD, 20));
        fileName.setForeground(Color.WHITE);
        fileName.setBounds(150, 130, 250, 30);
        add(fileName);

        fileNameText = new JTextField(20);
        fileNameText.setFont(new Font("Raleway", Font.BOLD, 20));
        fileNameText.setBounds(400, 130, 250, 30);
        add(fileNameText);

        openFile = new JLabel("Choose File :");
        openFile.setFont(new Font("Raleway", Font.BOLD, 20));
        openFile.setForeground(Color.WHITE);
        openFile.setBounds(150, 170, 250, 30);
        add(openFile);

        openButton = new JButton("File");
        openButton.setFont(new Font("Arial", Font.BOLD, 14));
        openButton.setBackground(Color.BLACK);
        openButton.setForeground(Color.WHITE);
        openButton.setBounds(400, 170, 250, 30);
        openButton.addActionListener(this);
        add(openButton);

        

        setLayout(null);
        setSize(850, 480);
        setLocation(450, 200);
        getContentPane().setBackground(Color.BLACK);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String tableName = fileNameText.getText();

        if (containsOnlyLetters(tableName) == true) {

            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

            int result = fileChooser.showOpenDialog(frame);

            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                System.out.println("Selected File: " + selectedFile.getAbsolutePath());
            }

            new ProvidingData(selectedFile, tableName);
            setVisible(false);
            
        } else {
            JOptionPane.showMessageDialog(null, "Please give only letters to table name.");
        }
    }

    public boolean containsOnlyLetters(String input) {
        return input.matches("^[a-zA-Z]+$");
    }

    public static void main(String[] args) {
        new SelectingFiles();
    }

}
