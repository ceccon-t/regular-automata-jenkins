package ufrgs.inf.formais.automata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import ufrgs.inf.formais.helper.State;
import ufrgs.inf.formais.helper.Symbol;
import ufrgs.inf.formais.helper.StateSymbolTuple;
import ufrgs.inf.formais.helper.Word;

public class DFA extends Automaton{
	
	protected String type = "Deterministic Finite Automaton";
	
	protected String shortType = "DFA";
	
	private HashMap<StateSymbolTuple, State> transitionFunction;
	
	public DFA() {
		
	}
	
	public DFA(String name, 
			HashSet<Symbol> alphabet,
			HashSet<State> states, 
			State initialState, 
			HashSet<State> finalStates,
			HashMap<StateSymbolTuple, State> transitionFunction) {
		this.name = name;
		this.alphabet = alphabet;
		this.states = states;
		this.initialState = initialState;
		this.finalStates = finalStates;
		this.transitionFunction = transitionFunction;
	}

	public String getType() {
		return type;
	}

	public String getShortType() {
		return shortType;
	}

	public HashMap<StateSymbolTuple, State> getTransitionFunction() {
		return transitionFunction;
	}

	public void setTransitionFunction(HashMap<StateSymbolTuple, State> transitionFunction) {
		this.transitionFunction = transitionFunction;
	}
	
	public boolean recognize(List<Symbol> word) {
		State state = initialState;
		
		int pos = 0;
		
		while (pos < word.size()) {
			StateSymbolTuple transition = new StateSymbolTuple(state, word.get(pos));
			
			if (!transitionFunction.containsKey(transition)) {
				return false;
			}
			
			state = transitionFunction.get(transition);
			
			pos++;
		}
		
		return (finalStates.contains(state)) ? true : false ;
	}
	
	public boolean recognize(Word word) {
		return this.recognize(word.getSequence());
	}
	
	public String stringifyTransitionFunction() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n- Transition function: \n");
		for (Map.Entry<StateSymbolTuple, State> transition : transitionFunction.entrySet()) {
			StateSymbolTuple key = transition.getKey();
			State destiny = transition.getValue();
			sb.append("( " + key.getState() + " , " + key.getSymbol() + " ) => " + destiny + "\n");
		}
		sb.append("\n-End of transition function-\n");
		
		return sb.toString();
	}
	

}