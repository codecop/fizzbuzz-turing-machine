package universal.state;

import universal.tape.Direction;
import universal.tape.Symbol;

class TransitionTableRow {

    static final State anyState = null;
    static final Symbol anySymbol = null;

    private final State state;
    private final Symbol symbol;
    private final State newState;
    private final Symbol newSymbol;
    private final Direction direction;

    TransitionTableRow(State state, Symbol symbol, State newState, Symbol newSymbol, Direction direction) {
        this.state = state;
        this.symbol = symbol;
        this.newState = newState;
        this.newSymbol = newSymbol;
        this.direction = direction;
    }

    boolean applies(State currentState, Symbol currentSymbol) {
        return (state == anyState || state.equals(currentState)) && //
                (symbol == anySymbol || symbol.equals(currentSymbol));
    }

    Transition getTransition(State currentState, Symbol currentSymbol) {
        return new Transition(//
                newState != anyState ? newState : currentState, //
                newSymbol != anySymbol ? newSymbol : currentSymbol, //
                direction != null ? direction : Direction.NONE);
    }
}