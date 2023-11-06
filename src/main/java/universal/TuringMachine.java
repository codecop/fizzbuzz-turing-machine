package universal;

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
