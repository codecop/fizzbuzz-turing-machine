package universal.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import universal.tape.Direction;
import universal.tape.Symbol;

/**
 * Factory for TransitionRules using table rows.
 */
public class TransitionTable {

    private final Function<String, Symbol> converter;
    private final List<TransitionTableRow> rows = new ArrayList<>();

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
        rows.add(new TransitionTableRow(state, symbol, newState, newSymbol, direction));
        return this;
    }

    public TransitionTableRules toRules() {
        return new TransitionTableRules(Collections.unmodifiableList(rows));
    }
}
