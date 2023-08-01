package Student;

public class OngoingProject extends Project {
    private String additionalProperty;

    // Constructor
    public OngoingProject(int projectId, String title, String description, String progress, String additionalProperty) {
        super(projectId, title, description, progress);
        this.additionalProperty = additionalProperty;
    }

    // Getter and Setter for the additionalProperty
    public String getAdditionalProperty() {
        return additionalProperty;
    }

    public void setAdditionalProperty(String additionalProperty) {
        this.additionalProperty = additionalProperty;
    }

    
}





