package universal;

public interface TransitionRules {
    
    Transition next(State state, Symbol symbol);
}
