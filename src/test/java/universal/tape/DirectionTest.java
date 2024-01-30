package universal.tape;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import universal.state.TransitionTable;

class DirectionTest {

    @Test
    void fromString() {
        assertEquals(Direction.RIGHT, TransitionTable.fromString("r"));

        assertEquals(Direction.LEFT, TransitionTable.fromString("L"));

        assertEquals(Direction.NONE, TransitionTable.fromString("n"));
        assertEquals(Direction.NONE, TransitionTable.fromString(" "));
    }

}
