package universal.state;

import universal.tape.Symbol;

public interface TransitionLookup {

    Transition next(State state, Symbol symbol);
}
