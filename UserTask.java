public class UserTask extends AbstractTask {

    public UserTask(String title, String description, String category, int priority) {
        super(title, description, category, priority);
    }

    @Override
    public String getTaskDetails() {
        return "User Task:\n" +
                "Title: " + title + "\n" +
                "Description: " + description + "\n" +
                "Category: " + category + "\n" +
                "Priority: " + priority;
    }
}
