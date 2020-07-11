package ga1g19;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

// This class defines the MathDoku grid.
public class Grid {
    private GUI gui;
    private int gridSize;
    private String fontOption;
    private int tilesFontSize;
    private int cagesFontSize;
    private GridPane gridPane;
    private HBox numbersHBox;
    private HBox controlsHBox;
    private ArrayList<Row> rowsList;
    private ArrayList<Column> columnsList;
    private ArrayList<Cage> cagesList;
    private ArrayList<Tile> tilesList;
    private Tile selectedTile;
    private MistakesChecker mistakesChecker;
    private History history;
    private Solver gridSolver;

    public Grid(GUI gui, GridPane gridPane, HBox numbersHBox, HBox controlsHBox, History history) {
        this.gui = gui;
        this.gridPane = gridPane;
        this.numbersHBox = numbersHBox;
        this.controlsHBox = controlsHBox;
        this.selectedTile = null;
        this.mistakesChecker = new MistakesChecker(this);
        this.history = history;
//        this.tilesFontSize = 22;
//        this.cagesFontSize = 12;
        this.fontOption = "Medium";
    }

    // Reads the input .txt file and creates cages accordingly.
    public void createGridFromFile(File instructionsFile) throws FileNotFoundException {
        if (instructionsFile != null) {
            ArrayList<String> cagesList = new ArrayList<>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(instructionsFile), StandardCharsets.UTF_8));
            String contentLine;

            // Reads one line at a time.
            try {
                contentLine = br.readLine();
                while (contentLine != null) {
                    cagesList.add(contentLine);
                    contentLine = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Creates the cages.
            createCages(cagesList);
        }
    }

    // Reads the text and creates cages accordingly.
    public void createGridFromText(String gameInstructionsText) {
        if (!gameInstructionsText.isEmpty()) {
            String[] cagesArray = gameInstructionsText.split("\\n");
            createCages(new ArrayList<>(Arrays.asList(cagesArray)));
        }
    }

