package universal.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import universal.tape.Direction;
import universal.tape.Symbol;

/**
 * Container with builder for list of TransitionTableRows.
 */
public class TransitionTable implements TransitionLookup {

    private final Function<String, Symbol> symbolConverter;
    private final List<TransitionTableRow> rows = new ArrayList<>();

    public TransitionTable(Function<String, Symbol> symbolConverter) {
        this.symbolConverter = symbolConverter;
    }

    /**
     * Convenience function.
     */
    public TransitionTable row(State state, String symbol, State newState, String newSymbol, String direction) {
        return row(state, //
                isSymbol(symbol) ? symbolConverter.apply(symbol) : null, //
                newState, //
                isSymbol(newSymbol) ? symbolConverter.apply(newSymbol) : null, //
                Direction.fromString(direction));
    }

    private boolean isSymbol(String symbol) {
        return symbol != null && symbol.length() > 0;
    }

    public TransitionTable row(State state, Symbol symbol, State newState, Symbol newSymbol, Direction direction) {
        rows.add(new TransitionTableRow(state, symbol, newState, newSymbol, direction));
        return this;
    }

    public TransitionTable add(TransitionTable next) {
        rows.addAll(next.rows);
        return this;
    }

    @Override
    public Transition next(State state, Symbol symbol) {
        Optional<TransitionTableRow> row = rows.stream().//
                filter(r -> r.applies(state, symbol)). //
                findFirst();
        return row.map(r -> r.getTransition(state, symbol)) //
                .orElse(null);
    }

}
