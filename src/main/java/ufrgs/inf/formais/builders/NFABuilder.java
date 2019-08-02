package ufrgs.inf.formais.builders;

import java.util.HashMap;
import java.util.HashSet;

import ufrgs.inf.formais.automata.NFA;
import ufrgs.inf.formais.helper.State;
import ufrgs.inf.formais.helper.StateSymbolTuple;
import ufrgs.inf.formais.helper.Symbol;

public class NFABuilder extends AutomatonBuilder {

	protected HashMap<StateSymbolTuple, HashSet<State>> transitionFunction;
	
	public NFABuilder() {
		this.alphabet = new HashSet<Symbol>();
		this.states = new HashSet<State>();
		this.finalStates = new HashSet<State>();
		this.transitionFunction = new HashMap<StateSymbolTuple, HashSet<State>>();
	}
	
	public void addTransitionFromStrings(String stateString, String symbolString, String destinyStateString) {
		State state = new State(stateString);
		Symbol symbol = new Symbol(symbolString);
		State destinyState = new State(destinyStateString);
		StateSymbolTuple stateSymbol = new StateSymbolTuple(state, symbol);
		if (!this.alphabet.contains(symbol) || !this.states.contains(state) || !this.states.contains(destinyState)) {
			// TODO: implement exceptions 
		} else {
			HashSet<State> destinies = (this.transitionFunction.containsKey(stateSymbol)) ? 
					this.transitionFunction.get(stateSymbol) : new HashSet<State>();
			destinies.add(destinyState);
			this.transitionFunction.put(stateSymbol, destinies);
		}
	}
	
	public void addTransition(State state, Symbol symbol, State destiny) {
		StateSymbolTuple stateSymbol = new StateSymbolTuple(state, symbol);
		if (!this.alphabet.contains(symbol) || !this.states.contains(state) || !this.states.contains(destiny)) {
			// TODO: implement exceptions 
		} else {
			HashSet<State> destinies = (this.transitionFunction.containsKey(stateSymbol)) ? 
					this.transitionFunction.get(stateSymbol) : new HashSet<State>();
			destinies.add(destiny);
			this.transitionFunction.put(stateSymbol, destinies);
		}
	}
	
	public NFA build() {
		return new NFA(name, alphabet, states, initialState, finalStates, transitionFunction);
	}
	
}
