package universal;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        ZERO, //
        ONE, //
        END_OF_TAPE // 
    }

    enum Q implements State {
        ZERO(false), //
        HALT(true);

        private boolean terminal;

        private Q(boolean terminal) {
            this.terminal = terminal;
        }

        @Override
        public boolean isTerminal() {
            return this.terminal;
        }
    }

    @Test
    void replaces0With1() {
        Q initialState = Q.ZERO;
        List<Alphabet> _011010 = Arrays.asList( //
                Alphabet.ZERO, Alphabet.ONE, Alphabet.ONE, //
                Alphabet.ZERO, Alphabet.ONE, Alphabet.ZERO, //
                Alphabet.END_OF_TAPE);
        Tape<Alphabet> tape = new Tape<>(_011010, Alphabet.END_OF_TAPE);

        TransitionTable transitions = new TransitionTable(). //
                row(Q.ZERO, Alphabet.ZERO, null, Alphabet.ONE, Direction.RIGHT). //
                row(Q.ZERO, Alphabet.ONE, null, null, Direction.RIGHT). //
                row(Q.ZERO, Alphabet.END_OF_TAPE, Q.HALT, null, Direction.NONE);

        TuringMachine<Alphabet> machine = new TuringMachine<>(tape, transitions, initialState);

        machine.loop();

        List<Alphabet> expected_111111 = Arrays.asList( //
                Alphabet.ONE, Alphabet.ONE, Alphabet.ONE, //
                Alphabet.ONE, Alphabet.ONE, Alphabet.ONE, //
                Alphabet.END_OF_TAPE);
        List<Alphabet> actualResult = tape.getCells();
        assertEquals(expected_111111, actualResult);
    }
}
