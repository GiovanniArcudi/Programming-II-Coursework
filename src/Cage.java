import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import java.util.ArrayList;

// This class defines a grid's cage
public class Cage {
    private Grid cageGrid;
    private ArrayList<Tile> cageTilesList;
    private String arithmeticOperator;
    private int target;
    private int cageFontSize;

    public Cage(Grid grid, int target, String arithmeticOperator, ArrayList<Tile> cageTilesList) {
        this.cageGrid = grid;
        this.arithmeticOperator = arithmeticOperator;
        this.target = target;
        this.cageTilesList = cageTilesList;

        for (Tile tile : cageTilesList) {
            tile.setCage(this);
        }
    }

    // Performs all the operations needed to display the cage.
    public void displayCage() {

        // Shows target and arithmetic operator of the cage.
        displayCageLabel();

        // Adds the right thickness to the borders each cageTile.
        for (Tile cageTile : cageTilesList) {
            String tileBordersStyle = "-fx-border-width: ";

            // UPPER BORDER
            if (cageTile.getGridPositionIndex() >= cageGrid.getGridSize()) {
                tileBordersStyle += refreshTileBorder(cageTile.getGridPositionIndex() - cageGrid.getGridSize());
            } else {
                tileBordersStyle += "4 ";
            }

            // RIGHT BORDER
            if ((cageTile.getGridPositionIndex() % cageGrid.getGridSize()) != 0) {
                tileBordersStyle += refreshTileBorder(cageTile.getGridPositionIndex() + 1);
            } else {
                tileBordersStyle += "4 ";
            }

            // DOWN BORDER
            if (cageTile.getGridPositionIndex() <= (Math.pow(cageGrid.getGridSize(), 2) - cageGrid.getGridSize())) {
                tileBordersStyle += refreshTileBorder(cageTile.getGridPositionIndex() + cageGrid.getGridSize());
            } else {
                tileBordersStyle += "4 ";
            }

            // LEFT BORDER
            if ((cageTile.getGridPositionIndex() - 1) % cageGrid.getGridSize() != 0) {
                tileBordersStyle += refreshTileBorder(cageTile.getGridPositionIndex() - 1);
            } else {
                tileBordersStyle += "4 ";
            }

            cageTile.changeBorder(tileBordersStyle);
        }
    }

    // Shows target and arithmetic operator on the cage.
    public void displayCageLabel() {
        Tile firstTile = cageTilesList.get(0);
        Label cageLabel;

        // Displays target and arithmetic operator on the top left tile of the cage.
        if (arithmeticOperator == null) {
            cageLabel = new Label(String.valueOf(target));
        } else {
            cageLabel = new Label(target + "," + arithmeticOperator);
        }
        cageLabel.setFont(new Font(cageFontSize));

        try {
            firstTile.getChildren().remove(1);
        } catch (Exception ignored) {}
        firstTile.getChildren().add(1, cageLabel);
        StackPane.setAlignment(firstTile.getChildren().get(1), Pos.TOP_LEFT);
    }

    /* Given a tile in the grid, it iterates over each tile in the cage and, if it is adjacent to the cage passed, it sets
    the border to be thin, otherwise sets the border to be thick. */
    public String refreshTileBorder(int gridPositionIndex) {
        for (Tile cageTile : cageTilesList) {
            if (cageTile.getGridPositionIndex() == gridPositionIndex) {
                return "1 ";
            }
        }
        return "4 ";
    }

    // Change cage's label fot size.
    public void setCageFontSize(int cageFontSize) {
        this.cageFontSize = cageFontSize;
    }

    // Returns cage's target.
    public int getTarget() {
        return target;
    }

    // Returns cage's arithmetic operator.
    public String getArithmeticOperator() {
        return arithmeticOperator;
    }

    // Returns the ArrayList containing all cage's tiles.
    public ArrayList<Tile> getCageTilesList() {
        return cageTilesList;
    }

    // Cage's grid accessor method.
    public Grid getCageGrid() {
        return cageGrid;
    }
}
