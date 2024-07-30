package load;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import dataProvider.Connect;
import dataProvider.ProvidingData;

public class Conditions {
    private static final double MONTHLY_FEE = 19.65;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Conditions() {
    }

    public Conditions(String table1, String table2, String[] columns1, String[] columns2, String[] additionalColumns) {

        String[] columns = new String[columns1.length + columns2.length + additionalColumns.length];
        System.arraycopy(columns1, 0, columns, 0, columns1.length);
        System.arraycopy(columns2, 0, columns, columns1.length, columns2.length);
        System.arraycopy(additionalColumns, 0, columns, columns1.length + columns2.length, additionalColumns.length);

        
        executeQuery(table1, table2, columns1, columns2, columns, "active_subs",
                "`" + table1 + "`.`Subscription Date` > CURRENT_DATE");

        executeQuery(table1, table2, columns1, columns2, columns, "has_30_days", "`" + table1
                + "`.`Subscription Date` BETWEEN DATE_SUB(CURRENT_DATE, INTERVAL 30 DAY) AND DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY)");

        executeQuery(table1, table2, columns1, columns2, columns, "has_60_days", "`" + table1
                + "`.`Subscription Date` BETWEEN DATE_SUB(CURRENT_DATE, INTERVAL 60 DAY) AND DATE_SUB(CURRENT_DATE, INTERVAL 31 DAY)");

    }

    private void executeQuery(String table1, String table2, String[] columns1, String[] columns2, String[] columns,
            String outputTable, String condition) {
        Connect connect = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connect = new Connect();
            statement = connect.createStatement();
            LocalDate today = LocalDate.now();

            String columnQueryPart = "";
            for (String column : columns1) {
                columnQueryPart += "`" + table1 + "`.`" + column + "`, ";
            }
            for (String column : columns2) {
                columnQueryPart += "`" + table2 + "`.`" + column + "`, ";
            }
            System.out.println(columnQueryPart);
            columnQueryPart = columnQueryPart.substring(0, columnQueryPart.length() - 2);
            String query = "SELECT " + columnQueryPart + ", `" + table1 + "`.`Subscription Date` FROM `" + table1
                    + "` JOIN `"
                    + table2 + "` ON `" + table1 + "`.`Customer Id` = `" + table2 + "`.`Customer Id`" + " WHERE "
                    + condition;
            System.out.println(query);
            resultSet = statement.executeQuery(query);
            new ProvidingData(outputTable, columns);

            while (resultSet.next()) {
                Object[] values = new Object[columns.length];
                int index = 0;
                LocalDate expirationDate = resultSet.getDate(table1 + ".Subscription Date").toLocalDate();
                for (String column : columns1) {
                    values[index++] = resultSet.getObject(table1 + "." + column);
                }
                for (String column : columns2) {
                    values[index++] = resultSet.getObject(table2 + "." + column);
                }

                if (expirationDate.isAfter(today)) {
                    values[index++] = "Yes";

                    String formattedDate = expirationDate.format(DATE_FORMATTER);
                    values[index++] = formattedDate;

                    double remainingFee = calculateRemainingFee(today, expirationDate);
                    values[index] = remainingFee;
                } else {
                    values[index++] = "No";
                    values[index++] = "N/A";
                    values[index] = "N/A";
                }

                insertData(outputTable, columns, values);
            }
            new ExportToCSV(outputTable);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (statement != null)
                    statement.close();
                if (connect != null)
                    connect.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private double calculateRemainingFee(LocalDate currentDate, LocalDate expirationDate) {
        long monthsBetween = ChronoUnit.MONTHS.between(currentDate.withDayOfMonth(1), expirationDate.withDayOfMonth(1));
        double fee = monthsBetween * MONTHLY_FEE;
        BigDecimal roundedFee = new BigDecimal(fee).setScale(2, RoundingMode.HALF_UP);
        return roundedFee.doubleValue();
    }

    public void insertData(String table, String[] columns, Object[] values) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + table + " (");
        for (String column : columns) {
            sql.append("`").append(column).append("`, ");
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 2) + ") VALUES (");
        for (int i = 0; i < columns.length; i++) {
            sql.append("?, ");
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 2) + ")");

        Connect connect = null;
        PreparedStatement preparedStatement = null;
        try {
            connect = new Connect();
            preparedStatement = connect.prepareStatement(sql.toString());

            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connect != null)
                    connect.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Conditions(String table1, String table2) {
        String[] columns1 = { "Customer Id", "First Name", "Last Name" };
        String[] columns2 = { "Phone 1", "Email", "Website" };
        String[] additionalColumn = { "Active", "Expiration Date", "Fee Remaining" };
        new Conditions(table1, table2, columns1, columns2, additionalColumn);
    }

}
