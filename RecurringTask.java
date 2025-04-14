public class RecurringTask extends AbstractTask {
    private String recurrencePattern;

    public RecurringTask(String title, String description, String category, int priority, String recurrencePattern) {
        super(title, description, category, priority);
        this.recurrencePattern = recurrencePattern;
    }

    public String getRecurrencePattern() {
        return recurrencePattern;
    }

    @Override
    public String getTaskDetails() {
        return "Recurring Task:\n" +
                "Title: " + title + "\n" +
                "Description: " + description + "\n" +
                "Category: " + category + "\n" +
                "Priority: " + priority + "\n" +
                "Recurs: " + recurrencePattern;
    }
}
