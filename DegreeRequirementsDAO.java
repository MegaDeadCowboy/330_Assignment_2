import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DegreeRequirementsDAO {
    private Database database;

    public DegreeRequirementsDAO(Database database) {
        this.database = database;
    }

    public List<RequiredCourse> getRemainingRequirements(int studentId) {
        List<RequiredCourse> remainingCourses = new ArrayList<>();

        try {
            PreparedStatement stmt = database.getPreparedStatement(DatabaseConstants.getDegreeRequirements);
            stmt.setInt(1, studentId);
            stmt.setInt(2, studentId);  
            
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                remainingCourses.add(hydrateRequiredCourse(result));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving degree requirements!", e);
        }

        return remainingCourses;
    }

    private RequiredCourse hydrateRequiredCourse(ResultSet result) throws SQLException {
        RequiredCourse course = new RequiredCourse();
        
        course.setCourseId(result.getString("course_id"));
        course.setTitle(result.getString("title"));
        course.setCredits(result.getInt("credits"));

        return course;
    }
}