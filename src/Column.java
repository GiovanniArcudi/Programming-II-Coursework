package ga1g19;

import java.util.ArrayList;

// This class defines a grid's column.
public class Column {
    private ArrayList<Tile> columnTilesList;

    public Column(ArrayList<Tile> columnTiles) {
        this.columnTilesList = columnTiles;
    }

    // Adds a tile to the columnTilesList.
    public void add(Tile tile) {
        columnTilesList.add(tile);
    }

    // columnTilesList accessor method.
    public ArrayList<Tile> getTilesList() {
        return columnTilesList;
    }
}