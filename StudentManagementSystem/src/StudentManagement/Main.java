package StudentManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    static final String URL =
            "jdbc:mysql://localhost:3306/student_management_db";
    static final String USER = "root";
    static final String PASSWORD = "Abinaya@1808";

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con =
                    DriverManager.getConnection(URL, USER, PASSWORD)) {

                System.out.println("Database Connected Successfully!");

                while (true) {
                    System.out.println("\n--- Student Management System ---");
                    System.out.println("1. Add Student");
                    System.out.println("2. View Students");
                    System.out.println("3. Search Student");
                    System.out.println("4. Delete Student");
                    System.out.println("5. Exit");
                    System.out.print("Enter your choice: ");

                    int choice;

                    try {
                        choice = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Enter a valid number!");
                        continue;
                    }

                    switch (choice) {
                        case 1:
                            addStudent(con);
                            break;
                        case 2:
                            viewStudents(con);
                            break;
                        case 3:
                            searchStudent(con);
                            break;
                        case 4:
                            deleteStudent(con);
                            break;
                        case 5:
                            System.out.println("Thank you!");
                            return;
                        default:
                            System.out.println("Invalid choice!");
                    }
                }

            }

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    static void addStudent(Connection con) throws SQLException {

        System.out.print("Enter student name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();

        System.out.print("Enter phone: ");
        String phone = sc.nextLine().trim();

        if (name.isEmpty() || email.isEmpty()) {
            System.out.println("Name and email are required!");
            return;
        }

        String sql = "INSERT INTO students "
                + "(student_name, email, phone) VALUES (?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone.isEmpty() ? null : phone);

            ps.executeUpdate();
            System.out.println("Student added successfully!");

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("This email already exists!");
            } else {
                throw e;
            }
        }
    }

    static void viewStudents(Connection con) throws SQLException {

        String sql = "SELECT student_id, student_name, email, phone, "
                + "created_at FROM students ORDER BY student_id";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- Student Records ---");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("Student ID: "
                        + rs.getInt("student_id"));
                System.out.println("Name: "
                        + rs.getString("student_name"));
                System.out.println("Email: "
                        + rs.getString("email"));
                System.out.println("Phone: "
                        + rs.getString("phone"));
                System.out.println("Created At: "
                        + rs.getTimestamp("created_at"));
                System.out.println("----------------------------");
            }

            if (!found) {
                System.out.println("No student records found.");
            }
        }
    }

    static void searchStudent(Connection con) throws SQLException {

        System.out.print("Enter Student ID: ");

        int id;

        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Student ID!");
            return;
        }

        String sql = "SELECT student_id, student_name, email, phone, "
                + "created_at FROM students WHERE student_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\nStudent Found!");
                    System.out.println("Student ID: "
                            + rs.getInt("student_id"));
                    System.out.println("Name: "
                            + rs.getString("student_name"));
                    System.out.println("Email: "
                            + rs.getString("email"));
                    System.out.println("Phone: "
                            + rs.getString("phone"));
                    System.out.println("Created At: "
                            + rs.getTimestamp("created_at"));
                } else {
                    System.out.println("Student not found!");
                }
            }
        }
    }

    static void deleteStudent(Connection con) throws SQLException {

        System.out.print("Enter Student ID to delete: ");

        int id;

        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Student ID!");
            return;
        }

        String sql = "DELETE FROM students WHERE student_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student not found!");
            }
        }
    }
}