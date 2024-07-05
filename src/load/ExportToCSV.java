package load;

import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.Statement;

import dataProvider.Connect;

public class ExportToCSV {

    public ExportToCSV(){}

    public ExportToCSV(String CSVFile) {
        
        try {
            Connect connect = new Connect();
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + CSVFile);
            FileWriter fileWriter = new FileWriter(CSVFile);

            int columnCount = resultSet.getMetaData().getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                fileWriter.append(resultSet.getMetaData().getColumnName(i));
                if (i < columnCount) fileWriter.append(",");
            }
            fileWriter.append("\n");
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    fileWriter.append(resultSet.getString(i));
                    if (i < columnCount) fileWriter.append(",");
                }
                fileWriter.append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

