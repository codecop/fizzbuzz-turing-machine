package universal.state;

import java.util.function.Function;

import universal.tape.Direction;
import universal.tape.Symbol;

/**
 * Convenience functions.
 */
public class StringTransitionTable extends TransitionTable {

    private final Function<String, Symbol> symbolConverter;

    public StringTransitionTable(Function<String, Symbol> symbolConverter) {
        this.symbolConverter = symbolConverter;
    }

    public StringTransitionTable row(State state, String symbol, State newState, String newSymbol, String direction) {
        super.row(state, //
                convert(symbol), //
                newState, //
                convert(newSymbol), //
                fromString(direction));
        return this;
    }

    private Symbol convert(String symbol) {
        return isSymbol(symbol) ? symbolConverter.apply(symbol) : TransitionTableRow.anySymbol;
    }

    private boolean isSymbol(String symbol) {
        return symbol != null && symbol.length() > 0;
    }

    Direction fromString(String direction) {
        if (direction == null) {
            return Direction.NONE;
        }
        switch (direction.toLowerCase()) {
        case "l":
            return Direction.LEFT;
        case "":
        case " ":
        case "n":
            return Direction.NONE;
        case "r":
            return Direction.RIGHT;
        default:
            throw new IllegalArgumentException(direction);
        }
    }

}
