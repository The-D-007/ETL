package load;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import dataProvider.Connect;

public class Tables {
    public Tables(String[] tableNames) {
        try (Connection connection = new Connect().getConnection()) {
            boolean allTablesExist = true;

            for (String tableName : tableNames) {
                System.out.println("Checking table: " + tableName); // Debug statement
                if (tableName != null && !tableName.isEmpty()) {
                    boolean tableExists = doesTableExist(connection, "data", tableName);
                    if (!tableExists) {
                        System.out.println("Table " + tableName + " does not exist.");
                        allTablesExist = false;
                    }
                } else {
                    System.out.println("Invalid table name: " + tableName);
                    allTablesExist = false;
                }
            }

            if (allTablesExist) {
                System.out.println("All tables exist.");
                new Conditions(tableNames[0], tableNames[1]);
            }
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
}
