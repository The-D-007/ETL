package dataProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {
    private Connection connection;
    private String URL = "jdbc:mysql://localhost:3306/data";
    private String userName = "root";
    private String password = "370HSSV 0773H";

    public Connect() {
        try {
            this.connection = DriverManager.getConnection(URL, userName, password);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public Statement createStatement() throws SQLException {
        return this.connection.createStatement();
    }

    public static void main(String[] args) {
        new Connect();
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return this.connection.prepareStatement(sql);
    }
}
