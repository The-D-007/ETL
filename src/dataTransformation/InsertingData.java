package dataTransformation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

import dataProvider.Connect;
import load.TakingTables;

public class InsertingData {
    static int count = 0;

    public InsertingData(File selectedFile, String tableName) {
        count++;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(selectedFile));
            Connect connect = new Connect();

            String firstLine = bufferedReader.readLine();
            if (firstLine != null) {
                String[] columns = firstLine.split(",");

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    insertData(connect, tableName, columns, line);
                }
            }
           
            
        } catch (Exception E) {
            E.printStackTrace();
        }

        if (count > 1) {
            System.out.println(count);
            new TakingTables();
        }

    }

    private void insertData(Connect connect, String tableName, String[] columns, String line) throws SQLException {
        String commas = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        Pattern pattern = Pattern.compile(commas);
        String[] data = pattern.split(line, -1);

        StringBuilder insertInSQL = new StringBuilder("INSERT INTO " + tableName + " (");
        for (String column : columns) {
            insertInSQL.append("`").append(column.trim()).append("`,");
        }
        insertInSQL.setLength(insertInSQL.length() - 1);
        insertInSQL.append(" ) VALUES (");

        for (int i = 0; i < data.length; i++) {
            insertInSQL.append("?,");
        }
        insertInSQL.setLength(insertInSQL.length() - 1);
        insertInSQL.append(")");

        try (PreparedStatement pstmt = connect.prepareStatement(insertInSQL.toString())) {
            for (int i = 0; i < data.length; i++) {
                String value = data[i].trim();
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                pstmt.setString(i + 1, value);
            }
            pstmt.executeUpdate();
        }

    }
}
