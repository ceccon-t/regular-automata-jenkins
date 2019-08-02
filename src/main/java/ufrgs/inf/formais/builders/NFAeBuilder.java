package ufrgs.inf.formais.builders;

import java.util.HashMap;
import java.util.HashSet;

import ufrgs.inf.formais.automata.NFAe;
import ufrgs.inf.formais.helper.State;
import ufrgs.inf.formais.helper.StateSymbolTuple;
import ufrgs.inf.formais.helper.Symbol;

public class NFAeBuilder extends NFABuilder {
	
	HashMap<State, HashSet<State>> epsilonTransitions;
	
	public NFAeBuilder() {
		this.alphabet = new HashSet<Symbol>();
		this.states = new HashSet<State>();
		this.finalStates = new HashSet<State>();
		this.transitionFunction = new HashMap<StateSymbolTuple, HashSet<State>>();
		this.epsilonTransitions = new HashMap<State, HashSet<State>>();
	}
	
	public void addEpsilonTransitionFromStrings(String originString, String destinyString) {
		State origin = new State(originString);
		State destiny = new State(destinyString);
		if (!this.states.contains(origin) || !this.states.contains(destiny)) {
			// TODO: implement exceptions 
		} else {
			HashSet<State> destinies = (this.epsilonTransitions.containsKey(origin)) ? this.epsilonTransitions.get(origin) : new HashSet<State>();
			destinies.add(destiny);
			this.epsilonTransitions.put(origin, destinies);
		}
	}
	
	public NFAe build() {
		return new NFAe(name, alphabet, states, initialState, finalStates, transitionFunction, epsilonTransitions);
	}

}
