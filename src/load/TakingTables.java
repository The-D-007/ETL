package load;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import dataProvider.Connect;

public class TakingTables extends JFrame implements ActionListener {

    JLabel label, firstTable, secondTable;
    JTextField firsTextField, secondTextField;
    JButton okay;

    public TakingTables() {


        label = new JLabel("Enter table names to get user subscription data");
        label.setFont(new Font("Raleway", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
        label.setBounds(180, 60, 500, 30);
        add(label);

        firstTable = new JLabel("Give first table name :");
        firstTable.setFont(new Font("Raleway", Font.BOLD, 20));
        firstTable.setForeground(Color.WHITE);
        firstTable.setBounds(150, 130, 250, 30);
        add(firstTable);

        firsTextField = new JTextField(20);
        firsTextField.setFont(new Font("Raleway", Font.BOLD, 20));
        firsTextField.setBounds(420, 130, 250, 30);
        add(firsTextField);

        secondTable = new JLabel("Give second table name :");
        secondTable.setFont(new Font("Raleway", Font.BOLD, 20));
        secondTable.setForeground(Color.WHITE);
        secondTable.setBounds(150, 190, 250, 30);
        add(secondTable);

        secondTextField = new JTextField(20);
        secondTextField.setFont(new Font("Raleway", Font.BOLD, 20));
        secondTextField.setBounds(420, 190, 250, 30);
        add(secondTextField);

        okay = new JButton("Okay");
        okay.setFont(new Font("Arial", Font.BOLD, 14));
        okay.setBackground(Color.BLACK);
        okay.setForeground(Color.WHITE);
        okay.setBounds(420, 250, 250, 30);
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
        String tableNameOne = firsTextField.getText();
        String tableNameTwo = secondTextField.getText();

        try (Connection connection = new Connect().getConnection()) {
            boolean tableOneExists = doesTableExist(connection, "data", tableNameOne);
            boolean tableTwoExists = doesTableExist(connection, "data", tableNameTwo);

            if (tableOneExists && tableTwoExists) {
                System.out.println("Both tables exist.");
                new Conditions(tableNameOne, tableNameTwo);

            } else {
                if (!tableOneExists) {
                    System.out.println("Table " + tableNameOne + " does not exist.");
                }
                if (!tableTwoExists) {
                    System.out.println("Table " + tableNameTwo + " does not exist.");
                }
            }
            setVisible(false);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private boolean doesTableExist(Connection connection, String schemaName, String tableName) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet resultSet = metaData.getTables(null, schemaName, tableName, new String[] { "TABLE" })) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    

    public static void main(String[] args) {
        new TakingTables();
    }

}