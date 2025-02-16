public class ValidateStudent {
    private StudentDAO studentDAO;

    public ValidateStudent(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    /**
     * Searches for a student by their ID and returns the Student object if found
     * @param studentId The ID of the student to search for
     * @return Student object if found, null if not found
     */
    public Student searchForStudent(int studentId) {
        try {
            Student student = studentDAO.getStudentById(studentId);
            
            if (student == null) {
                System.err.println("Student ID " + studentId + " not found in database");
                return null;
            }
            
            return student;
        } catch (Exception e) {
            System.err.println("Error searching for student: " + e.getMessage());
            return null;
        }
    }
}