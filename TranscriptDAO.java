import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TranscriptDAO {
    private Database database;

    public TranscriptDAO(Database database) {
        this.database = database;
    }

    public List<TranscriptEntry> getTranscript(int studentId) {
        List<TranscriptEntry> transcript = new ArrayList<>();

        try {
            PreparedStatement stmt = database.getPreparedStatement(DatabaseConstants.getTranscriptById);
            stmt.setInt(1, studentId);

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                transcript.add(hydrateTranscriptEntry(result));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving transcript!", e);
        }

        return transcript;
    }

    private TranscriptEntry hydrateTranscriptEntry(ResultSet result) throws SQLException {
        TranscriptEntry entry = new TranscriptEntry();
        
        entry.setCourseId(result.getString("course_id"));
        entry.setCourseTitle(result.getString("title"));
        entry.setSemester(result.getString("semester"));
        entry.setYear(result.getInt("year"));
        entry.setGrade(result.getString("grade"));
        entry.setCredits(result.getInt("credits"));

        return entry;
    }
}