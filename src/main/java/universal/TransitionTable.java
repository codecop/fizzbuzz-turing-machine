package universal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Factory for TransitionRules using table rows.
 */
public class TransitionTable {

    private final Function<String, Symbol> converter;
    private final List<TransitionRow> rows = new ArrayList<>();

    public TransitionTable() {
        this(s -> {
            throw new IllegalArgumentException(s);
        });
    }

    public TransitionTable(Function<String, Symbol> converter) {
        this.converter = converter;
    }

    /**
     * Convenience function.
     */
    public TransitionTable row(State state, String symbol, State newState, String newSymbol, String direction) {
        return row(state, //
                isSymbol(symbol) ? converter.apply(symbol) : null, //
                newState, //
                isSymbol(newSymbol) ? converter.apply(newSymbol) : null, //
                Direction.fromString(direction));
    }

    private boolean isSymbol(String symbol) {
        return symbol != null && symbol.length() > 0;
    }

    public TransitionTable row(State state, Symbol symbol, State newState, Symbol newSymbol, Direction direction) {
        rows.add(new TransitionRow(state, symbol, newState, newSymbol, direction));
        return this;
    }

    public TransitionTableRules toRules() {
        return new TransitionTableRules(Collections.unmodifiableList(rows));
    }
}

class TransitionRow {

    static final State anyState = null;
    static final Symbol anySymbol = null;

    private final State state;
    private final Symbol symbol;
    private final State newState;
    private final Symbol newSymbol;
    private final Direction direction;

    TransitionRow(State state, Symbol symbol, State newState, Symbol newSymbol, Direction direction) {
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