    // Iterates over the arrayList of Strings (cages specifications) passed as parameter and creates cages accordingly.
    public void createCages(ArrayList<String> cagesList) {
        boolean inputFormatErrorFound = false;
        this.tilesList = new ArrayList<>();
        this.cagesList = new ArrayList<>();

        try {
            for (String cageString : cagesList) {

                // Stores cage's target, arithmetic operator (no operator if it's a single cell) and tiles definition.
                int blankSpaceIndex = cageString.indexOf(" ");
                String tilesDefinition = cageString.substring(blankSpaceIndex + 1);
                String cageOperation;
                String cageTarget;
                if (tilesDefinition.length() == 1 || tilesDefinition.length() == 2) {
                    cageTarget = cageString.substring(0, blankSpaceIndex);
                    cageOperation = null;
                } else {
                    cageTarget = cageString.substring(0, blankSpaceIndex - 1);
                    cageOperation = cageString.substring(blankSpaceIndex - 1, blankSpaceIndex);
                }
                int target = Integer.parseInt(cageTarget);

                // Creates a list of all the tiles contained in the cage.
                ArrayList<Tile> cageTilesList = new ArrayList<>();

                // Creates the tiles that compose the cage and adds them to the cageTilesList.
                for (String tilePosition : tilesDefinition.split(",")) {
                    Tile newTile = new Tile(Integer.parseInt(tilePosition));
                    newTile.updateBorder();

                    // Defines the behaviour of the tile when clicked.
                    newTile.setOnMouseClicked(mouseClick -> {
                        try {
                            // Sets the borders of the previously selected tile to default value and then selects the new tile.
                            selectedTile.updateBorder();
                            selectTile(newTile);
                            this.mistakesChecker.checkGrid();
                        } catch (NullPointerException e) {
                            selectTile(newTile);
                            this.mistakesChecker.checkGrid();
                        }
                    });

                    tilesList.add(newTile);
                    cageTilesList.add(newTile);
                }

                // If the cage is not a single-cell cage, checks if the cage operation is one of: + , * , x , - , / , ÷ .
                if (cageTilesList.size() != 1) {
                    if (cageOperation != null) {
                        checkCageOperator(cageOperation);
                    }
                }

                Cage newCage = new Cage(this, target, cageOperation, cageTilesList);
                newCage.setCageFontSize(cagesFontSize);
                this.cagesList.add(newCage);
            }
        } catch (Exception e) {

            inputFormatErrorFound = true;
            this.controlsHBox.setVisible(false);
            this.gui.getNumbersHBoxContainer().setVisible(false);
            this.gui.getPrimaryStage().setMinHeight(200);
            this.gui.getPrimaryStage().setMinWidth(500);

            // Notifies the user of the error.
            Alert impossibleSquareGridError = new Alert(Alert.AlertType.ERROR);
            impossibleSquareGridError.setTitle("INPUT ERROR!");
            impossibleSquareGridError.setHeaderText("Invalid input format.");
            impossibleSquareGridError.setContentText("A pre-defined puzzle must be given in the following text format:" + System.lineSeparator() +
                    System.lineSeparator() +
                    "•  Each line defines one cage of the puzzle." + System.lineSeparator() + System.lineSeparator() +
                    "•  The line starts with the target followed immediately by the arithmetic operator (or none if it's a single cell) for that cage." + System.lineSeparator() + System.lineSeparator() +
                    "•  This is followed by a space and a sequence of cell IDs that belong to the cage (consecutive cell IDs are separated by a comma). Here cells are numbered from 1 to (NxN), where 1 to N are the cells in the top row (left to right), N+1 to 2N are the cells in the second row from the top, and so on.");
            impossibleSquareGridError.showAndWait();
            resetGrid();
        }

        if (!inputFormatErrorFound) {
            try {

                // Checks that the input file is compliant with the restrictions imposed by the MathDoku game Which means:
                // 1.   The total number of defined cells (tiles) allows to create a square grid.
                // 2.   There are no repetitions in the definitions of tiles.
                // 3.   All the tiles indexes form a series from 1 to n squared (size of the grid squared).
                // 4.   Grid size is >= 2 and <= 8.
                checkSquarePossibility();
                checkTilesRepetitions();
                checkTilesIndexesCorrectness();
                checkSizeBoundaries();

                // If the grid respects the MathDoku constraints, it checks the cages and then creates and displays the grid.
                checkCages();
                buildGrid();
                showGrid();

            } catch (Exception e) {
                System.err.println(e.getMessage());
                this.controlsHBox.setVisible(false);
                this.gui.getNumbersHBoxContainer().setVisible(false);
                this.gui.getPrimaryStage().setMinHeight(200);
                this.gui.getPrimaryStage().setMinWidth(500);
            }
        }
    }

    // Checks that the total number of defined cells (tiles) allows to create a square grid.
    private void checkSquarePossibility() throws Exception {
        double squareRootOfTilesNumber = Math.sqrt(this.tilesList.size());
        if (squareRootOfTilesNumber - Math.floor(squareRootOfTilesNumber) != 0) {
            this.gui.getNumbersHBoxContainer().setVisible(false);
            this.gui.getPrimaryStage().setMinHeight(200);
            this.gui.getPrimaryStage().setMinWidth(500);

            // Notifies the user of the error.
            Alert impossibleSquareGridError = new Alert(Alert.AlertType.ERROR);
            impossibleSquareGridError.setTitle("INPUT ERROR!");
            impossibleSquareGridError.setHeaderText("In order to create a square grid, the total number of cells must be a perfect square!");
            impossibleSquareGridError.setContentText("Please double-check your input file.");
            impossibleSquareGridError.showAndWait();
            throw new Exception("The number of defined tiles does not allow the creation of a square grid.");
        }
    }

