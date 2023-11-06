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
    private final SYM defaultValue;
    // Define the head position.
    private int head = 0;

    Tape(List<SYM> input) {
        this(input, null);
    }

    // Input: Provide the initial content of the tape and the starting state of the machine.
    public Tape(List<SYM> input, SYM defaultValue) {
        for (int i = 0; i < input.size(); i++) {
            this.cells.put(i, input.get(i));
        }
        this.defaultValue = defaultValue;
    }

    public SYM read() {
        return cells.getOrDefault(head, defaultValue);
    }

    public void write(SYM symbol) {
        cells.put(head, symbol);
    }

    /**
     * Keep track of the position of the read/write head on the tape.
     */
    public void moveHead(Direction direction) {
        switch (direction) {
        case LEFT:
            head--;
            break;
        case NONE:
            break;
        case RIGHT:
            head++;
            break;
        default:
            throw new IllegalArgumentException(direction.name());
        }
    }

    // for testing

    public List<SYM> getCells() {
        List<SYM> values = cells.values().stream().collect(Collectors.toList());
        return Collections.unmodifiableList(values);
    }

    @Override
    public String toString() {
        String debug = "Tape: @" + head + " ";
        if (cells.containsKey(head - 1)) {
            debug += "[" + cells.get(head - 1) + "<]";
        }
        debug += "[" + cells.get(head) + "]";
        if (cells.containsKey(head + 1)) {
            debug += "[>" + cells.get(head + 1) + "]";
        }
        return debug;
    }
}
