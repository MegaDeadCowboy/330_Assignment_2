import java.sql.*;

public class SimpleJdbcExample {

    private String connectionString = "jdbc:mysql://127.0.0.1:3306/university?useSSL=false&allowPublicKeyRetrieval=true";
    private String userId = "rasmusa4";
    private String password = "-]d6M*~HJgN";


    public void runSimpleStatement() {
        try {
            Connection connection = DriverManager.getConnection(connectionString, userId, password);

            Statement statement = connection.createStatement();
            String sql = "SELECT dept_name, AVG(salary) FROM instructor GROUP BY dept_name";

            ResultSet result = statement.executeQuery(sql);

            while(result.next()) {
                String deptName = result.getString("dept_name");
                float avgSalary = result.getFloat(2);

                System.out.println(deptName + " " + avgSalary);
            }
        }
        catch (SQLException sqle) {
            System.out.println("Error executing query! " + sqle);
        }
    }


    public void runSimpleInsert() {
        try {
            Connection connection = DriverManager.getConnection(connectionString, userId, password);

            Statement statement = connection.createStatement();
            String sql = "INSERT INTO instructor VALUES (77987, 'Kim', 'Physics', 98000)";

            statement.executeUpdate(sql);
        }
        catch (SQLException sqle) {
            System.out.println("Error executing insert! " + sqle);
        }
    }


    public void runSimplePreparedStatement() {
        try {
            Connection connection = DriverManager.getConnection(connectionString, userId, password);

            String sql = "SELECT course_id FROM section WHERE semester=? AND year= ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, "Spring");
            statement.setInt(2, 2010);

            ResultSet result = statement.executeQuery();

            while(result.next()) {
                String courseId = result.getString("course_id");

                System.out.println(courseId);
            }

        }
        catch (SQLException sqle) {
            System.out.println("Error executing prepared query! " + sqle);
        }
    }
}