    // Checks that each position in the grid (tiles grid position indexes) has been defined only once.
    private void checkTilesRepetitions() throws Exception {

        for (Tile tile : tilesList) {
            int repetitionsCounter = 0;

            for (Tile tile1 : tilesList) {
                if (tile.getGridPositionIndex() == tile1.getGridPositionIndex()) {
                    repetitionsCounter++;
                }
            }

            if (repetitionsCounter > 1) {
                this.gui.getNumbersHBoxContainer().setVisible(false);
                this.gui.getPrimaryStage().setMinHeight(200);
                this.gui.getPrimaryStage().setMinWidth(500);

                // Notifies the user of the error.
                Alert tilesRepetitionError = new Alert(Alert.AlertType.ERROR);
                tilesRepetitionError.setTitle("INPUT ERROR!");
                tilesRepetitionError.setHeaderText("Each cell can only belong to one cage!");
                tilesRepetitionError.setContentText("Please double-check your input file and remove multiple cell usages.");
                tilesRepetitionError.showAndWait();
                throw new Exception("Each tile can belong to one single cage only.");
            }
        }
    }

    // Checks that all the tiles indexes form a series from 1 to n squared (no missing cells).
    private void checkTilesIndexesCorrectness() throws Exception {
        for (int gridPositionIndex = 1; gridPositionIndex <= this.tilesList.size(); gridPositionIndex++) {
            boolean positionDefined = false;

            for (Tile tile : this.tilesList) {
                if (gridPositionIndex == tile.getGridPositionIndex()) {
                    positionDefined = true;
                    break;
                }
            }

            if (!positionDefined) {
                this.gui.getNumbersHBoxContainer().setVisible(false);
                this.gui.getPrimaryStage().setMinHeight(200);
                this.gui.getPrimaryStage().setMinWidth(500);

                // Notifies the user of the error.
                Alert impossibleSquareGridError = new Alert(Alert.AlertType.ERROR);
                impossibleSquareGridError.setTitle("INPUT ERROR!");
                impossibleSquareGridError.setHeaderText("Cell indexes must go from 1 to N, without repetitions.");
                impossibleSquareGridError.setContentText("Please double-check your input file and check you cells' grid position definitions.");
                impossibleSquareGridError.showAndWait();
                throw new Exception("All the tiles indexes must form a series from 1 to n squared (size of the grid squared).");
            }
        }
    }

    // Checks that the grid's size is within the MathDoku boundaries.
    private void checkSizeBoundaries() throws Exception {

        // Computes the grid size.
        int size = 1;
        for (Tile tile : tilesList) {
            if (tile.getGridPositionIndex() > size) {
                size = tile.getGridPositionIndex();
            }
        }
        this.gridSize = (int) Math.sqrt(size);

        // If the grid does not respect the MathDoku size constraints, it notifies the user of the error.
        if (this.gridSize > 8 || this.gridSize < 2) {
            this.gui.getNumbersHBoxContainer().setVisible(false);
            this.gui.getPrimaryStage().setMinHeight(200);
            this.gui.getPrimaryStage().setMinWidth(500);

            Alert impossibleSquareGridError = new Alert(Alert.AlertType.ERROR);
            impossibleSquareGridError.setTitle("INPUT ERROR!");
            impossibleSquareGridError.setHeaderText("Invalid grid size. Should be between 2 and 8.");
            impossibleSquareGridError.setContentText("Please double-check your input file.");
            impossibleSquareGridError.showAndWait();
            throw new Exception("Invalid grid size. Should be >=2 and <= 8.");
        }
    }

    // Looks for errors with cages.
    public void checkCages() throws Exception {
        // Checks that the number of tiles defined is equal to the square of the grid size.
        checkTilesNumber();

        // Checks that singleton cages' target is between 1 and the sqrt of the size of the grid.
        checkSingletonCages();

        // Checks that all the tiles within a cage are adjacent.
        checkAdjacentTiles();
    }

    // Checks that the number of tiles defined is equal to the square of the grid size.
    private void checkTilesNumber() throws Exception {
        if (tilesList.size() != Math.pow(this.gridSize,2)) {
            this.gui.getNumbersHBoxContainer().setVisible(false);
            this.gui.getPrimaryStage().setMinHeight(200);
            this.gui.getPrimaryStage().setMinWidth(500);

            throw new Exception("You have not defined all the tiles in the grid!");
        }
    }

