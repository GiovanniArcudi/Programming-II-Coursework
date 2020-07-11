import java.util.ArrayList;

// This class defines a grid's row.
public class Row {
    private ArrayList<Tile> rowTilesList;

    public Row(ArrayList<Tile> rowOfTiles) {
        this.rowTilesList = rowOfTiles;
    }

    // Adds a tile to the rowTilesList.
    public void add(Tile tile) {
        rowTilesList.add(tile);
    }

    // rowTilesList accessor method.
    public ArrayList<Tile> getTilesList() {
        return rowTilesList;
    }
}
