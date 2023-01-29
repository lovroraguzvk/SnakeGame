package exception;

public class SnakeWallHit extends Exception {
    public SnakeWallHit() {
        super("Snake hit the wall");
    }
}