    // Looks for singleton cages, and makes sure that their target is between 1 and the sqrt of the size of the grid.
    private void checkSingletonCages() throws Exception {
        for (Cage cage : cagesList) {
            if (cage.getCageTilesList().size() == 1) {
                if (cage.getTarget() > this.gridSize || cage.getTarget() < 1) {
                    this.gui.getNumbersHBoxContainer().setVisible(false);
                    this.gui.getPrimaryStage().setMinHeight(200);
                    this.gui.getPrimaryStage().setMinWidth(500);

                    // Notifies the user of the error.
                    Alert singletonCagesError = new Alert(Alert.AlertType.ERROR);
                    singletonCagesError.setTitle("INPUT ERROR!");
                    singletonCagesError.setHeaderText("Single-cell cages' target must be within grid's size!");
                    singletonCagesError.setContentText("Please double-check your input file.");
                    singletonCagesError.showAndWait();
                    throw new Exception("A singleton cell's target can only be between 1 and the width of the grid.");
                }
            }
        }
    }

    // Controls the presence of tiles that are disconnected from the rest of the cage.
    private void checkAdjacentTiles() throws Exception {
        for (Cage cage : cagesList) {
            if (cage.getCageTilesList().size() > 1) {
                for (Tile tile : cage.getCageTilesList()) {
                    boolean adjacentTileFound = false;
                    int tilePositionIndex = tile.getGridPositionIndex();
                    int[] adjacentTilesIndexes = {tilePositionIndex + 1, tilePositionIndex - 1, tilePositionIndex + gridSize, tilePositionIndex - gridSize};

                    for (Tile cageTile : cage.getCageTilesList()) {
                        for (int position : adjacentTilesIndexes) {
                            if (cageTile.getGridPositionIndex() == position) {
                                adjacentTileFound = true;
                                break;
                            }
                        }
                    }
                    if (!adjacentTileFound) {
                        this.gui.getNumbersHBoxContainer().setVisible(false);
                        this.gui.getPrimaryStage().setMinHeight(200);
                        this.gui.getPrimaryStage().setMinWidth(500);

                        // Notifies the user if not all tiles within a cage are adjacent.
                        Alert adjacentTilesError = new Alert(Alert.AlertType.ERROR);
                        adjacentTilesError.setTitle("INPUT ERROR!");
                        adjacentTilesError.setHeaderText("All the tiles within a cage must be adjacent!");
                        adjacentTilesError.setContentText("Please double-check your input file.");
                        adjacentTilesError.showAndWait();
                        throw new Exception("All the tiles within a cage must be adjacent!");
                    }
                }
            }
        }
    }

    // Builds the grid on the master pane.
    public void buildGrid() {

        // Creates columns and rows and adds tiles to them.
        rowsList = new ArrayList<>();
        columnsList = new ArrayList<>();
        for (int k = 1; k <= gridSize; k++) {
            rowsList.add(new Row(new ArrayList<>()));
            columnsList.add(new Column(new ArrayList<>()));
        }
        for (Tile tile : tilesList) {
            tile.setRowIndex((int) Math.floor(((double) tile.getGridPositionIndex() - 1) / gridSize));
            tile.setColumnIndex((tile.getGridPositionIndex() - 1) % gridSize);
            rowsList.get(tile.getRowIndex()).add(tile);
            columnsList.get(tile.getColumnIndex()).add(tile);
        }

        // Adds numbers to the grid's numberBox.
        numbersHBox.getChildren().clear();
        for (int i = 1; i <= gridSize; i++) {
            Button numberButton = new Button(String.valueOf(i));
            numberButton.setFont(new Font(25));
            numberButton.setPrefSize(50,30);
            numbersHBox.getChildren().add(numberButton);

            // Sets the lambda expression for the number button.
            numberButton.setOnMouseClicked(mouseClick -> {
                try {
                    history.addNewChange(new Change(selectedTile, Integer.parseInt(numberButton.getText())));
                    selectedTile.displayValue(Integer.parseInt(numberButton.getText()));
                    mistakesChecker.checkGrid();
                } catch (NullPointerException noSelectedTile) {
                    System.out.println("No tile has been selected!");
                }
            });
        }

        // Adds delete button to the grid's numberBox.
        Button deleteButton = new Button("Delete");
        deleteButton.setFont(new Font(20));
        numbersHBox.getChildren().add(deleteButton);

        // Sets the lambda expression for the delete button.
        deleteButton.setOnMouseClicked(mouseClick -> {
            try {
                history.addNewChange(new Change(selectedTile, 0));
                selectedTile.displayValue(0);
                mistakesChecker.checkGrid();
            } catch (NullPointerException noSelectedTile) {
                System.out.println("No tile has been selected!");
            }
        });

        // Unlocks controls bar.
        controlsHBox.setVisible(true);
        controlsHBox.getChildren().get(2).setDisable(false);
        controlsHBox.getChildren().get(3).setDisable(false);
        controlsHBox.getChildren().get(4).setDisable(false);
        controlsHBox.getChildren().get(5).setDisable(false);
        controlsHBox.getChildren().get(6).setDisable(false);

        // Builds solver.
        this.gridSolver = new Solver(this);
    }

