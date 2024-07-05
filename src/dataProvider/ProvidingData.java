package dataProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.sql.Statement;

import dataTransformation.InsertingData;

public class ProvidingData {

    public ProvidingData(){}

    public ProvidingData(File selectedFile, String tableName) {

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(selectedFile));
            Connect connect = new Connect();

            String firstLine = bufferedReader.readLine();
            if (firstLine != null) {
                String[] columns = firstLine.split(",");

                StringBuilder createTableSQL = new StringBuilder("CREATE TABLE " + tableName + " (");

                for (String column : columns) {
                    createTableSQL.append("`").append(column.trim()).append("` VARCHAR(200),");
                }
                createTableSQL.setLength(createTableSQL.length() - 1);
                createTableSQL.append(");");

                System.out.println(createTableSQL);

                try (Statement statement = connect.createStatement()) {
                    statement.execute(createTableSQL.toString());
                    System.out.println("Table created successfully.");
                }
            }
            new InsertingData(selectedFile, tableName);
        } catch (Exception E) {
            E.printStackTrace();
        }
    }


   public ProvidingData(String tableName, String[] columns) {
        StringBuilder createTableSQL = new StringBuilder("CREATE TABLE " + tableName + " (");
        
        for (String column : columns) {
            createTableSQL.append("`").append(column.trim()).append("` VARCHAR(200),");
        }
        createTableSQL.setLength(createTableSQL.length() - 1);
        createTableSQL.append(");");

        try {
            Connect connect = new Connect();
            Statement statement = connect.createStatement();
            statement.execute(createTableSQL.toString());
            System.out.println("Table " + tableName + " created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
