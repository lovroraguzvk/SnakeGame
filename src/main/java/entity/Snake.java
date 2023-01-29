package entity;

import enums.SnakeCompType;
import exception.SnakeBitItself;
import util.TilePosition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Snake implements Iterable<Snake.SnakeComponent> {

    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int EAST = 2;
    public static final int WEST = 3;

    public static class SnakeComponent extends Tile {

        private SnakeCompType type;
        public SnakeComponent next;
        public SnakeComponent prev;

        public SnakeComponent(int x, int y, SnakeCompType type) {
            super(x, y);
            this.type = type;
        }

        public SnakeComponent(TilePosition position, SnakeCompType type) {
            super(position);
            this.type = type;
        }

        public SnakeCompType getType() {
            return type;
        }

        public SnakeComponent getNext() {
            return next;
        }

        public void setType(SnakeCompType type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return String.format("%d,%d,%s", getX(), getY(), type);
        }

        public void draw(Graphics2D g2d, Rectangle2D.Double rect, Point offset, Image[] images) {
            g2d.drawImage(
                    images[type.ordinal()],
                    (int) (rect.x + offset.x + getX() * rect.width),
                    (int) (rect.y + offset.y + getY() * rect.height),
                    (int) rect.width,
                    (int) rect.height,
                    null
            );
        }

        public int compareTo(SnakeComponent o) {
            return 0;
        }

        public TilePosition[] availableTiles() {
            if ((type.isHead() || type.isTail()) && (next != null || prev != null)) {
                int posOfAdjacent;
                if (type.isHead()) posOfAdjacent = this.position.getDirection(next.position);
                else posOfAdjacent = this.position.getDirection(prev.position);

                TilePosition[] availablePositions = new TilePosition[3];
                int i = 0;

                if (posOfAdjacent != TilePosition.NORTH)
                    availablePositions[i++] = new TilePosition(this.getX(), this.getY() - 1);
                if (posOfAdjacent != TilePosition.EAST)
                    availablePositions[i++] = new TilePosition(this.getX() + 1, this.getY());
                if (posOfAdjacent != TilePosition.SOUTH)
                    availablePositions[i++] = new TilePosition(this.getX(), this.getY() + 1);
                if (posOfAdjacent != TilePosition.WEST)
                    availablePositions[i] = new TilePosition(this.getX() - 1, this.getY());

                return availablePositions;
            }
            return null;
        }

        public void move(int direction) {
            switch(direction) {
                case Snake.NORTH -> this.setY(getY() - 1);
                case Snake.SOUTH -> this.setY(getY() + 1);
                case Snake.EAST -> this.setX(getX() + 1);
                case Snake.WEST -> this.setX(getX() - 1);
                default -> throw new IllegalArgumentException("Invalid direction");
            }
        }



    }

    public SnakeComponent head;
    private SnakeComponent tail;
    private Set<TilePosition> positions = new TreeSet<>();
    private int size = 0;
    private Image[] images;

    public Snake() {
        addNewPart(new TilePosition(6, 5));
        addNewPart(new TilePosition(5, 5));
        grow();
        grow();
        images = loadImages();
    }


    public void addNewPart(TilePosition position) {
        SnakeComponent newPart = new SnakeComponent(position, SnakeCompType.TAIL_SOUTH);

        if (head == null) {
            newPart.setType(SnakeCompType.HEAD_NORTH);
            head = newPart;
        } else {
            if (tail.availableTiles() != null && Arrays.stream(tail.availableTiles()).noneMatch(position::equals)) {
                throw new IllegalArgumentException("Invalid position for new part");
            }

            tail.next = newPart;
            newPart.prev = tail;
        }
        tail = newPart;

        positions.add(position);

        renderCompTypes();
        size++;
    }

    public void moveAndGrow(int direction) throws SnakeBitItself {
        TilePosition oldTailPos = new TilePosition(tail.position);
        move(direction);
        addNewPart(oldTailPos);
    }

    public void grow() {
        if (tail.getType() == SnakeCompType.TAIL_NORTH) {
            addNewPart(new TilePosition(tail.getX(), tail.getY() - 1));
        } else if (tail.getType() == SnakeCompType.TAIL_SOUTH) {
            addNewPart(new TilePosition(tail.getX(), tail.getY() + 1));
        } else if (tail.getType() == SnakeCompType.TAIL_EAST) {
            addNewPart(new TilePosition(tail.getX() + 1, tail.getY()));
        } else if (tail.getType() == SnakeCompType.TAIL_WEST) {
            addNewPart(new TilePosition(tail.getX() - 1, tail.getY()));
        }
    }

    public void move(int direction) throws SnakeBitItself {
        LinkedList<SnakeComponent> oldPositions = new LinkedList<>();
        var oldHead = new SnakeComponent(head.position, head.type);
        var oldTail = new SnakeComponent(tail.position, tail.type);
        for (SnakeComponent part : this) {
            oldPositions.add(new SnakeComponent(part.position, part.getType()));
        }

        int dir = direction, nextDir;
        for (var component : this) {
            if (component.getType().isTail()) {
                positions.remove(component.position);
                component.move(dir);
                positions.add(component.position);
                break;
            }

            nextDir = component.next.getDirection(component);
            positions.remove(component.position);
            component.move(dir);
            positions.add(component.position);
            dir = nextDir;
        }

        if (positions.size() != size &&
                !(oldHead.isNextTo(oldTail) && oldHead.getDirection(oldTail) == direction)) {
            System.out.println("Bit yourself!");
            renderCompTypes();
            throw new SnakeBitItself();
            /*
            Iterator<SnakeComponent> it = oldPositions.iterator();

            SnakeComponent current = it.next();
            for (var component : this) {
                component.setPosition(current.position);
                if (component.getType().isTail()) break;
                current = it.next();
            }

             */
        }

        renderCompTypes();
    }

    public void renderCompTypes() {
        if (head == null || head.next == null) return;

        for (var component : this) {
            if (component == head) {
                switch (component.getDirection(component.next)) {
                    case TilePosition.NORTH -> component.setType(SnakeCompType.HEAD_SOUTH);
                    case TilePosition.SOUTH -> component.setType(SnakeCompType.HEAD_NORTH);
                    case TilePosition.EAST -> component.setType(SnakeCompType.HEAD_WEST);
                    case TilePosition.WEST -> component.setType(SnakeCompType.HEAD_EAST);
                }
            }
            else if (component == tail) {
                switch (component.getDirection(component.prev)) {
                    case TilePosition.NORTH -> component.setType(SnakeCompType.TAIL_SOUTH);
                    case TilePosition.SOUTH -> component.setType(SnakeCompType.TAIL_NORTH);
                    case TilePosition.EAST -> component.setType(SnakeCompType.TAIL_WEST);
                    case TilePosition.WEST -> component.setType(SnakeCompType.TAIL_EAST);
                }
            }
            else {
                int dir1 = component.getDirection(component.prev);
                int dir2 = component.getDirection(component.next);
                if (dir1 == TilePosition.NORTH && dir2 == TilePosition.SOUTH || dir1 == TilePosition.SOUTH && dir2 == TilePosition.NORTH)
                    component.setType(SnakeCompType.BODY_VERTICAL);
                else if (dir1 == TilePosition.EAST && dir2 == TilePosition.WEST || dir1 == TilePosition.WEST && dir2 == TilePosition.EAST)
                    component.setType(SnakeCompType.BODY_HORIZONTAL);
                else if (dir1 == TilePosition.NORTH && dir2 == TilePosition.EAST || dir1 == TilePosition.EAST && dir2 == TilePosition.NORTH)
                    component.setType(SnakeCompType.BODY_NE);
                else if (dir1 == TilePosition.NORTH && dir2 == TilePosition.WEST || dir1 == TilePosition.WEST && dir2 == TilePosition.NORTH)
                    component.setType(SnakeCompType.BODY_NW);
                else if (dir1 == TilePosition.SOUTH && dir2 == TilePosition.EAST || dir1 == TilePosition.EAST && dir2 == TilePosition.SOUTH)
                    component.setType(SnakeCompType.BODY_SE);
                else if (dir1 == TilePosition.SOUTH && dir2 == TilePosition.WEST || dir1 == TilePosition.WEST && dir2 == TilePosition.SOUTH)
                    component.setType(SnakeCompType.BODY_SW);
            }

        }
    }

    public void draw(Graphics2D g2d, Rectangle2D.Double rect, Point offset) {
        this.renderCompTypes();
        for (var component : this) {
            component.draw(g2d, rect, offset, Color.GREEN);
        }
    }

    public void drawWithImages(Graphics2D g2d, Rectangle2D.Double rect, Point offset) {
        this.renderCompTypes();
        for (var component : this) {
            component.draw(g2d, rect, offset, images);
        }
    }

    @Override
    public Iterator<SnakeComponent> iterator() {
        return new Iterator<SnakeComponent>() {
            private SnakeComponent current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public SnakeComponent next() {
                SnakeComponent tmp = current;
                current = current.next;
                return tmp;
            }
        };
    }

    private Image[] loadImages() {
        String image_names[] = {
                "head_north.png",
                "head_east.png",
                "head_south.png",
                "head_west.png",
                "body_horizontal.png",
                "body_vertical.png",
                "body_ne.png",
                "body_se.png",
                "body_sw.png",
                "body_nw.png",
                "tail_north.png",
                "tail_east.png",
                "tail_south.png",
                "tail_west.png"
        };
        Image[] images = new Image[14];

        for (int i = 0; i < image_names.length; i++) {
            System.out.println("Loading image: " + image_names[i]);
            System.out.println("Path: " + getClass().getResource("/entity/snake2/" + image_names[i]));
            try (InputStream is = getClass().getResourceAsStream("/entity/snake2/" + image_names[i])){
                images[i] = ImageIO.read(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        /*
        images[0] = new ImageIcon("src/main/resources/graphics/snake2/body_tail_north.png").getImage();
        images[1] = new ImageIcon("src/main/resources/graphics/body_tail_east.png").getImage();
        images[2] = new ImageIcon("src/main/resources/graphics/body_tail_south.png").getImage();
        images[3] = new ImageIcon("src/main/resources/graphics/body_tail_west.png").getImage();
        images[4] = new ImageIcon("src/main/resources/graphics/body_horizontal.png").getImage();
        images[5] = new ImageIcon("src/main/resources/graphics/body_vertical.png").getImage();
        images[6] = new ImageIcon("src/main/resources/graphics/body_ne.png").getImage();
        images[7] = new ImageIcon("src/main/resources/graphics/body_se.png").getImage();
        images[8] = new ImageIcon("src/main/resources/graphics/body_sw.png").getImage();
        images[9] = new ImageIcon("src/main/resources/graphics/body_nw.png").getImage();
        images[10] = new ImageIcon("src/main/resources/graphics/body_tail_north.png").getImage();
        images[11] = new ImageIcon("src/main/resources/graphics/body_tail_east.png").getImage();
        images[12] = new ImageIcon("src/main/resources/graphics/body_tail_south.png").getImage();
        images[13] = new ImageIcon("src/main/resources/graphics/body_tail_west.png").getImage();

         */
        return images;
    }
}