    // Shows the grid and cages on the pane.
    public void showGrid() {
        gridPane.getChildren().clear();
        history.clear();
        this.gui.getPrimaryStage().sizeToScene();
        this.gui.getNumbersHBoxContainer().setVisible(true);

        // Setting gridPane sizes.
        gridPane.setMinHeight(60 * gridSize);
        gridPane.setMinWidth(60 * gridSize);
        this.getGridPane().setMaxWidth(Math.min(gui.getMasterPane().getWidth(), gui.getMasterPane().getHeight() - 200 - 100));
        this.getGridPane().setMaxHeight(Math.min(gui.getMasterPane().getWidth(), gui.getMasterPane().getHeight() - 200 -100));

        // Sets the font size to default values.
        refreshFontSize();

        // Adds tiles to the gridPane, setting the initial size.
        for (Tile tile : tilesList) {
            double tileMinWidth = (this.getGridPane().getMinWidth() / this.getGridSize());
            double tileMinHeight = (this.getGridPane().getMinHeight() / this.getGridSize());
            double tileMaxWidth = (this.getGridPane().getMaxWidth() / this.getGridSize());
            double tileMaxHeight = (this.getGridPane().getMaxHeight() / this.getGridSize());
            tile.setMinSize(Math.max(tileMinWidth, tileMaxWidth), Math.max(tileMinHeight, tileMaxHeight));
            gridPane.add(tile, tile.getColumnIndex(), tile.getRowIndex());
            GridPane.setHgrow(tile, Priority.ALWAYS);
            GridPane.setVgrow(tile, Priority.ALWAYS);
        }

        // Sets the font size to default values.
        refreshFontSize();

        // Displays the cages.
        for (Cage cage : cagesList) {
            cage.displayCage();
        }

        // Sets primary stage size to be fitted by the master pane.
        this.gui.getPrimaryStage().setMaximized(false);
        this.gui.getPrimaryStage().sizeToScene();
        this.gui.getPrimaryStage().setMinHeight(this.gui.getPrimaryStage().getHeight());
        this.gui.getPrimaryStage().setMinWidth(this.gui.getPrimaryStage().getWidth());
        this.gui.getPrimaryStage().setResizable(true);
        this.gui.getPrimaryStage().show();
    }

    // Sets every element of the grid to its default setting.
    public void resetGrid() {
        this.gridSize = 0;
        this.tilesList = new ArrayList<>();
        this.cagesList = new ArrayList<>();
        this.columnsList = new ArrayList<>();
        this.rowsList = new ArrayList<>();
        numbersHBox.getChildren().clear();
        gridPane.getChildren().clear();
        history.clear();
    }

