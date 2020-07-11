import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

// This class defines the mistakes checker.
public class MistakesChecker {
    private Grid grid;
    private boolean isActive;
    private boolean cagesAllCorrect;
    private boolean rowsAllCorrect;
    private boolean columnsAllCorrect;
    private ArrayList<String> coloursList;

    public MistakesChecker(Grid grid) {
        this.grid = grid;
        this.isActive = false;
        this.cagesAllCorrect = false;
        this.rowsAllCorrect = false;
        this.columnsAllCorrect = false;
        this.coloursList = new ArrayList<>();
        this.coloursList.add("orangered");
        this.coloursList.add("chartreuse");
        this.coloursList.add("blue");
        this.coloursList.add("lightpink");
        this.coloursList.add("magenta");
        this.coloursList.add("gold");
        this.coloursList.add("palevioletred");
        this.coloursList.add("deepskyblue");
        this.coloursList.add("mediumspringgreen");
        this.coloursList.add("darkorange");
    }

    // Sets the status of the mistake checker to the boolean value passed as parameter.
    public void setActive(boolean status) {
        this.isActive = status;
    }

    // Looks for errors in the grid (cages, rows and columns).
    public void checkGrid() {

        // Sets all grid to default.
        this.grid.setGridToDefault();

        // Checks that all complete cages are reaching the desired target.
        checkCages();

        /* Creates a descending ordered array of values from 1 to the size of the grid and hashes it.
           The produced hashcode will later be used to check that each row and column contains exactly one time each number
           from 1 to the size of the grid. */
        int[] expectedTileValuesArray = new int[grid.getGridSize()];
        for (int i = 0; i < grid.getGridSize(); i++) {
            expectedTileValuesArray[i] = i + 1;
        }
        int expectedHashCode = Arrays.hashCode(expectedTileValuesArray);

        // Checks that there are no number repetitions in the rows.
        checkRows(expectedHashCode);

        // Checks that there are no number repetitions in the columns.
        checkColumns(expectedHashCode);

        // If the mistakes checker is not active set the grid to default colour.
        if (!isActive) {
            this.grid.setGridToDefault();
        }

        // If no errors are found, notifies the user they have won.
        if (cagesAllCorrect && rowsAllCorrect && columnsAllCorrect) {
            this.grid.getSelectedTile().setUnselected();
            this.grid.getGui().getToppingVBox().getChildren().remove(grid.getControlsHBox());
            this.grid.getGui().getMasterPane().getChildren().remove(grid.getGui().getNumbersHBoxContainer());
            showWinningAnimation();
        }
    }

