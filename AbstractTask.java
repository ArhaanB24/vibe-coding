public abstract class AbstractTask implements Task {
    protected String title;
    protected String description;
    protected String category;
    protected int priority;

    public AbstractTask(String title, String description, String category, int priority) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    // Let subclasses define this
    public abstract String getTaskDetails();
}
