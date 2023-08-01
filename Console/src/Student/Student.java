package Student;import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Student {
    private int id;
    private String email;
    private String password;

 
    public Student() {
    }

    public Student(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

 
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static Student authenticate(Connection connection, String email, String password) throws SQLException {
        String selectQuery = "SELECT * FROM students WHERE email = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String fetchedEmail = resultSet.getString("email");
                    String fetchedPassword = resultSet.getString("password");
                    return new Student(id, fetchedEmail, fetchedPassword);
                }
            }
        }
        return null;
    }

    public static int signUp(Connection connection, String email, String password) throws SQLException {
        String insertQuery = "INSERT INTO students (email, password) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        }
        return -1;
    }
}
