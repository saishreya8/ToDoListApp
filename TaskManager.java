import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks;
    private int nextId;
    private final String FILE_NAME = "tasks.dat";

    public TaskManager() {
        tasks = new ArrayList<>();
        nextId = 1;
        loadTasks();
    }

    public void addTask(String description) {
        Task task = new Task(nextId++, description);
        tasks.add(task);
        saveTasks();
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public void markTaskAsCompleted(int id) {
        Task task = findTaskById(id);
        if (task != null) {
            task.setCompleted(true);
            saveTasks();
        }
    }

    public void deleteTask(int id) {
        Task task = findTaskById(id);
        if (task != null) {
            tasks.remove(task);
            saveTasks();
        }
    }

    private Task findTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    private void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(tasks);
            oos.writeObject(nextId);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadTasks() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            tasks = (List<Task>) ois.readObject();
            nextId = (Integer) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }
}
