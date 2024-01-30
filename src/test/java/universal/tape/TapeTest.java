package universal.tape;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class TapeTest {

    enum S implements Symbol {
        A, B, C, EMPTY;
    }

    @Test
    void readAndWrite() {
        Tape<S> tape = new Tape<>(Arrays.asList(S.A), null);

        assertEquals(S.A, tape.read());
        tape.write(S.C);
        assertEquals(S.C, tape.read());
    }

    @Test
    void move() {
        Tape<S> tape = new Tape<>(Arrays.asList(S.A, S.B, S.C), null);

        tape.moveHead(Direction.RIGHT);
        assertEquals(S.B, tape.read());
        tape.moveHead(Direction.RIGHT);
        assertEquals(S.C, tape.read());

        tape.moveHead(Direction.NONE);
        assertEquals(S.C, tape.read());

        tape.moveHead(Direction.LEFT);
        assertEquals(S.B, tape.read());
    }

    @Test
    void wroteBeyondEndWithDefault() {
        Tape<S> tape = new Tape<>(Arrays.asList(S.A), S.EMPTY);

        tape.moveHead(Direction.RIGHT);
        assertEquals(S.EMPTY, tape.read());

        tape.write(S.B);
        assertEquals(S.B, tape.read());

        assertEquals(Arrays.asList(S.A, S.B), tape.getCells());
    }

    @Test
    void wroteBeyondEnd() {
        Tape<S> tape = new Tape<>(Arrays.asList(S.A), null);

        tape.moveHead(Direction.RIGHT);

        tape.write(S.C);
        assertEquals(S.C, tape.read());

        assertEquals(Arrays.asList(S.A, S.C), tape.getCells());
    }

    @Test
    void wroteBelowBeginning() {
        Tape<S> tape = new Tape<>(Arrays.asList(S.A), null);

        tape.moveHead(Direction.LEFT);

        tape.write(S.C);
        assertEquals(S.C, tape.read());

        assertEquals(Arrays.asList(S.C, S.A), tape.getCells());
    }

}
