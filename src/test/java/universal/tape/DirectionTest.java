package universal.tape;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DirectionTest {

    @Test
    void fromString() {
        assertEquals(Direction.RIGHT, Direction.fromString("r"));

        assertEquals(Direction.LEFT, Direction.fromString("L"));

        assertEquals(Direction.NONE, Direction.fromString("n"));
        assertEquals(Direction.NONE, Direction.fromString(" "));
    }

}
