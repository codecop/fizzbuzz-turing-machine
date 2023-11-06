package universal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import universal.tape.Symbol;

public class TransitionTableRules implements TransitionRules {

    private final List<TransitionRow> rows;

    public TransitionTableRules(List<TransitionRow> rows) {
        this.rows = rows;
    }

    @Override
    public Transition next(State state, Symbol symbol) {
        Optional<TransitionRow> row = rows.stream().//
                filter(r -> r.applies(state, symbol)). //
                findFirst();
        return row.map(r -> r.getTransition(state, symbol)) //
                .orElse(null);
    }

    public TransitionTableRules add(TransitionTableRules next) {
        List<TransitionRow> allRows = new ArrayList<>();
        allRows.addAll(this.rows);
        allRows.addAll(next.rows);
        return new TransitionTableRules(allRows);
    }

}
