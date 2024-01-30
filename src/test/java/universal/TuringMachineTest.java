package universal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import universal.state.State;
import universal.state.TransitionLookup;
import universal.state.TransitionTable;
import universal.tape.Direction;
import universal.tape.Symbol;
import universal.tape.Tape;

class TuringMachineTest {

    enum Alphabet implements Symbol {
        Zero, //
        One, //
        Blank // end of tape
    }

    enum Q implements State {
        Zero {
            @Override
            public boolean isTerminal() {
                return false;
            }
        },
        Halt {
            @Override
            public boolean isTerminal() {
                return true;
            }
        };
    }

    @Test
    void machineReplaces0With1() {
        Q initial = Q.Zero;
        List<Alphabet> _011010 = Arrays.asList(Alphabet.Zero, Alphabet.One, Alphabet.One, //
                Alphabet.Zero, Alphabet.One, Alphabet.Zero, Alphabet.Blank);
        Tape<Alphabet> tape = new Tape<>(_011010, Alphabet.Blank);

        TransitionLookup transitionLookup = new TransitionTable(null). //
                row(Q.Zero, Alphabet.Zero, null, Alphabet.One, Direction.RIGHT). //
                row(Q.Zero, Alphabet.One, null, null, Direction.RIGHT). //
                row(Q.Zero, Alphabet.Blank, Q.Halt, null, null); 

        TuringMachine<Alphabet> machine = new TuringMachine<>(tape, transitionLookup, initial);

        machine.loop();

        List<Alphabet> result = tape.getCells();
        List<Alphabet> _111111 = Arrays.asList(Alphabet.One, Alphabet.One, Alphabet.One, //
                Alphabet.One, Alphabet.One, Alphabet.One, Alphabet.Blank);
        assertEquals(_111111, result);
    }
}
