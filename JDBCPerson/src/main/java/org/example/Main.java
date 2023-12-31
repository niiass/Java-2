package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        // 1. შექმენით მონაცემთა ბაზა "Test_JDBC"
        String url = "jdbc:mysql://localhost:3306/java2";
        String username = "root";
        String password = "password";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            // 2. შექმენით ცხრილი "Person"
            String createTableQuery = "CREATE TABLE IF NOT EXISTS Person (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255)," +
                    "last_name VARCHAR(255)," +
                    "age INT," +
                    "sqesi VARCHAR(255)" +
                    ")";
            try (Statement statement = connection.createStatement()) {
                statement.execute(createTableQuery);
            }

            // 3. შეიყვანეთ მონაცემები კონსოლიდან
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter person information:");
            System.out.print("Name: ");
            String name = scanner.next();
            System.out.print("Last Name: ");
            String lastName = scanner.next();
            System.out.print("Age: ");
            int age = scanner.nextInt();
            System.out.print("Sqesi: ");
            String sqesi = scanner.next();

            // 4. შექმენით ბაზაში ჩანაწერი JDBC-ის გამოყენებით
            String insertQuery = "INSERT INTO Person (name, last_name, age, sqesi) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, lastName);
                preparedStatement.setInt(3, age);
                preparedStatement.setString(4, sqesi);
                preparedStatement.executeUpdate();
            }

            // 5. წამოიღოთ მონაცემები და დაბეჭდეთ ეკრანზე
            String selectQuery = "SELECT * FROM Person";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectQuery)) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String personName = resultSet.getString("name");
                    String personLastName = resultSet.getString("last_name");
                    int personAge = resultSet.getInt("age");
                    String personSqesi = resultSet.getString("sqesi");

                    System.out.println("ID: " + id + ", Name: " + personName + ", Last Name: " + personLastName +
                            ", Age: " + personAge + ", Sqesi: " + personSqesi);
                }
            }

            // 6. ბაზის დახურვა
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}