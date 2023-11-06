package universal.state;

import universal.tape.Symbol;

public interface TransitionRules {

    Transition next(State state, Symbol symbol);
}
