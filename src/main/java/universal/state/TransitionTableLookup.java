package universal.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import universal.tape.Symbol;

public class TransitionTableLookup implements TransitionLookup {

    private final List<TransitionTableRow> rows;

    public TransitionTableLookup(List<TransitionTableRow> rows) {
        this.rows = rows;
    }

    @Override
    public Transition next(State state, Symbol symbol) {
        Optional<TransitionTableRow> row = rows.stream().//
                filter(r -> r.applies(state, symbol)). //
                findFirst();
        return row.map(r -> r.getTransition(state, symbol)) //
                .orElse(null);
    }

    public TransitionTableLookup add(TransitionTableLookup next) {
        List<TransitionTableRow> allRows = new ArrayList<>();
        allRows.addAll(this.rows);
        allRows.addAll(next.rows);
        return new TransitionTableLookup(allRows);
    }

}
