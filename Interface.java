import java.util.List;
import java.util.Scanner;

public class Interface {
    private Scanner scanner;
    private Database database;
    private ValidateStudent validator;
    private StudentDAO studentDAO;
    private TranscriptDAO transcriptDAO;
    private DegreeRequirementsDAO degreeDAO;
    private EnrollmentDAO enrollmentDAO;
    private CourseRemovalDAO removalDAO;
    private Student currentStudent;

    public Interface() {
        this.scanner = new Scanner(System.in);
        this.database = new JdbcDatabase();
        this.studentDAO = new StudentDAO(database);
        this.validator = new ValidateStudent(studentDAO);
        this.transcriptDAO = new TranscriptDAO(database);
        this.degreeDAO = new DegreeRequirementsDAO(database);
        this.enrollmentDAO = new EnrollmentDAO(database);
        this.removalDAO = new CourseRemovalDAO(database);
    }

    public void start() {
        System.out.println("Welcome to the Student Information System");
        
        // Get and validate student ID
        if (!login()) {
            return;
        }

        while (true) {
            displayMenu();
            int choice = getMenuChoice();
            
            if (choice == 5) {
                System.out.println("Goodbye!");
                break;
            }
            
            processMenuChoice(choice);
        }
        
        scanner.close();
    }

