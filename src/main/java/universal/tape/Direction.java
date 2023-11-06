package universal.tape;

/**
 * Direction to move the head on the tape.
 */
public enum Direction {
    LEFT, NONE, RIGHT;

    public static Direction fromString(String direction) {
        if (direction == null) {
            return NONE;
        }
        switch (direction.toLowerCase()) {
        case "l":
            return LEFT;
        case "":
        case " ":
        case "n":
            return NONE;
        case "r":
            return RIGHT;
        default:
            throw new IllegalArgumentException(direction);
        }
    }
}
