import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {
    private Database database;

    public EnrollmentDAO(Database database) {
        this.database = database;
    }

    public List<Section> getAvailableSections(String semester, int year) {
        List<Section> sections = new ArrayList<>();
        try {
            PreparedStatement stmt = database.getPreparedStatement(DatabaseConstants.getAvailableSections);
            stmt.setString(1, semester);
            stmt.setInt(2, year);
            
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
            throw new RuntimeException("Error getting available sections!", e);
        }

        return sections;
    }

    public boolean isAlreadyEnrolled(int studentId, String courseId) {
        try {
            PreparedStatement stmt = database.getPreparedStatement(DatabaseConstants.checkEnrollment);
            stmt.setInt(1, studentId);
            stmt.setString(2, courseId);

            ResultSet result = stmt.executeQuery();

            return result.next(); // Returns true if student is already enrolled

        } catch (SQLException e) {
            throw new RuntimeException("Error checking enrollment!", e);
        }
    }

    public List<String> getMissingPrerequisites(int studentId, String courseId) {
        List<String> missingPrereqs = new ArrayList<>();
        try {
            PreparedStatement stmt = database.getPreparedStatement(DatabaseConstants.checkPrerequisites);
            stmt.setString(1, courseId);
            stmt.setInt(2, studentId);

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                missingPrereqs.add(result.getString("prereq_id"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error checking prerequisites!", e);
        }

        return missingPrereqs;
    }

    public void enrollStudent(int studentId, Section section) {
        try {
            PreparedStatement stmt = database.getPreparedStatement(DatabaseConstants.enrollStudent);
            stmt.setInt(1, studentId);
            stmt.setString(2, section.getCourseId());
            stmt.setString(3, section.getSectionId());
            stmt.setString(4, section.getSemester());
            stmt.setInt(5, section.getYear());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error enrolling student!", e);
        }
    }
}