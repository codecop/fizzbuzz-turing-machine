package universal.state;

import universal.tape.Direction;
import universal.tape.Symbol;

public class Transition {

    public final State nextState;
    public final Symbol symbolToWrite;
    public final Direction directionToMove;

    Transition(State nextState, Symbol symbolToWrite, Direction directionToMove) {
        this.nextState = nextState;
        this.symbolToWrite = symbolToWrite;
        this.directionToMove = directionToMove;
    }

}
