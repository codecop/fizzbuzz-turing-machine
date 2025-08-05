package universal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static universal.SimpleExampleTest.Bit.END_OF_TAPE;
import static universal.SimpleExampleTest.Bit._1;
import static universal.SimpleExampleTest.Bit._0;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import universal.state.State;
import universal.state.TransitionTable;
import universal.tape.Direction;
import universal.tape.Symbol;
import universal.tape.Tape;

class SimpleExampleTest {

    enum Bit implements Symbol {
        _0, _1, END_OF_TAPE
    }

    enum S implements State {
        RUNNING, HALT;

        @Override
        public boolean isTerminal() {
            return this == HALT;
        }
    }

    @Test
    void replaces0With1() {
        S initialState = S.RUNNING;
        var _011010 = Arrays.asList(_0, _1, _1, _0, _1, _0, END_OF_TAPE);
        var tape = new Tape<>(_011010, END_OF_TAPE);

        TransitionTable transitions = new TransitionTable(). //
                row(S.RUNNING, _0, null, _1, Direction.RIGHT). //
                row(S.RUNNING, _1, null, null, Direction.RIGHT). //
                row(S.RUNNING, END_OF_TAPE, S.HALT, null, Direction.NONE);

        var machine = new TuringMachine<>(tape, transitions, initialState);

        machine.loop();

        var expected_111111 = Arrays.asList(_1, _1, _1, _1, _1, _1, END_OF_TAPE);
        var actualResult = tape.getCells();
        assertEquals(expected_111111, actualResult);
    }
}
