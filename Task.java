import java.io.Serializable;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private String description;
    private boolean isCompleted;
    private int id;

    public Task(int id, String description) {
        this.id = id;
        this.description = description;
        this.isCompleted = false;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String toString() {
        return id + ". [" + (isCompleted ? "X" : " ") + "] " + description;
    }
}
