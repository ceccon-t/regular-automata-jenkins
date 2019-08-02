package ufrgs.inf.formais.builders;

import java.util.HashSet;
import java.util.List;

import ufrgs.inf.formais.helper.State;
import ufrgs.inf.formais.helper.Symbol;

public abstract class AutomatonBuilder {

	protected String name;
	
	protected HashSet<Symbol> alphabet;
	
	protected HashSet<State> states;
	
	protected State initialState;
	
	protected HashSet<State> finalStates;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAlphabet(HashSet<Symbol> alphabet) {
		this.alphabet = alphabet;
	}
	
	public void addSymbolToAlphabetFromString(String symbol) {
		this.alphabet.add(new Symbol(symbol));
	}
	
	public void addSymbolToAlphabet(Symbol symbol) {
		this.alphabet.add(symbol);
	}
	
	public void addSymbolListToAlphabetFromString(List<String> symbols) {
		for (String symbol : symbols) {
			this.alphabet.add(new Symbol(symbol));
		}
	}
	
	public void addSymbolListToAlphabet(List<Symbol> symbols) {
		for (Symbol symbol : symbols) {
			this.alphabet.add(symbol);
		}
	}
	
	public void setStates(HashSet<State> states) {
		this.states = states;
	}
	
	public void addStateFromString(String state) {
		this.states.add(new State(state));
	}
	
	public void addState(State state) {
		this.states.add(state);
	}
	
	public void addStateListFromString(List<String> states) {
		for (String state : states) {
			this.states.add(new State(state));
		}
	}
	
	public void addStateList(List<State> states) {
		for (State state : states) {
			this.states.add(state);
		}
	}
	
	public void setInitialStateFromString(String initialStateName) {
		State initialState = new State(initialStateName);
		if (!this.states.contains(initialState)) {
			// TODO: implement exceptions 
			//throw new Exception("Tried to add a non-existing state as initial state during DFA creation");
		} else {
			this.initialState = initialState;
		}
	}
	
	public void setInitialState(State initialState) {
		if (!this.states.contains(initialState)) {
			// TODO: implement exceptions 
			//throw new Exception("Tried to add a non-existing state as initial state during DFA creation");
		} else {
			this.initialState = initialState;
		}
	}
	
	public void setFinalStates(HashSet<State> finalStates) {
		this.finalStates = finalStates;
	}
	
	public void addFinalStateFromString(String finalStateString) {
		State finalState = new State(finalStateString);
		if (!this.states.contains(finalState)) {
			// TODO: implement exceptions 
			//throw new Exception("Tried to add a non-existing state as initial state during DFA creation");
		} else {
			this.finalStates.add(finalState);
		}
	}
	
	public void addFinalState(State finalState) {
		if (!this.states.contains(finalState)) {
			// TODO: implement exceptions 
			//throw new Exception("Tried to add a non-existing state as initial state during DFA creation");
		} else {
			this.finalStates.add(finalState);
		}
	}
	
	public void addFinalStateListFromString(List<String> finalStateStrings) {
		for (String finalStateString : finalStateStrings) {
			State finalState = new State(finalStateString);
			if (!this.states.contains(finalState)) {
				// TODO: implement exceptions 
				//throw new Exception("Tried to add a non-existing state as initial state during DFA creation");
			} else {
				this.finalStates.add(finalState);
			}
		}
	}
	
	public void addFinalStateList(List<State> finalStates) {
		for (State finalState : finalStates) {
			if (!this.states.contains(finalState)) {
				// TODO: implement exceptions 
				//throw new Exception("Tried to add a non-existing state as initial state during DFA creation");
			} else {
				this.finalStates.add(finalState);
			}
		}
	}
	
}
