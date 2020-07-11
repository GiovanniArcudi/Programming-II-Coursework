package ga1g19;

// This class defines a change in a tile.
public class Change {
    private Tile tile;
    private int oldValue;
    private int newValue;

    public Change(Tile tile, int newValue) {
        this.tile = tile;
        this.oldValue = tile.getValue();
        this.newValue = newValue;
    }

    // Returns the tile.
    public Tile getTile() {
        return tile;
    }

    // Returns the value of the tile before the change.
    public int getOldValue() {
        return oldValue;
    }

    // Returns the value of the tile after the change.
    public int getNewValue() { return newValue; }
}
