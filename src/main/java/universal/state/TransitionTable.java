package universal.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import universal.tape.Direction;
import universal.tape.Symbol;

/**
 * Container with builder for list of TransitionTableRows.
 */
public class TransitionTable implements TransitionLookup {

    private final List<TransitionTableRow> rows = new ArrayList<>();

    public TransitionTable row(State state, Symbol symbol, State newState, Symbol newSymbol, Direction direction) {
        rows.add(new TransitionTableRow(state, symbol, newState, newSymbol, direction));
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