    private boolean login() {
        while (true) {
            System.out.print("\nPlease enter your student ID (or -1 to exit): ");
            
            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid number.");

                scanner.next();
                continue;
            }
            
            int studentId = scanner.nextInt();
            
            if (studentId == -1) {
                System.out.println("Goodbye!");
                return false;
            }
            
            currentStudent = validator.searchForStudent(studentId);
            
            if (currentStudent == null) {
                System.out.println("No student found with ID: " + studentId);
                continue;
            }
            
            System.out.println("\nWelcome, " + currentStudent.getName() + "!");

            return true;
        }
    }

    private void displayMenu() {
        System.out.println("\nPlease select an operation:");
        System.out.println("1. Get Transcript");
        System.out.println("2. Check Degree Requirements");
        System.out.println("3. Add Course");
        System.out.println("4. Remove Course");
        System.out.println("5. Exit");
    }

    private int getMenuChoice() {
        while (true) {
            System.out.print("\nEnter your choice (1-5): ");
            
            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid number.");

                scanner.next();
                continue;
            }
            
            int choice = scanner.nextInt();
            
            if (choice >= 1 && choice <= 5) {
                return choice;

            } else {
                System.out.println("Please enter a number between 1 and 5.");
            }
        }
    }

    private void processMenuChoice(int choice) {
        try {
            switch (choice) {
                case 1:
                    showTranscript();
                    break;

                case 2:
                    checkDegreeRequirements();
                    break;

                case 3:
                    addCourse();
                    break;

                case 4:
                    removeCourse();
                    break;
            }

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
        
        // Pause before showing menu again
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine(); // Clear any leftover newline
        scanner.nextLine(); // Wait for user input
    }

    private void showTranscript() {
        System.out.println("\nTranscript for " + currentStudent.getName());
        System.out.println("----------------------------------------");
        
        List<TranscriptEntry> transcript = transcriptDAO.getTranscript(currentStudent.getId());
        
        if (transcript.isEmpty()) {
            System.out.println("No courses found.");

        } else {
            for (TranscriptEntry entry : transcript) {
                System.out.println(entry);
            }
        }
    }

    private void checkDegreeRequirements() {
        System.out.println("\nDegree Requirements for " + currentStudent.getName());
        System.out.println("Department: " + currentStudent.getDepartmentName());
        System.out.println("----------------------------------------");
        
        List<RequiredCourse> requirements = degreeDAO.getRemainingRequirements(currentStudent.getId());
        
        if (requirements.isEmpty()) {
            System.out.println("All department requirements completed!");

        } else {
            int totalCreditsNeeded = 0;
            for (RequiredCourse course : requirements) {
                System.out.println(course);

                totalCreditsNeeded += course.getCredits();

            }
            System.out.println("----------------------------------------");
            System.out.println("Total additional credits needed: " + totalCreditsNeeded);
        }
    }

    private void addCourse() {
        // Get semester
        System.out.print("\nEnter semester (Fall/Spring/Summer): ");
        String semester = scanner.next();

        if (!semester.matches("(?i)Fall|Spring|Summer")) {
            System.out.println("Invalid semester. Must be Fall, Spring, or Summer.");
            return;
        }
        
        // Get year
        System.out.print("Enter year: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid year.");
            scanner.next();
            return;
        }

        int year = scanner.nextInt();
        
        // Get available sections
        List<Section> sections = enrollmentDAO.getAvailableSections(semester, year);

        if (sections.isEmpty()) {
            System.out.println("No sections available for " + semester + " " + year);
            return;
        }
        
        // Display available sections
        System.out.println("\nAvailable Sections:");

        for (int i = 0; i < sections.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, sections.get(i));
        }
        
        // Get section choice
        System.out.print("\nEnter section number to enroll in (or 0 to cancel): ");

        if (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid section number.");
            scanner.next();
            return;
        }
        
        int choice = scanner.nextInt() - 1;
        if (choice == -1) {
            System.out.println("Enrollment cancelled.");
            return;
        }

        if (choice < 0 || choice >= sections.size()) {
            System.out.println("Invalid section number.");
            return;
        }
        
        Section selectedSection = sections.get(choice);
        
        // Validate enrollment
        if (enrollmentDAO.isAlreadyEnrolled(currentStudent.getId(), selectedSection.getCourseId())) {
            System.out.println("You are already enrolled in or have completed this course.");
            return;
        }
        
        List<String> missingPrereqs = enrollmentDAO.getMissingPrerequisites(
            currentStudent.getId(), selectedSection.getCourseId());
        
        if (!missingPrereqs.isEmpty()) {
            System.out.println("Cannot enroll. Missing prerequisites:");
            for (String prereq : missingPrereqs) {
                System.out.println("- " + prereq);
            }
            return;
        }
        
        // Enroll the student
        try {
            enrollmentDAO.enrollStudent(currentStudent.getId(), selectedSection);
            System.out.println("Successfully enrolled in " + selectedSection);

        } catch (RuntimeException e) {
            System.out.println("Error enrolling in course: " + e.getMessage());
        }
    }

    private void removeCourse() {
        List<Section> enrolledSections = removalDAO.getEnrolledSections(currentStudent.getId());
        
        if (enrolledSections.isEmpty()) {
            System.out.println("You are not currently enrolled in any courses that can be dropped.");
            return;
        }
        
        System.out.println("\nCurrently Enrolled Sections (without grades):");
        System.out.println("----------------------------------------");
        for (int i = 0; i < enrolledSections.size(); i++) {
            Section section = enrolledSections.get(i);
            System.out.printf("%d. %s (%s %d)%n", 
                i + 1, 
                section,
                section.getSemester(),
                section.getYear());
        }
        
        System.out.print("\nEnter section number to remove (or 0 to cancel): ");
        if (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid section number.");
            scanner.next();
            return;
        }
        
        int choice = scanner.nextInt() - 1;
        if (choice == -1) {
            System.out.println("Removal cancelled.");
            return;
        }
        if (choice < 0 || choice >= enrolledSections.size()) {
            System.out.println("Invalid section number.");
            return;
        }
        
        Section selectedSection = enrolledSections.get(choice);
        
        System.out.print("Are you sure you want to remove " + 
                       selectedSection.getCourseId() + " - " + 
                       selectedSection.getTitle() + "? (y/n): ");
                       
        String confirm = scanner.next();
        
        if (confirm.equalsIgnoreCase("y")) {
            boolean removed = removalDAO.removeEnrollment(currentStudent.getId(), selectedSection);

            if (removed) {
                System.out.println("Successfully removed from " + selectedSection);

            } else {
                System.out.println("Could not remove course. It may have already been removed.");
            }

        } else {
            System.out.println("Removal cancelled.");
        }
    }
}