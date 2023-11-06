package universal;

/**
 * Split complex transitions into groups as Chain of Responsibility.
 */
public abstract class TransitionRulesChain implements TransitionRules {

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
