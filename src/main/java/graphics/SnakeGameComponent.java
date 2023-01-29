package graphics;

import entity.Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class SnakeGameComponent extends JComponent {

    private SnakeMap map;

    public SnakeGameComponent(SnakeMap map) {
        super();
        this.map = map;
        setBackground(map.getBackgroundColor());
        setOpaque(true);
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        java.awt.Graphics2D g2d = ((java.awt.Graphics2D) g);

        Insets ins = getInsets();
        Dimension dim = getSize();
        Rectangle r = new Rectangle(
                ins.left,
                ins.top,
                dim.width - ins.left - ins.right,
                dim.height - ins.top - ins.bottom
        );
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(r.x, r.y, r.width, r.height);
        }

        int xTiles = map.getWidth();
        int yTiles = map.getHeight();

        float xTileSize = ((float) r.width) / xTiles;
        float yTileSize = ((float) r.height) / yTiles;

        map.drawWithImages(g2d, new Rectangle2D.Double(0, 0, Math.round(xTileSize), Math.round(yTileSize)), new Point(0, 0));
    }

    public void setMap(SnakeMap map) {
        this.map = map;
    }
}
