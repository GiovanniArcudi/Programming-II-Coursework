package ga1g19;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.util.Duration;

// This class define a grid's tile.
public class Tile extends StackPane implements Comparable<Tile> {
    private Cage cage;
    private int gridPositionIndex;
    private int rowIndex;
    private int columnIndex;
    private int value;
    private Label valueLabel;
    private int fontSize;
    private String border;
    private String tileStyle;
    private boolean hinting = false;
    private boolean isSelected = false;

    public Tile(int gridPositionIndex) {
        this.gridPositionIndex = gridPositionIndex;
        this.value = 0;
        this.getChildren().add(0, new Label(""));
        this.tileStyle = "-fx-border-color: black; ";
    }

    // To give the effect of selection, sets tile's border to be red.
    public void setSelected() {
        this.isSelected = true;
        if (!hinting) {
            this.setStyle("-fx-border-color: red; -fx-border-width: 3; ");
        } else {
            this.setStyle("-fx-border-color: red; -fx-border-width: 3; -fx-background-color: palegreen; ");
        }
    }

    // When a tile containing a wrong value is selected.
    public void setSelectedWithMistake() {
        this.isSelected = true;
        if (!hinting) {
            this.setStyle("-fx-border-color: red; -fx-border-width: 3; -fx-background-color: lightpink; ");
        } else {
            this.setStyle("-fx-border-color: red; -fx-border-width: 3; -fx-background-color: palegreen; ");
        }
    }

    // The default appearance for a non-selected/non-mistaken tile.
    public void setUnselected() {
        this.isSelected = false;
        if (!hinting) {
            this.tileStyle = "-fx-border-color: black; ";
        } else {
            this.tileStyle = "-fx-border-color: black; -fx-background-color: palegreen; ";
        }
        updateBorder();
    }

    // When a tile is not selected but contains a mistake.
    public void setUnselectedWithMistake() {
        this.isSelected = false;
        if (!hinting) {
            this.tileStyle = "-fx-border-color: black; -fx-background-color: lightpink; ";
        } else {
            this.tileStyle = "-fx-border-color: black; -fx-background-color: palegreen; ";
        }
        updateBorder();
    }

    // Sets the border of the tile.
    public void updateBorder() {
        if (border == null) {
            this.setStyle(tileStyle);
        } else {
            this.setStyle(tileStyle + border);
        }
    }

    // Modifies the border of the tile (mainly used for cage effect).
    public void changeBorder(String border) {
        this.border = border;
        updateBorder();
    }

    // Shows the value passed as parameter as hint.
    public void showHint(int hintValue) {

        // Sets the hinting flag to true;
        this.hinting = true;

        // Disables the hint button.
        this.getCage().getCageGrid().getControlsHBox().getChildren().get(3).setDisable(true);

        // Stores the actual displayed value.
        int pastValue = this.getValue();

        // Colors the tile in green.
        if (this.isSelected) {
            this.setStyle("-fx-border-color: red; -fx-border-width: 3; -fx-background-color: palegreen; ");
        } else {
            this.tileStyle = "-fx-border-color: black; -fx-background-color: palegreen; ";
            updateBorder();
        }

        // Shows the hint value.
        getChildren().remove(0);
        this.valueLabel = new Label();
        if (hintValue != 0) {
            valueLabel.setText(String.valueOf(hintValue));
        } else {
            valueLabel.setText("");
        }
        valueLabel.setFont(new Font(fontSize));
        getChildren().add(0, valueLabel);

        // Waits for 1 second and then restores the tile to the previous status and reactivates the hint button on.
        Timeline oneSecondWonder = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            displayValue(pastValue);
            this.getCage().getCageGrid().getControlsHBox().getChildren().get(3).setDisable(false);
            this.hinting = false;
            this.getCage().getCageGrid().getMistakesChecker().checkGrid();
        }));
        oneSecondWonder.setCycleCount(1);
        oneSecondWonder.play();
    }

    // Returns the position of the tile in the grid (gridPositionIndex).
    public int getGridPositionIndex() {
        return gridPositionIndex;
    }

    // Returns the value of the tile (value).
    public int getValue() {
        return value;
    }

    // Lets the tile display the value passed as parameter (if the value is 0, tile is blank).
    public void displayValue(int value) {
        this.value = value;
        getChildren().remove(0);
        this.valueLabel = new Label();

        if (value != 0) {
            valueLabel.setText(String.valueOf(value));
        } else {
            valueLabel.setText("");
        }

        valueLabel.setFont(new Font(fontSize));
        getChildren().add(0, valueLabel);
    }

    // Sets the color of the tile (used in the winning animation).
    public void setColor(String color) {
        this.tileStyle = "-fx-border-color: black; -fx-background-color: " + color + "; ";
        updateBorder();
    }

    // Sets the cage to which the tile belongs.
    public void setCage(Cage cage) {
        this.cage = cage;
    }

    // Tile's cage accessor method.
    public Cage getCage() {
        return this.cage;
    }

    // Sets the font size for the tile.
    public void setTileFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    // Sets the index value of the tile's raw.
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    // Sets the index value of the tile's column.
    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    // Returns the index value of the tile's raw.
    public int getRowIndex() {
        return rowIndex;
    }

    // Returns the index value of the tile's column.
    public int getColumnIndex() {
        return columnIndex;
    }

    // Compares two tiles'values.
    @Override
    public int compareTo(Tile tile) {
        return Integer.compare(getValue(), tile.getValue());
    }
}