    // Defines the action of selecting a tile on the grid.
    public void selectTile(Tile tile) {
        // If the previously selected tile is not null, sets it to default appearance.
        if (getSelectedTile() != null) {
            getSelectedTile().setUnselected();
        }

        this.selectedTile = tile;

        if (tile != null) {
            tile.setSelected();
        }
    }

    // Controls that the arithmetic operators are the accepted ones.
    public void checkCageOperator(String cageOperator) throws Exception {
        if (!cageOperator.equals("+") && !cageOperator.equals("x") && !cageOperator.equals("*") && !cageOperator.equals("-") && !cageOperator.equals("÷") && !cageOperator.equals("/")) {
            this.gui.getNumbersHBoxContainer().setVisible(false);
            this.gui.getPrimaryStage().setMinHeight(200);
            this.gui.getPrimaryStage().setMinWidth(500);

            throw new Exception("Only + , * , x , - , / , ÷ are accepted arithmetic operators.");
        }
    }

    // Grid's GUI accessor method.
    public GUI getGui() {
        return gui;
    }

    // Grid's GridPane accessor method.
    public GridPane getGridPane() {
        return gridPane;
    }

    // Grid's size accessor method.
    public int getGridSize() {
        return gridSize;
    }

    // Grid's selected tile accessor method.
    public Tile getSelectedTile() {
        return selectedTile;
    }

    // Grid's rows list accessor method.
    public ArrayList<Row> getRowsList() {
        return rowsList;
    }

    // Grid's column list accessor method.
    public ArrayList<Column> getColumnsList() {
        return columnsList;
    }

    // Grid's cages list accessor method.
    public ArrayList<Cage> getCagesList() {
        return cagesList;
    }

    // Grid's tiles list accessor method.
    public ArrayList<Tile> getTilesList() {
        return tilesList;
    }

    // Grid's mistake checker accessor method.
    public MistakesChecker getMistakesChecker() {
        return mistakesChecker;
    }

    // Grid's history accessor method.
    public History getHistory() {
        return history;
    }

    // Grid's solver accessor method.
    public Solver getGridSolver() {
        return gridSolver;
    }

    // Controls box accessor method.
    public HBox getControlsHBox() {
        return controlsHBox;
    }

    // Changes the font size of the grid's components (from choice box).
    public void setFontSize(String fontSize) {

        // Stores the user option.
        this.fontOption = fontSize;

        // Refreshes font sizes.
        refreshFontSize();
    }

    public void setGridToDefault() {
        for (Tile tile : this.getTilesList()) {
            tile.setUnselected();
        }

        if (this.getSelectedTile() != null) {
            getSelectedTile().setSelected();
        }
    }

    // Changes the font size of the grid's components according to the size of the Tile.
    public void refreshFontSize() {

        // Gets the height of a tile.
        double tileSize = getTilesList().get(0).getHeight();

        // Sets tiles' font size.
        if (fontOption != null && tileSize != 0) {
            if (fontOption.equals("Large")) {
                this.tilesFontSize = (int) (tileSize / 2.4);
            } else if (fontOption.equals("Small")) {
                this.tilesFontSize = (int) (tileSize / 3);
            } else {
                this.tilesFontSize = (int) (tileSize / 2.7);
            }
        } else {
            this.tilesFontSize = 22;
        }

        // Sets cages' font size.
        if (fontOption != null && tileSize != 0) {
            if (fontOption.equals("Large")) {
                this.cagesFontSize = (int) (tileSize / 4.3);
            } else if (fontOption.equals("Small")) {
                this.cagesFontSize = (int) (tileSize / 5.7);
            } else {
                this.cagesFontSize = (int) (tileSize / 5);
            }
        } else {
            this.tilesFontSize = 12;
        }

        // Updates tiles font sizes.
        try {
            for (Cage cage : cagesList) {
                for (Tile tile : cage.getCageTilesList()) {
                    tile.setTileFontSize(tilesFontSize);
                    tile.displayValue(tile.getValue());
                }
                cage.setCageFontSize(cagesFontSize);
                cage.displayCage();
            }

            this.getMistakesChecker().checkGrid();

        } catch (Exception ignore) { }
    }
}
