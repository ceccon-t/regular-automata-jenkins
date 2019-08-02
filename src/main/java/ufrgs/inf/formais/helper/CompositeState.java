package ufrgs.inf.formais.helper;

import java.util.HashSet;

public class CompositeState {
	
	private HashSet<State> states;
	
	public HashSet<State> getStates() {
		return states;
	}

	public void setStates(HashSet<State> states) {
		this.states = states;
	}

	public CompositeState(HashSet<State> states) {
		this.states = states;
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof CompositeState) ? this.states.equals(  ((CompositeState)o).getStates() ) : false ;
	}
	
	@Override
	public int hashCode() {
		return this.states.hashCode();
	}
	
	public State collapse(Integer number) {
		StringBuilder sb = new StringBuilder();
		sb.append("CS" + number.toString() + ">");
		
		for (State state : this.states) {
			sb.append(state.getName());
		}
		
		return new State(sb.toString());
		
	}

}
