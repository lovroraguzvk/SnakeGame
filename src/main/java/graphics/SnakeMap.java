package graphics;

import entity.Food;
import entity.Snake;
import entity.Tile;
import exception.SnakeBitItself;
import exception.SnakeWallHit;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

public class SnakeMap {
    private final int width;
    private final int height;
    private final LinkedList<Tile> food = new LinkedList<>();
    private final Snake snake = new Snake();
    private Color backgroundColor = Color.GRAY;

    public SnakeMap(int width, int height) {
        this.width = width;
        this.height = height;
        food.add(new Food(10, 10));
    }

    public SnakeMap(int width, int height, Color backgroundColor) {
        this(width, height);
        this.backgroundColor = backgroundColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void draw(Graphics2D g2d, Rectangle2D.Double rect, Point offset) {
        //updateSnake();
    }

    public void drawWithImages(Graphics2D g2d, Rectangle2D.Double rect, Point offset) {
        snake.drawWithImages(g2d, rect, offset);

        for (Tile tile : food) {
            tile.draw(g2d, rect, offset, Color.RED);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void moveSnake(int direction) throws SnakeBitItself, SnakeWallHit {
        if (checkBounds(direction)) {
            snake.move(direction);
            if (checkFood()) snake.grow();
        } else {
            throw new SnakeWallHit();
        }
    }

    public void moveAndGrowSnake(int direction) throws SnakeBitItself {
        if (checkBounds(direction)) {
            snake.moveAndGrow(direction);
        }
    }

    public boolean checkBounds(int direction) {
        if (snake.head.getX() == 0 && direction == Snake.WEST ||
            snake.head.getX() == width - 1 && direction == Snake.EAST ||
            snake.head.getY() == 0 && direction == Snake.NORTH ||
            snake.head.getY() == height - 1 && direction == Snake.SOUTH) {
            System.out.println("You hit the wall!");
            return false;
        }
        return true;
    }

    public boolean checkCollision() {
        for (Tile tile : snake) {
            if (tile.getX() == snake.head.getX() && tile.getY() == snake.head.getY()) {
                System.out.println("You hit yourself!");
                return true;
            }
        }
        return false;
    }

    public boolean checkFood() {
        for (Tile tile : food) {
            if (tile.getX() == snake.head.getX() && tile.getY() == snake.head.getY()) {
                food.remove(tile);
                food.add(new Food((int) (Math.random() * width), (int) (Math.random() * height)));

                return true;
            }
        }
        return false;
    }
}
