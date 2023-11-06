package fizzbuzz;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

interface Symbol {

}

interface State {
    boolean isTerminal();
}

/**
 * A Universal Turing Machine (UTM). The Universal Turing Machine can simulate the behaviour of any
 * Turing machine. It takes the description of any arbitrary Turing machine as input and mimic its
 * operation on a given input string.
 */
public class TuringMachine<SYM extends Symbol> {

    private final Tape<SYM> tape;
    private final TransitionRules transitionRules;
    private State state;

    private boolean debug = false;

    public TuringMachine(Tape<SYM> tape, TransitionRules transitionRules, State initialState) {
        this.tape = tape;
        this.transitionRules = transitionRules;
        this.state = initialState;
    }

    public void loop() {
        while (!state.isTerminal()) {
            act();
        }
    }

    /**
     * Reads the symbol present in that cell, and based on the current state and the read symbol, it
     * determines the next state and the symbol to write on the tape, and then it moves the head left or
     * right by one cell.
     */
    @SuppressWarnings("unchecked")
    private void act() {
        SYM symbol = read();

        if (debug) {
            System.out.println(tape + "\tState: '" + state + "' \t Symbol: '" + symbol + "'");
        }
        Transition transition = transitionRules.next(state, symbol);

        // Change State: The TM can change its internal state based on the current state it is in 
        // and the symbol it reads from the tape.
        setState(transition.nextState);
        write((SYM) transition.symbolToWrite);
        move(transition.directionToMove);
    }

    /**
     * The TM can read the symbol currently present on the tape at the position of its read/write head.
     */
    private SYM read() {
        return tape.read();
    }

    private void setState(State state) {
        this.state = state;
    }

    /**
     * The TM can write a new symbol on the tape at the position of its read/write head, replacing the
     * symbol that was previously there.
     */
    private void write(SYM symbol) {
        tape.write(symbol);
    }

    /**
     * The TM can move its read/write head one position to the left or right along the tape.
     */
    private void move(Direction direction) {
        tape.moveHead(direction);
    }

}

/**
 * Syntactic Sugar.
 */
class D {
    static final Direction L = Direction.LEFT;
    static final Direction N = Direction.NONE;
    static final Direction R = Direction.RIGHT;
}

enum Direction {
    LEFT, NONE, RIGHT;

    static Direction fromString(String direction) {
        if (direction == null) {
            return NONE;
        }
        switch (direction.toLowerCase()) {
        case "l":
            return LEFT;
        case "":
        case " ":
        case "n":
            return NONE;
        case "r":
            return RIGHT;
        default:
            throw new IllegalArgumentException(direction);
        }
    }
}

// Define the tape: Represent the infinite tape as a data structure, such as an array or a list. 
class Tape<SYM extends Symbol> {

    // Each cell of the tape holds a symbol.
    private final Map<Integer, SYM> cells = new TreeMap<>();
    private final SYM defaultValue;
    // Define the head position.
    private int head = 0;

    Tape() {
        this(Collections.emptyList(), null);
    }

    Tape(List<SYM> input) {
        this(input, null);
    }

    // Input: Provide the initial content of the tape and the starting state of the machine.
    Tape(List<SYM> input, SYM defaultValue) {
        for (int i = 0; i < input.size(); i++) {
            this.cells.put(i, input.get(i));
        }
        this.defaultValue = defaultValue;
    }

    SYM read() {
        return cells.getOrDefault(head, defaultValue);
    }

    void write(SYM symbol) {
        cells.put(head, symbol);
    }

    /**
     * Keep track of the position of the read/write head on the tape.
     */
    void moveHead(Direction direction) {
        switch (direction) {
        case LEFT:
            head--;
            break;
        case NONE:
            break;
        case RIGHT:
            head++;
            break;
        default:
            throw new IllegalArgumentException(direction.name());
        }
    }

    // for testing

    List<SYM> getCells() {
        List<SYM> values = cells.values().stream().collect(Collectors.toList());
        return Collections.unmodifiableList(values);
    }

    @Override
    public String toString() {
        String debug = "Tape: @" + head + " ";
        if (cells.containsKey(head - 1)) {
            debug += "[" + cells.get(head - 1) + "<]";
        }
        debug += "[" + cells.get(head) + "]";
        if (cells.containsKey(head + 1)) {
            debug += "[>" + cells.get(head + 1) + "]";
        }
        return debug;
    }
}
