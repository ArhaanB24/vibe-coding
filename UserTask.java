// Concrete implementation of a Task
class UserTask extends Task {
    public UserTask(String title, String description, String category, int priority) {
        super(title, description, category, priority);
    }

    @Override
    public String getTaskDetails() {
        return "Task: " + getTitle() + 
               "\nDescription: " + getDescription() + 
               "\nCategory: " + getCategory() + 
               "\nPriority: " + getPriority();
    }
}
