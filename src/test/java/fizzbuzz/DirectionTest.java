package fizzbuzz;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import fizzbuzz.Direction;

class DirectionTest {

    @Test
    void fromString() {
        assertEquals(Direction.RIGHT, Direction.fromString("r"));

        assertEquals(Direction.LEFT, Direction.fromString("L"));

        assertEquals(Direction.NONE, Direction.fromString("n"));
        assertEquals(Direction.NONE, Direction.fromString(" "));
    }

}
