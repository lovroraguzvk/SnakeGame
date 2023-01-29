package util;

public class TilePosition implements Comparable<TilePosition> {

    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int EAST = 2;
    public static final int WEST = 3;


    private int x;
    private int y;

    public TilePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public TilePosition(TilePosition position) {
        this.x = position.x;
        this.y = position.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        TilePosition o = (TilePosition) obj;
        return o.x == x && o.y == y;
    }

    @Override
    public String toString() {
        return String.format("%d,%d", x, y);
    }

    @Override
    public int compareTo(TilePosition o) {
        if (o.x == x) {
            return Integer.compare(y, o.y);
        }
        return Integer.compare(x, o.x);
    }

    public int getDirection(TilePosition other) {
        if (other.x == x) {
            if (other.y > y) return SOUTH;
            else return NORTH;
        } else {
            if (other.x > x) return EAST;
            else return WEST;
        }
    }


    public boolean isNextTo(TilePosition position) {
        return Math.abs(position.x - x) + Math.abs(position.y - y) == 1;
    }
}
