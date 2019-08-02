package ufrgs.inf.formais.automata;

import java.util.HashSet;

import ufrgs.inf.formais.helper.State;
import ufrgs.inf.formais.helper.Symbol;
import ufrgs.inf.formais.helper.Word;


public abstract class Automaton {

	protected String type = "Automaton";
	
	protected String shortType = "FA";
	
	protected String name;
	
	protected HashSet<Symbol> alphabet;
	
	protected HashSet<State> states;
	
	protected State initialState;
	
	protected HashSet<State> finalStates;

	public String getType() {
		return type;
	}

	public String getShortType() {
		return shortType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashSet<Symbol> getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(HashSet<Symbol> alphabet) {
		this.alphabet = alphabet;
	}

	public HashSet<State> getStates() {
		return states;
	}

	public void setStates(HashSet<State> states) {
		this.states = states;
	}

	public State getInitialState() {
		return initialState;
	}

	public void setInitialState(State initialState) {
		this.initialState = initialState;
	}

	public HashSet<State> getFinalStates() {
		return finalStates;
	}

	public void setFinalStates(HashSet<State> finalStates) {
		this.finalStates = finalStates;
	}
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n===\n");
		sb.append("AUTOMATON " + this.hashCode() + ":\n");
		sb.append("Name: " + this.getName() + "\n");
		sb.append("Type: " + this.getType() + "\n");
		
		// Alphabet
		sb.append("Alphabet:");
		for (Symbol symbol : this.getAlphabet()) {
			sb.append(" '" + symbol + "'");
		} 
		sb.append("\n");
		
		// States
		sb.append("States:");
		for (State state : this.getStates()) {
			sb.append(" '" + state + "'");
		}
		sb.append("\n");
		
		sb.append("Initial state: " + this.getInitialState() + "\n");
		
		// Final states
		sb.append("Final states:");
		for (State finalState: this.getFinalStates()) {
			sb.append(" '" + finalState + "'");
		}
		sb.append("\n");
		
		sb.append(this.stringifyTransitionFunction());
		
		sb.append("\n===\n");
		
		return sb.toString();
	}
	
	public abstract boolean recognize(Word word);
	
	public abstract String stringifyTransitionFunction();
	
}
