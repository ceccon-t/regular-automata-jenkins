package ufrgs.inf.formais.helper;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class StateSymbolTuple {
	
	private ImmutablePair<State, Symbol> pair;
	
	public StateSymbolTuple(State state, Symbol symbol) {
		this.pair = new ImmutablePair<State, Symbol>(state, symbol);
	}
	
	public ImmutablePair<State, Symbol> getPair() {
		return pair;
	}

	public State getState() {
		return this.pair.getLeft();
	}
	
	public Symbol getSymbol() {
		return this.pair.getRight();
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof StateSymbolTuple) ? this.pair.equals( ( (StateSymbolTuple) o).getPair() ) : false ;
	}
	
	@Override
	public int hashCode() {
		return this.pair.hashCode();
	}

}
