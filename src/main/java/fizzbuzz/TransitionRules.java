package fizzbuzz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface TransitionRules {
    Transition next(State state, Symbol symbol);
}

/**
 * Split complex transitions into groups as Chain of Responsibility.
 */
abstract class TransitionRulesChain implements TransitionRules {

    private TransitionRulesChain nextChain;

    public TransitionRulesChain add(TransitionRulesChain chain) {
        if (this.nextChain == null) {
            this.nextChain = chain;
        } else {
            this.nextChain.add(chain);
        }
        return this;
    }

    @Override
    public Transition next(State state, Symbol symbol) {
        Transition transition = nextOrNull(state, symbol);
        if (transition != null) {
            return transition;
        }
        if (nextChain != null) {
            return nextChain.next(state, symbol);
        }
        throw new IllegalArgumentException("State " + state + " ; Symbol " + symbol);
    }

    protected abstract Transition nextOrNull(State state, Symbol symbol);

}

/**
 * Factory for TransitionRules using table rows.
 */
class TransitionTable {

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

    public TransitionRulesChain toRules() {
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

    public TransitionRow(State state, Symbol symbol, State newState, Symbol newSymbol, Direction direction) {
        this.state = state;
        this.symbol = symbol;
        this.newState = newState;
        this.newSymbol = newSymbol;
        this.direction = direction;
    }

    public boolean applies(State currentState, Symbol currentSymbol) {
        return (state == anyState || state.equals(currentState)) && //
                (symbol == anySymbol || symbol.equals(currentSymbol));
    }

    public Transition getTransition(State currentState, Symbol currentSymbol) {
        return new Transition(//
                newState != anyState ? newState : currentState, //
                newSymbol != anySymbol ? newSymbol : currentSymbol, //
                direction != null ? direction : Direction.NONE);
    }
}

class Transition {
    final State nextState;
    final Symbol symbolToWrite;
    final Direction directionToMove;

    public Transition(State nextState, Symbol symbolToWrite, Direction directionToMove) {
        this.nextState = nextState;
        this.symbolToWrite = symbolToWrite;
        this.directionToMove = directionToMove;
    }
}

class TransitionTableRules extends TransitionRulesChain {

    private final List<TransitionRow> rows;

    public TransitionTableRules(List<TransitionRow> rows) {
        this.rows = rows;
    }

    @Override
    protected Transition nextOrNull(State state, Symbol symbol) {
        Optional<TransitionRow> row = rows.stream().//
                filter(r -> r.applies(state, symbol)). //
                findFirst();
        return row.map(r -> r.getTransition(state, symbol)) //
                .orElse(null);
    }

}