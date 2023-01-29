package entity;

import util.TilePosition;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Tile implements Comparable<Tile> {

    TilePosition position;

    public Tile(int x, int y) {
        this.position = new TilePosition(x, y);
    }

    public Tile(TilePosition position) {
        this.position = position;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public void setX(int x) {
        position = new TilePosition(x, position.getY());
    }

    public void setY(int y) {
        position = new TilePosition(position.getX(), y);
    }

    public void setPosition(TilePosition position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object obj) {
        Tile o = (Tile) obj;
        return o.position.equals(position);
    }

    @Override
    public String toString() {
        return position.toString();
    }

    public void draw(Graphics2D g2d, Rectangle2D.Double rect, Point offset, Color color) {
        g2d.setColor(color);
        g2d.fillRect(
                (int) (rect.x + offset.x + position.getX() * rect.width),
                (int) (rect.y + offset.y + position.getY() * rect.height),
                (int) rect.width,
                (int) rect.height
        );
    }

    public boolean isNextTo(Tile o) {
        return this.position.isNextTo(o.position);
    }

    public int getDirection(Tile o) {
        return this.position.getDirection(o.position);
    }

    @Override
    public int compareTo(Tile o) {
        return 0;
    }
}
