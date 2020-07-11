import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GUI extends Application {
    private Stage primaryStage;
    private BorderPane masterPane;
    private Grid grid;
    private Button loadFromFileButton;
    private Button loadFromTextButton;
    private HBox loadingHBox;
    private VBox toppingVBox;
    private VBox numbersHBoxContainer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Creates the master pane.
        this.masterPane = new BorderPane();

        // Creates a the title of the game and puts it in an HBox.
        Label title = new Label("MATHDOKU!");
        title.setFont(new Font("Ink Free",50));
        title.setAlignment(Pos.CENTER);
        HBox titleHBox = new HBox();
        titleHBox.setAlignment(Pos.CENTER);
        titleHBox.getChildren().add(title);

        // Creates the Load from file and Load from text buttons and puts it in an HBox.
        this.loadFromFileButton = new Button("Load game from File");
        this.loadFromTextButton = new Button("Load game from Text");
        loadFromFileButton.setAlignment(Pos.CENTER);
        loadFromTextButton.setAlignment(Pos.CENTER);
        this.loadingHBox = new HBox();
        loadingHBox.setAlignment(Pos.CENTER);
        loadingHBox.setPadding(new Insets(0, 0, 24, 0));
        loadingHBox.setSpacing(20);
        loadingHBox.getChildren().addAll(loadFromFileButton,loadFromTextButton);

        // Creates a topping VBox to hold the two title and loading buttons boxes and adds it to the masterPane.
        this.toppingVBox = new VBox();
        toppingVBox.setPadding(new Insets(0, 0, 12, 0));
        toppingVBox.setAlignment(Pos.CENTER);
        toppingVBox.setFillWidth(true);
        toppingVBox.getChildren().addAll(titleHBox, loadingHBox);
        toppingVBox.setMinHeight(200);
        toppingVBox.setMaxHeight(200);
        this.masterPane.setTop(toppingVBox);

        // Sets the behaviour of the "Load game from File" button (loadFromFileButton).
        loadFromFileButton.setOnAction(actionEvent -> {

            // Opens a FileChooser window.
            FileChooser fc = new FileChooser();
            fc.setTitle("Select Game File");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            // Allows user to select the file while blocking the stage.
            File selectedFile = fc.showOpenDialog(primaryStage);

            if (selectedFile != null) {
                newGame();

                // Creates a new grid on the indications of the file.
                try {
                    this.grid.createGridFromFile(selectedFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Sets the behaviour of the "Load game from Text" button (loadFromTextButton).
        loadFromTextButton.setOnAction(actionEvent -> {

            // Opens a dialog in a new window.
            Dialog<ButtonType> loadFromTextDialog = new Dialog<>();
            loadFromTextDialog.setTitle("Load game from Text");
            loadFromTextDialog.setHeaderText("Please enter the game specifications in the text area below.");

            // Adds OK and Cancel to the window.
            loadFromTextDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Creates the gridPane used by the dialog.
            GridPane dialogGridPane = new GridPane();
            dialogGridPane.setPadding(new Insets(12, 12, 12, 12));
            dialogGridPane.setAlignment(Pos.CENTER);

            // Creates a text area and adds it to the dialogGridPane.
            TextArea gameTextArea = new TextArea();
            dialogGridPane.getChildren().add(gameTextArea);
            loadFromTextDialog.getDialogPane().setContent(dialogGridPane);

            // If the OK button is pressed, it reads the text.
            loadFromTextDialog.showAndWait().filter(answer -> answer == ButtonType.OK).ifPresent(answer -> {
                newGame();
                grid.createGridFromText(gameTextArea.getText());
            });
        });

        // Activates keyboard sensing.
        activateKeyBoard();

        // GUI Settings.
        Scene scene = new Scene(masterPane);
        primaryStage.setTitle("Mathdoku!");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(300);
        primaryStage.sizeToScene();
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    // Creates a new game.
    public void newGame() {

        // Creates the controls bar.
        HBox controlsHBox = new HBox(6);
        controlsHBox.setPadding(new Insets(12, 6, 12, 6));
        controlsHBox.setStyle("-fx-border-color: black; -fx-border-width: 1 0 1 0; -fx-background-color: darkgray; ");
        controlsHBox.setAlignment(Pos.CENTER);

        // Creates the numbers bar.
        this.numbersHBoxContainer = new VBox();
        numbersHBoxContainer.setPadding(new Insets(15, 0, 0, 0));
        numbersHBoxContainer.setAlignment(Pos.CENTER);
        numbersHBoxContainer.setFillWidth(true);
        numbersHBoxContainer.setMinHeight(100);
        numbersHBoxContainer.setMaxHeight(100);
        HBox numbersHBox = new HBox(6);
        numbersHBox.setPadding(new Insets(12, 6, 12, 6));
        numbersHBox.setStyle("-fx-border-color: black; -fx-border-width: 1 0 1 0; -fx-background-color: darkgray; ");
        numbersHBox.setMinHeight(85);
        numbersHBox.setMaxHeight(85);
        numbersHBox.setAlignment(Pos.CENTER);
        numbersHBoxContainer.getChildren().add(numbersHBox);
        this.masterPane.setBottom(numbersHBoxContainer);
        BorderPane.setAlignment(masterPane.getBottom(), Pos.BOTTOM_CENTER);

        // Adds tools to the controls bar.
        Button undoButton = new Button("Undo");
        Button redoButton = new Button("Redo");
        Button clearButton = new Button("Clear Grid");
        Button hintButton = new Button("Hint");
        clearButton.setDisable(true);
        Label mistakesCheckerLabel = new Label("Click to check the grid:");
        CheckBox mistakesCheckerCheckBox = new CheckBox();
        mistakesCheckerLabel.setDisable(true);
        mistakesCheckerCheckBox.setDisable(true);
        ChoiceBox<String> fontSizeSelector = new ChoiceBox<>();
        fontSizeSelector.getItems().addAll("Small", "Medium", "Large");
        fontSizeSelector.getSelectionModel().select(1);
        fontSizeSelector.setDisable(true);
        controlsHBox.getChildren().addAll(undoButton, redoButton, clearButton, hintButton, mistakesCheckerLabel, mistakesCheckerCheckBox, fontSizeSelector);
        controlsHBox.setVisible(false);
        this.toppingVBox.getChildren().add(controlsHBox);
        this.masterPane.setTop(toppingVBox);
        BorderPane.setAlignment(masterPane.getTop(), Pos.TOP_CENTER);

        // Generates a grid pane.
        GridPane gridPane = new GridPane();

        // Sets gridPane Max Width and Min Width (toppingVBox is always 200 and numbersHBoxContainer is always 100).
        gridPane.setMaxHeight(Math.min(masterPane.getWidth(), masterPane.getHeight() - 200 - 100));
        gridPane.setMaxWidth(Math.min(masterPane.getWidth(), masterPane.getHeight() - 200 - 100));
        gridPane.setVgap(0);
        gridPane.setHgap(0);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setSnapToPixel(false);

        // Generates a grid object.
        this.grid = new Grid(this, gridPane, numbersHBox, controlsHBox, new History(undoButton, redoButton));
        grid.getGridPane().setAlignment(Pos.CENTER);

        // Width listener
        masterPane.widthProperty().addListener((obs, oldVal, newVal) -> refreshGridSizes());

        // Height listener.
        masterPane.heightProperty().addListener((obs, oldVal, newVal) -> refreshGridSizes());


        // Sets the behaviour of the "Load game from File" button (loadFromFileButton).
        loadFromFileButton.setOnAction(actionEvent -> {

            // Opens a FileChooser window.
            FileChooser fc = new FileChooser();
            fc.setTitle("Select Game File");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            // Allows user to select the file while blocking the stage.
            File selectedFile = fc.showOpenDialog(primaryStage);

            if (selectedFile != null) {
                toppingVBox.getChildren().remove(controlsHBox);
                this.masterPane.getChildren().remove(grid.getGridPane());
                this.masterPane.getChildren().remove(numbersHBoxContainer);
                newGame();

                // Creates a new grid on the indications of the file.
                try {
                    this.grid.createGridFromFile(selectedFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Sets the behaviour of the "Load game from Text" button (loadFromTextButton).
        loadFromTextButton.setOnAction(actionEvent -> {

            // Opens a dialog in a new window.
            Dialog<ButtonType> loadFromTextDialog = new Dialog<>();
            loadFromTextDialog.setTitle("Load game from Text");
            loadFromTextDialog.setHeaderText("Please enter the game specifications in the text area below.");

            // Adds OK and Cancel to the window.
            loadFromTextDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Creates the gridPane used by the dialog.
            GridPane dialogGridPane = new GridPane();
            dialogGridPane.setPadding(new Insets(12, 12, 12, 12));
            dialogGridPane.setAlignment(Pos.CENTER);

            // Creates a text area and adds it to the dialogGridPane.
            TextArea gameTextArea = new TextArea();
            dialogGridPane.getChildren().add(gameTextArea);
            loadFromTextDialog.getDialogPane().setContent(dialogGridPane);

            // If the OK button is pressed, it reads the text.
            loadFromTextDialog.showAndWait().filter(answer -> answer == ButtonType.OK).ifPresent(answer -> {
                toppingVBox.getChildren().remove(controlsHBox);
                this.masterPane.getChildren().remove(grid.getGridPane());
                this.masterPane.getChildren().remove(numbersHBoxContainer);
                newGame();
                grid.createGridFromText(gameTextArea.getText());
            });
        });

        // Defines the behaviour of the undoLastChange button when clicked.
        undoButton.setOnMouseClicked(mouseClick -> {
            grid.getHistory().undoLastChange();
            if (grid.getHistory().getCurrentChange() != null) {
                grid.selectTile(grid.getHistory().getCurrentChange().getTile());
                grid.getMistakesChecker().checkGrid();
            } else {
                grid.selectTile(null);
            }
        });

        // Defines the behaviour of the redoLastChange button when clicked.
        redoButton.setOnMouseClicked(mouseClick -> {
            grid.getHistory().redoLastChange();
            if (grid.getHistory().getCurrentChange() != null) {
                grid.selectTile(grid.getHistory().getCurrentChange().getTile());
                grid.getMistakesChecker().checkGrid();
            } else {
                grid.selectTile(null);
            }
        });

        // Sets the behaviour of the "Clear Grid" button (clearButton).
        clearButton.setOnAction(buttonClicked -> {

            // Asks the user for confirmation.
            Alert clearGridAlert = new Alert(Alert.AlertType.WARNING);
            clearGridAlert.setTitle("Clear Grid");
            clearGridAlert.setHeaderText("This operation will clear all the entries in the grid.");
            clearGridAlert.setContentText("Do you want to proceed?");

            // If yes, clears all the entries in the grid and the history.
            clearGridAlert.showAndWait().filter(answer -> answer == ButtonType.OK)
                    .ifPresent(answer -> {
                        grid.selectTile(null);
                        grid.setGridToDefault();
                        for (Tile tile : grid.getTilesList()) {
                            tile.displayValue(0);
                        }
                        grid.getHistory().clear();
                    });
        });

        // Sets the lambda expression for the hintButton.
        hintButton.setOnAction(buttonClicked -> {

            // Creates an ArrayLIst of tiles whose input does not correspond with the solution one.
            ArrayList<Tile> wrongTiles = new ArrayList<>();
            for (Tile tile : this.grid.getTilesList()) {
                if (tile.getValue() != this.grid.getGridSolver().getSolution()[tile.getGridPositionIndex() - 1]) {
                    wrongTiles.add(tile);
                }
            }

            // Selects a random tile and displays the hint value.
            if (wrongTiles.size() == 1) {
                wrongTiles.get(0).showHint(this.grid.getGridSolver().getSolution()[wrongTiles.get(0).getGridPositionIndex() - 1]);
            } else {
                int randomInt = ThreadLocalRandom.current().nextInt(0, wrongTiles.size());
                wrongTiles.get(randomInt).showHint(this.grid.getGridSolver().getSolution()[wrongTiles.get(randomInt).getGridPositionIndex() - 1]);
            }
        });

        // Sets the lambda expression for the mistakesCheckerCheckBox checkbox.
        mistakesCheckerCheckBox.setOnMouseClicked(mouseClick -> {
            grid.getMistakesChecker().setActive(mistakesCheckerCheckBox.isSelected());
            grid.getMistakesChecker().checkGrid();
        });

        // Sets the lambda expression for the fontSizeSelector ChoiceBox.
        fontSizeSelector.getSelectionModel().selectedItemProperty().addListener((font, previousFont, updatedFont) -> grid.setFontSize(updatedFont));

        // Adds the components to the masterPane.
        this.masterPane.setTop(toppingVBox);
        this.masterPane.setCenter(gridPane);
        this.masterPane.setBottom(numbersHBoxContainer);
        BorderPane.setAlignment(masterPane.getTop(), Pos.TOP_CENTER);
        BorderPane.setAlignment(masterPane.getCenter(), Pos.CENTER);
        BorderPane.setAlignment(masterPane.getBottom(), Pos.BOTTOM_CENTER);
    }

    // Activates the keyboard.
    public void activateKeyBoard() {
        // Sets the behavior of the masterPane when a key is pressed.
        masterPane.setOnKeyPressed(keyPressed -> {

            // Perform the tasks only if a tile has been selected.
            if (grid.getSelectedTile() != null) {

                // Stores the numerical value of the key pressed.
                KeyCode keyPressedCode = keyPressed.getCode();

                // Updates the number that is shown on the tile, or deletes it.
                try {
                    switch (keyPressedCode) {
                        case W:
                            // Sets the borders of the previously selected tile to default value.
                            this.grid.getSelectedTile().updateBorder();

                            // Compute the index of the upper tile and, if possible, selects it.
                            int upperTileIndex = this.grid.getSelectedTile().getGridPositionIndex() - this.grid.getGridSize();
                            if (upperTileIndex > 0) {
                                for (Tile tile : this.grid.getTilesList()) {
                                    if (tile.getGridPositionIndex() == upperTileIndex) {
                                        this.grid.selectTile(tile);
                                        break;
                                    }
                                }
                            }
                            this.grid.getMistakesChecker().checkGrid();
                            break;

                        case S:
                            // Sets the borders of the previously selected tile to default value.
                            this.grid.getSelectedTile().updateBorder();

                            // Compute the index of the downer tile and, if possible, selects it.
                            int downerTileIndex = this.grid.getSelectedTile().getGridPositionIndex() + this.grid.getGridSize();
                            if (downerTileIndex <= Math.pow(this.grid.getGridSize(), 2)) {
                                for (Tile tile : this.grid.getTilesList()) {
                                    if (tile.getGridPositionIndex() == downerTileIndex) {
                                        this.grid.selectTile(tile);
                                        break;
                                    }
                                }
                            }
                            this.grid.getMistakesChecker().checkGrid();
                            break;

                        case D:
                            // Sets the borders of the previously selected tile to default value.
                            this.grid.getSelectedTile().updateBorder();

                            // Compute the index of the right tile and, if possible, selects it.
                            int rightTileIndex = this.grid.getSelectedTile().getGridPositionIndex() + 1;
                            if ((rightTileIndex - 1) % this.grid.getGridSize() != 0) {
                                for (Tile tile : this.grid.getTilesList()) {
                                    if (tile.getGridPositionIndex() == rightTileIndex) {
                                        this.grid.selectTile(tile);
                                        break;
                                    }
                                }
                            }
                            this.grid.getMistakesChecker().checkGrid();
                            break;

                        case A:
                            // Sets the borders of the previously selected tile to default value.
                            this.grid.getSelectedTile().updateBorder();

                            // Compute the index of the left tile and, if possible, selects it.
                            int leftTileIndex = this.grid.getSelectedTile().getGridPositionIndex() - 1;
                            if (leftTileIndex % this.grid.getGridSize() != 0) {
                                for (Tile tile : this.grid.getTilesList()) {
                                    if (tile.getGridPositionIndex() == leftTileIndex) {
                                        this.grid.selectTile(tile);
                                        break;
                                    }
                                }
                            }
                            this.grid.getMistakesChecker().checkGrid();
                            break;

                        case DIGIT1:
                        case DIGIT2:
                        case DIGIT3:
                        case DIGIT4:
                        case DIGIT5:
                        case DIGIT6:
                        case DIGIT7:
                        case DIGIT8:
                            if (keyPressedCode.getCode() - 48 <= grid.getGridSize()) {
                                grid.getHistory().addNewChange(new Change(grid.getSelectedTile(), keyPressedCode.getCode() - 48));
                                grid.getSelectedTile().displayValue(keyPressedCode.getCode() - 48);
                                grid.getMistakesChecker().checkGrid();
                            }
                            break;

                        case NUMPAD1:
                        case NUMPAD2:
                        case NUMPAD3:
                        case NUMPAD4:
                        case NUMPAD5:
                        case NUMPAD6:
                        case NUMPAD7:
                        case NUMPAD8:
                            if (keyPressedCode.getCode() - 96 <= grid.getGridSize()) {
                                grid.getHistory().addNewChange(new Change(grid.getSelectedTile(), keyPressedCode.getCode() - 96));
                                grid.getSelectedTile().displayValue(keyPressedCode.getCode() - 96);
                                grid.getMistakesChecker().checkGrid();
                            }
                            break;

                        case BACK_SPACE:
                        case DELETE:
                            grid.getHistory().addNewChange(new Change(grid.getSelectedTile(), 0));
                            grid.getSelectedTile().displayValue(0);
                            grid.getMistakesChecker().checkGrid();
                            break;
                    }
                } catch (NullPointerException e) {
                    System.err.println(e.getMessage());
                    System.out.println("No tile selected!");
                }
            }
        });
    }

    // Updates grid size and font size according to the size of the window.
    public void refreshGridSizes() {

        // Changes gridPane size.
        this.grid.getGridPane().setMaxHeight(Math.min(masterPane.getWidth(), masterPane.getHeight() - toppingVBox.getHeight() - numbersHBoxContainer.getHeight()));
        this.grid.getGridPane().setMaxWidth(Math.min(masterPane.getWidth(), masterPane.getHeight() - toppingVBox.getHeight() - numbersHBoxContainer.getHeight()));

        // Changes tiles size.
        if (this.grid != null) {
            if (this.grid.getTilesList() != null) {
                for (Tile tile : this.grid.getTilesList()) {
                    tile.setMinSize((this.grid.getGridPane().getWidth() / this.grid.getGridSize()), (this.grid.getGridPane().getHeight() / this.grid.getGridSize()));
                }

                // Changes grid font size.
                this.grid.refreshFontSize();
            }
        }
    }

    // GUI master pane accessor method.
    public BorderPane getMasterPane() {
        return masterPane;
    }

    // GUI primary stage accessor method.
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    // Gui topping VBox accessor method.
    public VBox getToppingVBox() {
        return toppingVBox;
    }

    // GUI loading HBox accessor method.
    public HBox getLoadingHBox() {
        return loadingHBox;
    }

    // GUI's numbersHBoxContainer accessor method.
    public VBox getNumbersHBoxContainer() {
        return numbersHBoxContainer;
    }
}