    // Checks that cage's tiles give the right target result.
    private void checkCages() {

        // Initializes cage errors counter.
        int cageErrorsCounter = 0;

        // Iterates over each cage of the grid and performs some error checking operations.
        for (Cage cage : grid.getCagesList()) {
            boolean cageError = false;

            // Creates a list containing all the values of all the tiles of the cage.
            ArrayList<Integer> cageTilesValues = new ArrayList<>();
            for (Tile tile : cage.getCageTilesList()) {
                cageTilesValues.add(tile.getValue());
            }

            if (isComplete(cageTilesValues)) {

                // Checks that cage's tiles give the right target result.
                int expectedTarget = cage.getTarget();
                int tempResult = 0;

                // If the cage is single-cell just check the cell's value, otherwise perform calculations with the operator.
                if (cage.getCageTilesList().size() == 1) {
                    tempResult = cage.getCageTilesList().get(0).getValue();

                    if (tempResult != expectedTarget) {
                        cageErrorsCounter++;
                        cageError = true;
                    }
                } else {
                    switch (cage.getArithmeticOperator()) {

                        // If the operator is +, adds all the tiles values and checks that the result is the expected one.
                        case "+":
                            for (Tile tile : cage.getCageTilesList()) {
                                tempResult += tile.getValue();
                            }
                            if (tempResult != expectedTarget) {
                                cageErrorsCounter++;
                                cageError = true;
                            }
                            break;

                        // If the operator is * or x, multiplies all the tiles values and checks that the result is the expected one.
                        case "*":
                        case "x":
                            tempResult = 1;
                            for (Tile tile : cage.getCageTilesList()) {
                                tempResult = tempResult * tile.getValue();
                            }
                            if (tempResult != expectedTarget) {
                                cageErrorsCounter++;
                                cageError = true;
                            }
                            break;

                        /* If the operator is -, takes the biggest value of the cage and subtracts all the other elements to it.
                           Then, it checks that the result is the expected one. */
                        case "-":

                            // Takes the biggest element and subtracts all other elements of the cage to it.
                            cageTilesValues.sort(Collections.reverseOrder());
                            tempResult = cageTilesValues.get(0) * 2;
                            for (int value : cageTilesValues) {
                                tempResult -= value;
                            }

                            // Checks that the result is correct.
                            if (tempResult != expectedTarget) {
                                cageErrorsCounter++;
                                cageError = true;
                            }
                            break;

                        /* If the operator is / or ÷, takes the biggest value of the cage and divide all the other elements to it.
                           Then, it checks that the result is the expected one. */
                        case "/":
                        case "÷":

                            // Takes the biggest element and divide all other elements of the cage to it.
                            cageTilesValues.sort(Collections.reverseOrder());
                            tempResult = cageTilesValues.get(0) * cageTilesValues.get(0);
                            for (int value : cageTilesValues) {
                                tempResult = tempResult / value;
                            }

                            // Checks that the result is correct.
                            if (tempResult != expectedTarget) {
                                cageErrorsCounter++;
                                cageError = true;
                            }
                            break;
                    }
                }

                // If an error has been found in the cage, set all its tiles to lightpink.
                if (cageError) {
                    for (Tile tile : cage.getCageTilesList()) {
                        tile.setUnselectedWithMistake();
                        if (tile == grid.getSelectedTile()) {
                            tile.setSelectedWithMistake();
                        }
                    }
                }
            } else {
                cageErrorsCounter++;
            }
        }

        // If no errors relative to cages have been found set the cagesAllCorrect flag to true.
        if (cageErrorsCounter == 0) {
            this.cagesAllCorrect = true;
        }
    }

    /* Check that each row contains exactly one time each number from 1 to the size of the grid by comparing
       the hashcode of the array of row values to the expected hashcode. */
    private void checkRows (int expectedHashCode)  {

        // Initializes rows errors counter.
        int rowsErrorsCounter = 0;

        for (Row row : grid.getRowsList()) {
            ArrayList<Tile> rowTiles = row.getTilesList();
            ArrayList<Integer> rowTilesValues = new ArrayList<>();

            for (Tile tile : rowTiles) {
                rowTilesValues.add(tile.getValue());
            }

            // If the row is complete, check it.
            rowsErrorsCounter = countErrors(expectedHashCode, rowsErrorsCounter, rowTiles, rowTilesValues);
        }

        // If no errors relative to rows have been found set the rowsAllCorrect flag to true.
        if (rowsErrorsCounter == 0) {
            this.rowsAllCorrect = true;
        }
    }

    /* Check that each column contains exactly one time each number from 1 to the size of the grid by comparing
       the hashcode of the array of column values to the expected hashcode. */
    private void checkColumns (int expectedHashCode) {

        // Initializes rows errors counter.
        int columnsErrorsCounter = 0;

        for (Column column : grid.getColumnsList()) {
            ArrayList<Tile> columnTiles = column.getTilesList();
            ArrayList<Integer> columnTilesValues = new ArrayList<>();

            for (Tile tile : columnTiles) {
                columnTilesValues.add(tile.getValue());
            }

            // If the row is complete, check it.
            columnsErrorsCounter = countErrors(expectedHashCode, columnsErrorsCounter, columnTiles, columnTilesValues);
        }

        // If no errors relative to columns have been found set the columnsAllCorrect flag to true.
        if (columnsErrorsCounter == 0) {
            this.columnsAllCorrect = true;
        }
    }

