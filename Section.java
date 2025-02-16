public class Section {
    private String courseId;
    private String sectionId;
    private String title;
    private String semester;
    private int year;
    private String department;
    private int credits;

    public String getCourseId() { 
        return courseId; 
    }

    public String getSectionId() { 
        return sectionId; 
    }

    public String getTitle() { 
        return title; 
    }

    public String getSemester() { 
        return semester; 
    }

    public int getYear() { 
        return year; 
    }

    public String getDepartment() { 
        return department; 
    }

    public int getCredits() { 
        return credits; 
    }

    public void setCourseId(String courseId) { 
        this.courseId = courseId; 
    }    

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId; 
    }
    
    public void setTitle(String title) { 
        this.title = title; 
    }
    
    public void setSemester(String semester) { 
        this.semester = semester; 
    }
    
    public void setYear(int year) { 
        this.year = year; 
    }
    
    public void setDepartment(String department) { 
        this.department = department; 
    }
    
    public void setCredits(int credits) { 
        this.credits = credits; 
    }

    @Override
    public String toString() {
        return String.format("%s Section %s: %s (%d credits)", 
            courseId, sectionId, title, credits);
    }
}