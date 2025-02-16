import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseRemovalDAO {
    private Database database;

    public CourseRemovalDAO(Database database) {
        this.database = database;
    }

    public List<Section> getEnrolledSections(int studentId) {
        List<Section> sections = new ArrayList<>();
        
        try {
            PreparedStatement stmt = database.getPreparedStatement(DatabaseConstants.getEnrolledSections);
            stmt.setInt(1, studentId);

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                Section section = new Section();
                section.setCourseId(result.getString("course_id"));
                section.setSectionId(result.getString("sec_id"));
                section.setTitle(result.getString("title"));
                section.setSemester(result.getString("semester"));
                section.setYear(result.getInt("year"));
                section.setDepartment(result.getString("dept_name"));
                section.setCredits(result.getInt("credits"));
                sections.add(section);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting enrolled sections!", e);
        }

        return sections;
    }

    public boolean removeEnrollment(int studentId, Section section) {
        try {
            PreparedStatement stmt = database.getPreparedStatement(DatabaseConstants.removeEnrollment);
            stmt.setInt(1, studentId);
            stmt.setString(2, section.getCourseId());
            stmt.setString(3, section.getSectionId());
            stmt.setString(4, section.getSemester());
            stmt.setInt(5, section.getYear());

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error removing enrollment!", e);
        }
    }
}