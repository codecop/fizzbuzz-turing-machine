package universal.state;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import universal.tape.Direction;

class StringTransitionTableTest {

    StringTransitionTable table = new StringTransitionTable(null);

    @Test
    void directionFromString() {
        assertEquals(Direction.RIGHT, table.fromString("r"));

        assertEquals(Direction.LEFT, table.fromString("L"));

        assertEquals(Direction.NONE, table.fromString("n"));
        assertEquals(Direction.NONE, table.fromString(" "));
    }

}
