package universal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class TuringMachineTest {

    @Test
    void machineReplaces0With1() {
        Q initial = Q.Zero;
        List<Alphabet> _001010 = Arrays.asList(Alphabet.Zero, Alphabet.Zero, Alphabet.One, //
                Alphabet.Zero, Alphabet.One, Alphabet.Zero, Alphabet.Blank);
        Tape<Alphabet> tape = new Tape<>(_001010);
        TransitionRules transitionRules = new Transitions();
        TuringMachine<Alphabet> machine = new TuringMachine<>(tape, transitionRules, initial);

        machine.loop();

        List<Alphabet> result = tape.getCells();
        List<Alphabet> _111111 = Arrays.asList(Alphabet.One, Alphabet.One, Alphabet.One, //
                Alphabet.One, Alphabet.One, Alphabet.One, Alphabet.Blank);
        assertEquals(_111111, result);
    }

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

    class Transitions extends TransitionRulesChain {

        @Override
        protected Transition nextOrNull(State state, Symbol symbol) {
            if (state == Q.Zero && symbol == Alphabet.Zero) {
                // If in state q0 and read '0', write '1' and move right.
                return new Transition(Q.Zero, Alphabet.One, Direction.RIGHT);

            } else if (state == Q.Zero && symbol == Alphabet.One) {
                // If in state q0 and read '1', move right without modification.
                return new Transition(Q.Zero, Alphabet.One, Direction.RIGHT);

            } else if (state == Q.Zero && symbol == Alphabet.Blank) {
                // If in state q0 and read blank (end of tape), halt.
                return new Transition(Q.Halt, Alphabet.Blank, Direction.NONE);

            } else {
                return null;
            }
        }

    }

    @Test
    void machineReplaces0With1UsingTable() {
        Q initial = Q.Zero;
        List<Alphabet> _011010 = Arrays.asList(Alphabet.Zero, Alphabet.One, Alphabet.One, //
                Alphabet.Zero, Alphabet.One, Alphabet.Zero, Alphabet.Blank);
        Tape<Alphabet> tape = new Tape<>(_011010);

        TransitionRules transitionRules = new TransitionTable(). //
                row(Q.Zero, Alphabet.Zero, null, Alphabet.One, Direction.RIGHT). //
                row(Q.Zero, Alphabet.One, null, null, Direction.RIGHT). //
                row(Q.Zero, Alphabet.Blank, Q.Halt, null, null). //
                toRules();

        TuringMachine<Alphabet> machine = new TuringMachine<>(tape, transitionRules, initial);

        machine.loop();

        List<Alphabet> result = tape.getCells();
        List<Alphabet> _111111 = Arrays.asList(Alphabet.One, Alphabet.One, Alphabet.One, //
                Alphabet.One, Alphabet.One, Alphabet.One, Alphabet.Blank);
        assertEquals(_111111, result);
    }
}
