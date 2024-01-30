package universal.tape;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

// Define the tape: Represent the infinite tape as a data structure, such as an array or a list. 
public class Tape<SYM extends Symbol> {

    // Each cell of the tape holds a symbol.
    private final Map<Integer, SYM> cells = new TreeMap<>();
    private final SYM defaultSymbol;
    // Define the head position.
    private int headPosition = 0;

    // Input: Provide the initial content of the tape and the starting state of the machine.
    public Tape(List<SYM> symbols, SYM defaultValue) {
        for (int i = 0; i < symbols.size(); i++) {
            this.cells.put(i, symbols.get(i));
        }
        this.defaultSymbol = defaultValue;
    }

    public SYM read() {
        return cells.getOrDefault(headPosition, defaultSymbol);
    }

    public void write(SYM symbol) {
        cells.put(headPosition, symbol);
    }

    /**
     * Keep track of the position of the read/write head on the tape.
     */
    public void moveHead(Direction direction) {
        switch (direction) {
        case LEFT:
            headPosition--;
            break;
        case NONE:
            break;
        case RIGHT:
            headPosition++;
            break;
        default:
            throw new IllegalArgumentException(direction.name());
        }
    }

    // display whole tape for testing

    public List<SYM> getCells() {
        List<SYM> values = cells.values().stream().collect(Collectors.toList());
        return Collections.unmodifiableList(values);
    }

    @Override
    public String toString() {
        String debug = "Tape: @" + headPosition + " ";
        debug += "[" + cells.get(headPosition - 1) + "<]";
        debug += "[" + cells.get(headPosition) + "]";
        debug += "[>" + cells.get(headPosition + 1) + "]";
        return debug;
    }
}
