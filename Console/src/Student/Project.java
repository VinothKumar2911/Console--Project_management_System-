package Student;

public class Project {
    private int projectId;
    public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	private String title;
    private String description;
    private String progress;

    // Constructor
    public Project(int projectId, String title, String description, String progress) {
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.progress = progress;
    }

    
}
