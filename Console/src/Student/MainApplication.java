package Student;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;



public class MainApplication {
    public static void main(String[] args) {
    	        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return; 
        }

        try (Connection connection = DatabaseManager.getConnection()) {
            System.out.println("Connected to the database!");
            Scanner scanner = new Scanner(System.in);

            // Login or Sign-up
            Student student = loginOrSignUp(connection, scanner);
            if (student == null) {
                System.out.println("Exiting...");
                return;
            }

            System.out.println("Submits Your Projects Here..!");
            System.out.println("-----------------------------");

            while (true) {
            	System.out.println("\nOptions:");
              System.out.println("1. Upload a new project");
              System.out.println("2. Edit a project by ID");
              System.out.println("3. Update project progress");
              System.out.println("4. View All Projects");
              System.out.println("5. Exit");
              System.out.println("Enter your choice (1/2/3/4/5): ");
              System.out.println("-----------------------------");
              int choice = scanner.nextInt();
              scanner.nextLine(); 

              switch (choice) {
                  case 1:
                      System.out.print("Enter your project title: ");
                      String projectTitle = scanner.nextLine();

                      System.out.print("Enter your project description: ");
                      String projectDescription = scanner.nextLine();

                      uploadProject(connection, projectTitle, projectDescription);
                      System.out.println("Project uploaded successfully!");
                      break;
                  case 2:
                     
                      System.out.print("Enter the project ID you want to edit: ");
                      if (scanner.hasNextInt()) {
                          int projectIdToEdit = scanner.nextInt();
                          scanner.nextLine();
                          System.out.print("Enter the updated project title: ");
                          String updatedTitle = scanner.nextLine();

                          System.out.print("Enter the updated project description: ");
                          String updatedDescription = scanner.nextLine();

                          editProject(connection, projectIdToEdit, updatedTitle, updatedDescription);
                      } else {
                          System.out.println("Invalid input for project ID. Please enter a valid integer.");
                          scanner.nextLine(); 
                      }
                      break;
                  case 3:
                      
                      System.out.print("Enter the project ID to update progress: ");
                      int projectIdToUpdateProgress = scanner.nextInt();
                      scanner.nextLine(); 

                      System.out.print("Enter the updated project progress: ");
                      String updatedProgress = scanner.nextLine();

                      updateProjectProgress(connection, projectIdToUpdateProgress, updatedProgress);
                      break;
                  case 4:
                    
                      viewAllProjects(connection);
                      break;
                  case 5:
                      System.out.println("Your Project Progress Is Submitted Successfulyy");
                      return;
                  default:
                      System.out.println("Invalid choice. Please try again.");
              }
          }
        } catch (SQLException e) {
        	e.printStackTrace();
            }
    }



    public static void uploadProject(Connection connection, String title, String description) throws SQLException {
        String insertQuery = "INSERT INTO projects (title, description) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.executeUpdate();
        }
    }

    public static void editProject(Connection connection, int projectId, String title, String description) throws SQLException {
        String updateQuery = "UPDATE projects SET title = ?, description = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, projectId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Project with ID " + projectId + " updated successfully!");
            } else {
                System.out.println("Project with ID " + projectId + " not found.");
            }
        }
    }

    public static void updateProjectProgress(Connection connection, int projectId, String progress) throws SQLException {
        String updateQuery = "UPDATE projects SET progress = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, progress);
            preparedStatement.setInt(2, projectId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Project progress updated for ID " + projectId + " successfully!");
            } else {
                System.out.println("Project with ID " + projectId + " not found.");
            }
        }
    }

//    public static void viewAllProjects(Connection connection) {
//        String selectQuery = "SELECT * FROM projects";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
//             ResultSet resultSet = preparedStatement.executeQuery()) {
//            System.out.println("All Projects:");
//            while (resultSet.next()) {
//                int projectId = resultSet.getInt("id");
//                String title = resultSet.getString("title");
//                String description = resultSet.getString("description");
//                String progress = resultSet.getString("progress");
//
//                System.out.println("Project ID: " + projectId);
//                System.out.println("Title: " + title);
//                System.out.println("Description: " + description);
//                System.out.println("Progress: " + progress);
//                System.out.println("-----------------------------");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    
    
    
    //Method to get project details using inheritance
    public static void viewAllProjects(Connection connection) {
        String selectQuery = "SELECT * FROM projects";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            System.out.println("All Projects:");
            while (resultSet.next()) {
                int projectId = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String progress = resultSet.getString("progress");

                // Check if progress is null or empty
                if (progress == null || progress.isEmpty()) {
                    System.out.println("Invalid progress value for Project ID " + projectId);
                    continue;
                }

                // Check if it's an ongoing project based on the progress
                if (progress.equalsIgnoreCase("In Progress")) {
                    String additionalProperty = "Some additional property specific to ongoing projects";
                    OngoingProject ongoingProject = new OngoingProject(projectId, title, description, progress, additionalProperty);
                    viewProjectDetails(ongoingProject);
                } else {
                    // For non-ongoing projects, use the Project class as before
                    Project project = new Project(projectId, title, description, progress);
                    viewProjectDetails(project);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Method to view project details using polymorphism
    public static void viewProjectDetails(Project project) {
        System.out.println("Project ID: " + project.getProjectId());
        System.out.println("Title: " + project.getTitle());
        System.out.println("Description: " + project.getDescription());
        System.out.println("Progress: " + project.getProgress());

        // Additional details for OngoingProject
        if (project instanceof OngoingProject) {
            OngoingProject ongoingProject = (OngoingProject) project;
            String additionalProperty = ongoingProject.getAdditionalProperty();
            System.out.println("Additional Property: " + additionalProperty);
        }

        System.out.println("-----------------------------");
    }

    
    private static Student loginOrSignUp(Connection connection, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Login");
            System.out.println("2. Sign-up");
            System.out.println("3. Exit");

            System.out.print("Enter your choice (1/2/3): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter your email: ");
                    String email = scanner.nextLine();

                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine();

                    Student authenticatedStudent = Student.authenticate(connection, email, password);
                    if (authenticatedStudent != null) {
                        System.out.println("Login successful!");
                        return authenticatedStudent;
                    } else {
                        System.out.println("Invalid email or password. Please try again.");
                    }
                    break;
                case 2:
                    System.out.print("Enter your email: ");
                    String newEmail = scanner.nextLine();

                    System.out.print("Enter your password: ");
                    String newPassword = scanner.nextLine();

                    int newStudentId = Student.signUp(connection, newEmail, newPassword);
                    if (newStudentId != -1) {
                        System.out.println("Sign-up successful! You can now log in.");
                    } else {
                        System.out.println("Error occurred during sign-up. Please try again.");
                    }
                    break;
                case 3:
                    return null; 
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

