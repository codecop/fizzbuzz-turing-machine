package universal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static universal.TuringMachineTest.Alphabet.END_OF_TAPE;
import static universal.TuringMachineTest.Alphabet._1;
import static universal.TuringMachineTest.Alphabet._0;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import universal.state.State;
import universal.state.TransitionTable;
import universal.tape.Direction;
import universal.tape.Symbol;
import universal.tape.Tape;

class TuringMachineTest {

    enum Alphabet implements Symbol {
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
        List<Alphabet> _011010 = Arrays.asList(_0, _1, _1, _0, _1, _0, END_OF_TAPE);
        Tape<Alphabet> tape = new Tape<>(_011010, END_OF_TAPE);

        TransitionTable transitions = new TransitionTable(). //
                row(S.RUNNING, _0, null, _1, Direction.RIGHT). //
                row(S.RUNNING, _1, null, null, Direction.RIGHT). //
                row(S.RUNNING, END_OF_TAPE, S.HALT, null, Direction.NONE);

        TuringMachine<Alphabet> machine = new TuringMachine<>(tape, transitions, initialState);

        machine.loop();

        List<Alphabet> expected_111111 = Arrays.asList(_1, _1, _1, _1, _1, _1, END_OF_TAPE);
        List<Alphabet> actualResult = tape.getCells();
        assertEquals(expected_111111, actualResult);
    }
}
