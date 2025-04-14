import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class TaskManagerApp extends JFrame {
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Task> filteredTasks = new ArrayList<>();
    private DefaultListModel<String> taskListModel = new DefaultListModel<>();
    private JTextArea taskDescriptionArea;
    private JComboBox<String> categoryFilter;
    private JComboBox<String> priorityFilter;
    private JButton resetFilterButton;

    // Available categories and priorities for filtering
    private final String[] categories = {"All Categories", "Work", "Personal", "Study", "Health", "Other"};
    private final String[] priorities = {"All Priorities", "1", "2", "3", "4", "5"};

    public TaskManagerApp() {
        // Set up the frame
        setTitle("Task Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Task Manager");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Main Panel with split panes
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(400);

        // Task List Panel
        JPanel taskListPanel = new JPanel(new BorderLayout());

        // Search and Filter Panel
        JPanel searchFilterPanel = new JPanel(new BorderLayout());

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Filter Panel
        JPanel filterPanel = new JPanel(new GridLayout(1, 5, 5, 5));
        JLabel categoryLabel = new JLabel("Category:");
        categoryFilter = new JComboBox<>(categories);

        JLabel priorityLabel = new JLabel("Priority:");
        priorityFilter = new JComboBox<>(priorities);

        resetFilterButton = new JButton("Reset Filters");

        filterPanel.add(categoryLabel);
        filterPanel.add(categoryFilter);
        filterPanel.add(priorityLabel);
        filterPanel.add(priorityFilter);
        filterPanel.add(resetFilterButton);

        searchFilterPanel.add(searchPanel, BorderLayout.NORTH);
        searchFilterPanel.add(filterPanel, BorderLayout.SOUTH);

        // Task List
        JList<String> taskList = new JList<>(taskListModel);
        taskList.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(taskList);

        taskListPanel.add(searchFilterPanel, BorderLayout.NORTH);
        taskListPanel.add(scrollPane, BorderLayout.CENTER);

        // Task Description Panel
        JPanel descriptionPanel = new JPanel(new BorderLayout());

        JLabel descriptionLabel = new JLabel("Task Details:");
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 16));

        taskDescriptionArea = new JTextArea();
        taskDescriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        taskDescriptionArea.setEditable(false);

        descriptionPanel.add(descriptionLabel, BorderLayout.NORTH);
        descriptionPanel.add(new JScrollPane(taskDescriptionArea), BorderLayout.CENTER);

        // Add panels to split pane
        mainSplitPane.setLeftComponent(taskListPanel);
        mainSplitPane.setRightComponent(descriptionPanel);

        add(mainSplitPane, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel taskTitleLabel = new JLabel("Task Title:");
        JTextField taskTitleField = new JTextField();

        JLabel taskDescriptionLabel = new JLabel("Task Description:");
        JTextField taskDescriptionField = new JTextField();

        JLabel taskCategoryLabel = new JLabel("Category:");
        JComboBox<String> taskCategoryField = new JComboBox<>(Arrays.copyOfRange(categories, 1, categories.length));

        JLabel taskPriorityLabel = new JLabel("Priority (1-5):");
        JComboBox<String> taskPriorityField = new JComboBox<>(Arrays.copyOfRange(priorities, 1, priorities.length));

        JButton addButton = new JButton("Add Task");

        inputPanel.add(taskTitleLabel);
        inputPanel.add(taskTitleField);

        inputPanel.add(taskDescriptionLabel);
        inputPanel.add(taskDescriptionField);

        inputPanel.add(taskCategoryLabel);
        inputPanel.add(taskCategoryField);

        inputPanel.add(taskPriorityLabel);
        inputPanel.add(taskPriorityField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // Initialize the filtered tasks list with all tasks
        applyFilters("");

        // Add Button Action Listener
        addButton.addActionListener(e -> {
            try {
                String title = taskTitleField.getText().trim();
                String description = taskDescriptionField.getText().trim();
                String category = (String) taskCategoryField.getSelectedItem();
                int priority = Integer.parseInt((String) taskPriorityField.getSelectedItem());

                if (title.isEmpty() || description.isEmpty()) {
                    throw new IllegalArgumentException("Title and description must be filled!");
                }

                Task task = new UserTask(title, description, category, priority);
                tasks.add(task);

                // Reapply current filters to update the list
                applyFilters(searchField.getText().trim().toLowerCase());

                // Clear fields
                taskTitleField.setText("");
                taskDescriptionField.setText("");
                taskCategoryField.setSelectedIndex(0);
                taskPriorityField.setSelectedIndex(0);

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Search Button Action Listener
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim().toLowerCase();
            applyFilters(query);
        });

        // Filter Action Listeners
        categoryFilter.addActionListener(e -> applyFilters(searchField.getText().trim().toLowerCase()));
        priorityFilter.addActionListener(e -> applyFilters(searchField.getText().trim().toLowerCase()));

        // Reset Filter Button Action Listener
        resetFilterButton.addActionListener(e -> {
            searchField.setText("");
            categoryFilter.setSelectedIndex(0);
            priorityFilter.setSelectedIndex(0);
            applyFilters("");
        });

        // Task List Selection Listener
        taskList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Prevent repeated events
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < filteredTasks.size()) {
                    Task selectedTask = filteredTasks.get(selectedIndex);
                    taskDescriptionArea.setText(selectedTask.getTaskDetails());
                }
            }
        });
    }

    // Method to apply filters and update the task list
    private void applyFilters(String searchQuery) {
        filteredTasks.clear();

        String selectedCategory = (String) categoryFilter.getSelectedItem();
        String selectedPriority = (String) priorityFilter.getSelectedItem();

        for (Task task : tasks) {
            boolean matchesSearch = searchQuery.isEmpty() || 
                    task.getTitle().toLowerCase().contains(searchQuery) || 
                    task.getDescription().toLowerCase().contains(searchQuery);

            boolean matchesCategory = selectedCategory.equals("All Categories") || 
                    task.getCategory().equals(selectedCategory);

            boolean matchesPriority = selectedPriority.equals("All Priorities") || 
                    task.getPriority() == Integer.parseInt(selectedPriority);

            if (matchesSearch && matchesCategory && matchesPriority) {
                filteredTasks.add(task);
            }
        }

        updateTaskList();

        if (filteredTasks.isEmpty() && !searchQuery.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tasks found matching your criteria.", 
                    "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Method to update the task list display
    private void updateTaskList() {
        taskListModel.clear();
        for (Task task : filteredTasks) {
            taskListModel.addElement(task.getTitle() + " (Priority: " + task.getPriority() + ")");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaskManagerApp app = new TaskManagerApp();
            app.setVisible(true);
        });
    }
}
