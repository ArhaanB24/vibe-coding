// Abstract class representing a generic task
abstract class Task {
    private String title;
    private String description;
    private String category;
    private int priority;

    public Task(String title, String description, String category, int priority) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public int getPriority() {
        return priority;
    }

    public abstract String getTaskDetails();
}
