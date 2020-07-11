// This class defines a Grid solver.
public class Solver {
    private Grid grid;
    private Cage[] cagesArray;              // Tile indexed x on the grid has cageList[x-1] Cage.
    private int[] guessedValuesArray;       // Tile indexed x guessed value is guessedValuesArray[x-1].
    private int guessingIndexIterator;

    // Class constructor.
    public Solver (Grid grid) {
        this.grid = grid;
        cagesArray = new Cage[((int) Math.pow(grid.getGridSize(), 2))];
        guessedValuesArray = new int[((int) Math.pow(grid.getGridSize(), 2))];
        guessingIndexIterator = 0;

        // Adds the right Cage to the right position in cagesArray.
        for (int i = 0; i < Math.pow(this.grid.getGridSize(),2); i++) {
            for (Tile tile : this.grid.getTilesList()) {
                if (tile.getGridPositionIndex() == i + 1) {
                    cagesArray[i] = tile.getCage();
                }
            }
        }

        // Solves the grid.
        solveGrid();
    }

    // Solves the grid.
    public void solveGrid() {
        try {
            while (guessingIndexIterator >= 0 && guessingIndexIterator < Math.pow(grid.getGridSize(), 2)) {

                // If a guessed value gets bigger than grid's size sets it to 0 and moves the iterator back.
                if (guessedValuesArray[guessingIndexIterator] >= grid.getGridSize()) {
                    guessedValuesArray[guessingIndexIterator] = 0;
                    guessingIndexIterator--;
                    continue;
                }

                // If the guessed value is still within grid's size but is not correct, increments it.
                do {
                    guessedValuesArray[guessingIndexIterator]++;
                } while (!isCorrect(guessedValuesArray[guessingIndexIterator]) && guessedValuesArray[guessingIndexIterator] <= grid.getGridSize());

                // If the guessed value is bigger than grid's size sets it to 0, brings the iterator back, and restarts the loop.
                if (guessedValuesArray[guessingIndexIterator] > grid.getGridSize()) {
                    guessedValuesArray[guessingIndexIterator] = 0;
                    guessingIndexIterator--;
                    continue;
                }

                // If the guessed value is correct and within grid's size, move the tileIndexIterator ahead.
                guessingIndexIterator++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Checks if a value can be inserted in a given place.
    public boolean isCorrect(int guessedValue) {
        return checkColumn(guessedValue) && checkRow(guessedValue) && checkCage();
    }

    // Checks if, according to previous inputs, the guessed value can stay in the column.
    public boolean checkColumn(int guessedValue) {
        for (int gridIndex = guessingIndexIterator - grid.getGridSize(); gridIndex >= 0; gridIndex -= grid.getGridSize()) {
            if (guessedValuesArray[gridIndex] == guessedValue) {
                return false;
            }
        }
        return true;
    }

    // Checks if, according to previous inputs, the guessed value can stay in the row.
    public boolean checkRow(int guessedValue) {
        for (int gridIndex = guessingIndexIterator - 1; (gridIndex + 1) % grid.getGridSize() > 0; gridIndex--) {
            if (guessedValuesArray[gridIndex] == guessedValue) {
                return false;
            }
        }
        return true;
    }

    // Checks if, according to previous inputs, the guessed value can stay in the cage.
    public boolean checkCage() {

        // Gets the arithmetic operator of the cage the tile we are trying to fill belongs to.
        String arithmeticOperator = cagesArray[guessingIndexIterator].getArithmeticOperator();

        // Different behaviour for different operators.
        if (arithmeticOperator != null && !arithmeticOperator.isEmpty()) {
            switch (arithmeticOperator) {
                case "-":
                    return checkSubtraction();
                case "*":
                case "x":
                    return checkMultiplication();
                case "/":
                case "÷":
                    return checkDivision();
                default:
                    return checkAddition();
            }
        } else {
            return checkAddition();
        }
    }

    // Checks if, according to previous inputs, the guessedValue is ok for a cage whose operator is +.
    public boolean checkAddition() {

        // Initializes useful variables.
        int cageTarget = cagesArray[guessingIndexIterator].getTarget();
        int cageTotal = 0;

        boolean cageIsComplete = true;

        // Sums up all the guessedValues belonging to the tiles of the cage of the tile whose grid index is the one passed.
        for (int i = 0; i < cagesArray[guessingIndexIterator].getCageTilesList().size(); i++) {

            cageTotal += guessedValuesArray[cagesArray[guessingIndexIterator].getCageTilesList().get(i).getGridPositionIndex() - 1];

            // If the cage contains one zero this means that is not complete.
            if (guessedValuesArray[cagesArray[guessingIndexIterator].getCageTilesList().get(i).getGridPositionIndex() - 1] == 0) {
                cageIsComplete = false;
            }
        }

        // If the cage is complete and the sum equals the target, or if cage is not complete but the sum is less
        // then the target, the guessed value is fine so return true.
        return cageTotal == cageTarget || (!cageIsComplete && cageTotal < cageTarget);
    }

    // Checks if, according to previous inputs, the guessedValue is ok for a cage whose operator is -.
    public boolean checkSubtraction() {

        // Gets the grid index of the cage tile with the max value.
        int cageMaxValueTileIndex = cagesArray[guessingIndexIterator].getCageTilesList().get(0).getGridPositionIndex() - 1;
        for (int i = 1; i < cagesArray[guessingIndexIterator].getCageTilesList().size(); i++) {
            if (cagesArray[guessingIndexIterator].getCageTilesList().get(i).getGridPositionIndex() - 1 > cageMaxValueTileIndex) {
                cageMaxValueTileIndex = cagesArray[guessingIndexIterator].getCageTilesList().get(i).getGridPositionIndex() - 1;
            }
        }

        // Initializes useful variables.
        int cageTarget = cagesArray[guessingIndexIterator].getTarget();
        int cageTotal = 0;
        int cageMaxValue = guessedValuesArray[cageMaxValueTileIndex];

        // Sums up all the guessedValues belonging to the tiles of the cage of the tile whose grid index is the one passed.
        for (int i = 0; i < cagesArray[guessingIndexIterator].getCageTilesList().size(); i++) {
            cageTotal += guessedValuesArray[cagesArray[guessingIndexIterator].getCageTilesList().get(i).getGridPositionIndex() - 1];

            // If one of the guessed values is greater than the previously defined cageMaxValue, update cageMaxValue.
            if (guessedValuesArray[cagesArray[guessingIndexIterator].getCageTilesList().get(i).getGridPositionIndex() - 1] > cageMaxValue) {
                cageMaxValue = guessedValuesArray[cagesArray[guessingIndexIterator].getCageTilesList().get(i).getGridPositionIndex() - 1];
            }

            // If the cage is incomplete, the guessed value can stay, so return true
            if (guessedValuesArray[cagesArray[guessingIndexIterator].getCageTilesList().get(i).getGridPositionIndex() - 1] == 0) {
                return true;
            }
        }

        // Takes the biggest element and subtracts all other elements of the cage to it.
        return (2 * cageMaxValue) - cageTotal == cageTarget;
    }

    // Checks if, according to previous inputs, the guessedValue is ok for a cage whose operator is x or *.
    public boolean checkMultiplication() {

        // Initializes useful variables.
        int cageTarget = cagesArray[guessingIndexIterator].getTarget();
        int cageTotal = 1;
        boolean cageIsComplete = true;

        // Multiplies all the guessedValues belonging to the tiles of the cage of the tile whose grid index is the one passed.
        for (int i = 0; i < cagesArray[guessingIndexIterator].getCageTilesList().size(); i++) {
            cageTotal *= guessedValuesArray[cagesArray[guessingIndexIterator].getCageTilesList().get(i).getGridPositionIndex() - 1];

            // If the cage is incomplete, set the flag to appropriate value.
            if (guessedValuesArray[cagesArray[guessingIndexIterator].getCageTilesList().get(i).getGridPositionIndex() - 1] < 1) {
                cageIsComplete = false;
            }
        }

        return cageTotal == cageTarget || (!cageIsComplete && cageTotal <= cageTarget);
    }

    // Checks if, according to previous inputs, the guessedValue is ok for a cage whose operator is ÷ or /.
    public boolean checkDivision() {

        // Gets the grid index of the cage tile with the max value.
        int cageMaxValueTileIndex = cagesArray[guessingIndexIterator].getCageTilesList().get(0).getGridPositionIndex() - 1;
        for (int i = 1; i < cagesArray[guessingIndexIterator].getCageTilesList().size(); i++) {
            if (cagesArray[guessingIndexIterator].getCageTilesList().get(i).getGridPositionIndex() - 1 > cageMaxValueTileIndex) {
                cageMaxValueTileIndex = cagesArray[guessingIndexIterator].getCageTilesList().get(i).getGridPositionIndex() - 1;
            }
        }

        // Initializes useful variables.
        int cageTarget = cagesArray[guessingIndexIterator].getTarget();
        int cageTotal = 1;
        int cageMaxValue = guessedValuesArray[cageMaxValueTileIndex];

        // Multiplies all the guessedValues belonging to the tiles of the cage of the tile whose grid index is the one passed.
        for (int i = 0; i < cagesArray[guessingIndexIterator].getCageTilesList().size(); i++) {
            cageTotal *= guessedValuesArray[cagesArray[guessingIndexIterator].getCageTilesList().get(i).getGridPositionIndex() - 1];

            // If one of the guessed values is greater than the previously defined cageMaxValue, update cageMaxValue.
            if (guessedValuesArray[cagesArray[guessingIndexIterator].getCageTilesList().get(i).getGridPositionIndex() - 1] > cageMaxValue) {
                cageMaxValue = guessedValuesArray[cagesArray[guessingIndexIterator].getCageTilesList().get(i).getGridPositionIndex() - 1];
            }

            // If the cage is incomplete, the guessed value can stay, so return true
            if (guessedValuesArray[cagesArray[guessingIndexIterator].getCageTilesList().get(i).getGridPositionIndex() - 1] < 1) {
                return true;
            }
        }
        cageTotal /= cageMaxValue;

        return cageMaxValue / cageTotal == cageTarget;
    }

    // Returns solution.
    public int[] getSolution(){
        return guessedValuesArray;
    }
}
