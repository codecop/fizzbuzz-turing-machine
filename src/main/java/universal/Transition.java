package universal;

class Transition {

    final State nextState;
    final Symbol symbolToWrite;
    final Direction directionToMove;

    Transition(State nextState, Symbol symbolToWrite, Direction directionToMove) {
        this.nextState = nextState;
        this.symbolToWrite = symbolToWrite;
        this.directionToMove = directionToMove;
    }
}
