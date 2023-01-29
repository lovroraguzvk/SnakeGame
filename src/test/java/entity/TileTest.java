package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    @Test
    void test() {
        Tile tile = new Tile(1, 2);
        assertEquals(1, tile.getX());
        assertEquals(2, tile.getY());
    }

}