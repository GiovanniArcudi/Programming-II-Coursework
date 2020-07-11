package ga1g19;

import java.util.Stack;
import javafx.scene.control.Button;

// This class defines the undoLastChange/redoLastChange timeline, using two Stacks.
public class History {
    private Change currentChange;
    private Button undoButton;
    private Button redoButton;
    private Stack<Change> undoHistory;
    private Stack<Change> redoHistory;

    public History(Button undoButton, Button redoButton) {
        this.undoButton = undoButton;
        this.redoButton = redoButton;
        undoButton.setDisable(true);
        redoButton.setDisable(true);
        this.undoHistory = new Stack<>();
        this.redoHistory = new Stack<>();
    }

    // Adds a new change to the undoLastChange history.
    public void addNewChange(Change change) {
        if (change.getOldValue() != change.getNewValue()) {
            undoHistory.push(currentChange);
            undoButton.setDisable(false);
            currentChange = change;
            if (!redoHistory.empty()) {
                redoHistory.clear();
                redoButton.setDisable(true);
            }
        }
    }

    // Undoes the last change in the grid.
    public void undoLastChange() {
        if (!undoHistory.isEmpty()) {
            currentChange.getTile().setUnselected();
            currentChange.getTile().displayValue(currentChange.getOldValue());
            redoHistory.push(currentChange);
            redoButton.setDisable(false);
            currentChange = undoHistory.pop();

            if (undoHistory.isEmpty()) {
                undoButton.setDisable(true);
            }
        }
    }

    // Redoes the last change in the grid.
    public void redoLastChange() {
        if (!redoHistory.isEmpty()) {
            if (currentChange != null) {
                currentChange.getTile().setUnselected();
            }

            undoHistory.push(currentChange);
            undoButton.setDisable(false);
            currentChange = redoHistory.pop();
            currentChange.getTile().displayValue(currentChange.getNewValue());

            if (redoHistory.isEmpty()) {
                redoButton.setDisable(true);
            }
        }
    }

    // Current change accessor method.
    public Change getCurrentChange() {
        return currentChange;
    }

    // Removes all changes from history stacks.
    public void clear() {
        currentChange = null;
        undoHistory.clear();
        redoHistory.clear();
        undoButton.setDisable(true);
        redoButton.setDisable(true);
    }
}
