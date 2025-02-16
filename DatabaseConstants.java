public class DatabaseConstants {

    //login information
    public static final String JdbcConnectionString = "jdbc:mysql://127.0.0.1:3306/university?useSSL=false&allowPublicKeyRetrieval=true";
    public static final String JdbcUserId = "rasmusa4";
    public static final String JdbcPassword = "-]d6M*~HJgN";

    //prepared statements 
    public static final String selectStudentById = "SELECT * FROM student WHERE id = ?";
  
    public static final String getTranscriptById = 
        "SELECT c.course_id, c.title, c.credits, " +
        "       t.semester, t.year, t.grade " +
        "FROM takes t " +
        "JOIN course c ON t.course_id = c.course_id " +
        "WHERE t.ID = ? " +
        "ORDER BY t.year, " +
        "CASE t.semester " +
        "    WHEN 'Fall' THEN 3 " +
        "    WHEN 'Spring' THEN 1 " +
        "    WHEN 'Summer' THEN 2 " +
        "END";

        public static final String getDegreeRequirements = 
        "SELECT c.course_id, c.title, c.credits " +
        "FROM course c " +
        "JOIN student s ON c.dept_name = s.dept_name " +
        "WHERE s.ID = ? " +
        "AND c.course_id NOT IN ( " +
        "    SELECT t.course_id " +
        "    FROM takes t " +
        "    WHERE t.ID = ? " +
        "    AND t.grade IS NOT NULL " +
        "    AND t.grade != 'F' " +
        ") " +
        "ORDER BY c.course_id";

        public static final String getAvailableSections = 
        "SELECT s.course_id, s.sec_id, c.title, s.semester, s.year, " +
        "       c.dept_name, c.credits " +
        "FROM section s " +
        "JOIN course c ON s.course_id = c.course_id " +
        "WHERE s.semester = ? AND s.year = ? " +
        "ORDER BY s.course_id, s.sec_id";

    public static final String checkEnrollment = 
        "SELECT 1 FROM takes " +
        "WHERE ID = ? AND course_id = ? " +
        "AND (grade IS NULL OR grade != 'F')";

    public static final String checkPrerequisites = 
        "SELECT p.prereq_id FROM prereq p " +
        "WHERE p.course_id = ? " +
        "AND p.prereq_id NOT IN ( " +
        "    SELECT t.course_id FROM takes t " +
        "    WHERE t.ID = ? " +
        "    AND t.grade IS NOT NULL " +
        "    AND t.grade != 'F' " +
        ")";

    public static final String enrollStudent = 
        "INSERT INTO takes (ID, course_id, sec_id, semester, year) " +
        "VALUES (?, ?, ?, ?, ?)";

        public static final String getEnrolledSections = 
        "SELECT t.course_id, t.sec_id, c.title, t.semester, t.year, " +
        "       c.dept_name, c.credits, t.grade " +
        "FROM takes t " +
        "JOIN course c ON t.course_id = c.course_id " +
        "WHERE t.ID = ? AND (t.grade IS NULL) " +
        "ORDER BY t.year, " +
        "CASE t.semester " +
        "    WHEN 'Fall' THEN 3 " +
        "    WHEN 'Spring' THEN 1 " +
        "    WHEN 'Summer' THEN 2 " +
        "END, t.course_id";

    public static final String removeEnrollment = 
        "DELETE FROM takes " +
        "WHERE ID = ? " +
        "AND course_id = ? " +
        "AND sec_id = ? " +
        "AND semester = ? " +
        "AND year = ? " +
        "AND grade IS NULL";
    }