    private int countErrors(int expectedHashCode, int errorsCounter, ArrayList<Tile> tilesList, ArrayList<Integer> tilesValuesList) {
        if (isComplete(tilesValuesList)) {
            int[] columnValues = new int[grid.getGridSize()];
            for (int i = 0; i < grid.getGridSize(); i++) {
                columnValues[i] = tilesList.get(i).getValue();
            }
            Arrays.sort(columnValues);

            // If hash codes do not match, highlights the entire column in light pink.
            if (Arrays.hashCode(columnValues) != expectedHashCode) {
                for (Tile columnTile : tilesList) {
                    columnTile.setUnselectedWithMistake();
                    if (columnTile == grid.getSelectedTile()) {
                        columnTile.setSelectedWithMistake();
                    }
                }
                errorsCounter++;
            }
        } else {
            errorsCounter++;
        }
        return errorsCounter;
    }

    // Returns true if the list of values passed as parameter does not contain a zero, false otherwise.
    public boolean isComplete (ArrayList < Integer > valuesArrayList) {
        for (int i : valuesArrayList) {
            if (i == 0) {
                return false;
            }
        }
        return true;
    }

    // Shows the winning animation.
    public void showWinningAnimation() {
        // Hides the loading HBox and the.
        this.grid.getGui().getLoadingHBox().setVisible(false);


        // Colours all the tiles with random colours.
        Random randomizer = new Random();
        int randomInt = randomizer.nextInt(10);
        for (int j = 1; j < Math.pow(this.grid.getGridSize(), 2); j++) {
            this.grid.getTilesList().get(j - 1).setColor(coloursList.get(randomInt));

            if (randomInt == 9) {
                randomInt = 0;
            }
            this.grid.getTilesList().get(j).setColor(coloursList.get(randomInt + 1));
            randomInt++;
        }

        // Creates a list of Fade transitions, one for each tile.
        ArrayList<FadeTransition> fadeTransitionsList = new ArrayList<>((int) Math.pow(this.grid.getGridSize(), 2));
        for (int k = 0; k < this.grid.getTilesList().size(); k++) {
            fadeTransitionsList.add(new FadeTransition(Duration.millis(randomizer.nextInt(200) + 100), this.grid.getTilesList().get(k)));
            fadeTransitionsList.get(k).setFromValue(1);
            fadeTransitionsList.get(k).setToValue(0);
            fadeTransitionsList.get(k).setAutoReverse(true);
            fadeTransitionsList.get(k).setCycleCount(10);
        }

        // Creates a list of Rotation transitions, one for each tile.
        ArrayList<RotateTransition> rotateTransitionsList = new ArrayList<>((int) Math.pow(this.grid.getGridSize(), 2));
        for (int k = 0; k < this.grid.getTilesList().size(); k++) {
            rotateTransitionsList.add(new RotateTransition(Duration.millis(500), this.grid.getTilesList().get(k)));
            rotateTransitionsList.get(k).setAxis(Rotate.Z_AXIS);
            rotateTransitionsList.get(k).setByAngle(360);
            rotateTransitionsList.get(k).setAutoReverse(true);
            rotateTransitionsList.get(k).setCycleCount(7);
        }

        // Plays all the transitions in parallel.
        ParallelTransition parallelTransition = new ParallelTransition(fadeTransitionsList.get(0), rotateTransitionsList.get(0));
        for (int z = 1; z < this.grid.getTilesList().size(); z++) {
            parallelTransition.getChildren().addAll(fadeTransitionsList.get(z), rotateTransitionsList.get(z));
        }

        // Shows the winning popup once the animations have finished.
        parallelTransition.setOnFinished((ActionEvent event) -> {
            Alert winningPopUp = new Alert(Alert.AlertType.INFORMATION);
            winningPopUp.setTitle("You have won!");
            winningPopUp.setHeaderText("Congratulations!");
            winningPopUp.setContentText("You've won this MathDoku game!");
            winningPopUp.setOnCloseRequest((DialogEvent popupClosed) -> this.grid.getGui().getLoadingHBox().setVisible(true));
            winningPopUp.show();

            this.grid.getGui().getToppingVBox().getChildren().remove(this.grid.getControlsHBox());
            this.grid.getGui().getMasterPane().getChildren().remove(grid.getGridPane());
            this.grid.getGui().getNumbersHBoxContainer().setVisible(false);
            this.grid.getGui().getPrimaryStage().setMinHeight(200);
            this.grid.getGui().getPrimaryStage().setMinWidth(500);
        });

        // Plays the parallel transition.
        parallelTransition.play();
    }
}
