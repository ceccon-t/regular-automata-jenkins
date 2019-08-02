package ufrgs.inf.formais.builders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

import ufrgs.inf.formais.automata.DFA;
import ufrgs.inf.formais.automata.NFA;
import ufrgs.inf.formais.automata.NFAe;
import ufrgs.inf.formais.helper.CompositeState;
import ufrgs.inf.formais.helper.State;
import ufrgs.inf.formais.helper.StateSymbolTuple;
import ufrgs.inf.formais.helper.Symbol;

public class AutomataConverter {
	
	public static DFA nfaToDfa(NFA nfa)  {
		
		if (nfa.isDeterministic()) return convertDeterministicNfa(nfa);
		
		DFABuilder builder = new DFABuilder();
		builder.setName(nfa.getName() + " | Converted to DFA");
		HashSet<Symbol> alphabet = nfa.getAlphabet(); 
		builder.setAlphabet(alphabet);
		
		HashMap<StateSymbolTuple, HashSet<State>> oldTransitionFunction = nfa.getTransitionFunction();
		
		HashSet<State> newStates = new HashSet<State>();
		HashSet<State> newFinalStates = new HashSet<State>();
		
		// Maps from the set of states of nfa to the corresponding state to be put in dfa
		HashMap<CompositeState, State> collapsedStates = new HashMap<CompositeState, State>();
		int countCollapsed = 0;
		
		// Queue to control traversal over nfa transition function
		LinkedList<CompositeState> statesToVisit = new LinkedList<CompositeState>();
		
		// Transition function of final dfa
		HashMap<StateSymbolTuple, State> newTransitionFunction = new HashMap<StateSymbolTuple, State>();
		
		// Sets up traversal
		CompositeState newInitialState = new CompositeState(new HashSet<State>(Arrays.asList(nfa.getInitialState())));
		collapsedStates.put(newInitialState, newInitialState.collapse(countCollapsed++));
		newStates.add(collapsedStates.get(newInitialState));
		
		statesToVisit.add(newInitialState);
		
		// breadth-first search, seeing transition function as a graph, starting from initial state
		// and checking all possible destination states from current context, generating the properties
		// to be put on final dfa along the way
		while (!statesToVisit.isEmpty()) {
			CompositeState current = statesToVisit.removeFirst();
			for (Symbol symbol : alphabet) {
				HashSet<State> allReachable = new HashSet<State>();
				for (State state : current.getStates()) {
					StateSymbolTuple tuple = new StateSymbolTuple(state, symbol);
					if (oldTransitionFunction.containsKey(tuple)) {
						allReachable.addAll(oldTransitionFunction.get(tuple));
					}
				}
				if (allReachable.size() > 0) {
					CompositeState newComposite = new CompositeState(allReachable);
					if (!collapsedStates.containsKey(newComposite)) { // first time found, should be added everywhere
						State collapsed = newComposite.collapse(countCollapsed++);
						collapsedStates.put(newComposite, collapsed);
						newStates.add(collapsed);
						if (compositeStateContainsFinalState(newComposite, nfa)) newFinalStates.add(collapsed);
						statesToVisit.addLast(newComposite); // adds to the end of the queue only if not found yet
					}
					newTransitionFunction.put(new StateSymbolTuple(collapsedStates.get(current), symbol), collapsedStates.get(newComposite)); 
				}
			}
		}
		
		// traversal over, properties already set
		builder.setStates(newStates);
		builder.setInitialState(collapsedStates.get(newInitialState));
		builder.setFinalStates(newFinalStates);
		
		// goes through builder method to add transition just to double check that no invalid
		// transition has been generated during the creation of new transition function
		for (Map.Entry<StateSymbolTuple, State> transition : newTransitionFunction.entrySet()) {
			StateSymbolTuple tuple = transition.getKey();
			State origin = tuple.getState();
			Symbol symbol = tuple.getSymbol();
			State destiny = transition.getValue();
			builder.addTransition(origin, symbol, destiny);
		}
		
		return builder.build();
		
	}
	
	public static NFA nfaeToNfa(NFAe nfae) {
		
		// Reusable things
		String newName = nfae.getName() + " | Converted to NFA";
		HashSet<Symbol> alphabet = nfae.getAlphabet();
		
		// Old things that need to be accessed frequently
		HashSet<State> oldStates = nfae.getStates();
		HashMap<StateSymbolTuple, HashSet<State>> oldTransitionFunction = nfae.getTransitionFunction();
		HashSet<State> oldFinalStates = nfae.getFinalStates();
		
		if (nfae.hasNoEpsilonTransitions()) {
			return new NFA(newName, alphabet, nfae.getStates(), nfae.getInitialState(), nfae.getFinalStates(), nfae.getTransitionFunction());
		}
		
		// For the third-dimension trick
		CompositeState compositeNewInitialState = new CompositeState(nfae.getEpsilonClosure(nfae.getInitialState()));
		State newInitialState = compositeNewInitialState.collapse(0);
		
		NFABuilder builder = new NFABuilder();
		
		builder.setName(newName);
		builder.setAlphabet(alphabet);
		
		// Initializes new states and set new initial state
		HashSet<State> newStates = new HashSet<State>();
		HashSet<State> newFinalStates = new HashSet<State>();
		
		// Add everything from NFAe into new NFA properties
		newStates.addAll(oldStates);
		newStates.add(newInitialState);
		newFinalStates.addAll(nfae.getFinalStates());
		if (compositeStateContainsFinalState(compositeNewInitialState, oldFinalStates)) {
			newFinalStates.add(newInitialState);
		}
		
		builder.setStates(newStates);
		builder.setInitialState(newInitialState);
		builder.setFinalStates(newFinalStates);
		
		for (Map.Entry<StateSymbolTuple, HashSet<State>> entry : nfae.getTransitionFunction().entrySet() ) {
			//newTransitionFunction.put(entry.getKey(), entry.getValue());
			StateSymbolTuple key = entry.getKey();
			HashSet<State> destinies = entry.getValue();
			for (State destinyState : destinies) {
				builder.addTransition(key.getState(), key.getSymbol(), destinyState);
			}
		}
		
		// Expand transition function
		for (Symbol symbol : alphabet) {
			
			// create new transitions for new initial state on third dimension
			HashSet<State> destiniesForInitialState = new HashSet<State>();
			for (State internalState : compositeNewInitialState.getStates()) {
				StateSymbolTuple tuple = new StateSymbolTuple(internalState, symbol);
				HashSet<State> partialDestinies = oldTransitionFunction.get(tuple);
				if (partialDestinies != null) {
					destiniesForInitialState.addAll(partialDestinies);
					HashSet<State> epsilonClosuresOfPartialDestinies = new HashSet<State>();
					for (State destiny : partialDestinies) {
						HashSet<State> epsilonClosure = nfae.getEpsilonClosure(destiny);
						if (epsilonClosure != null) {
							epsilonClosuresOfPartialDestinies.addAll(epsilonClosure);
						}
					}
					destiniesForInitialState.addAll(epsilonClosuresOfPartialDestinies);
				}
			}
			for (State destinyForInitialState : destiniesForInitialState) {
				builder.addTransition(newInitialState, symbol, destinyForInitialState);
			}
			
			// expand existing transitions to contain the epsilon closure of all destinies as well
			for (State state : oldStates) {
				StateSymbolTuple tuple = new StateSymbolTuple(state, symbol);
				if (oldTransitionFunction.containsKey(tuple)) {
					HashSet<State> oldDestiniesForTransition = oldTransitionFunction.get(tuple);
					
					// get epsilon closures for all possible destinies
					HashSet<State> epsilonClosuresOfDestinies = new HashSet<State>();
					for (State destiny : oldDestiniesForTransition) {
						HashSet<State> epsilonClosure = nfae.getEpsilonClosure(destiny);
						if (epsilonClosure != null) {
							epsilonClosuresOfDestinies.addAll(epsilonClosure);
						}
					}
					
					// add closures as destinies in new transition function
					for (State newDestiny : epsilonClosuresOfDestinies) {
						builder.addTransition(state, symbol, newDestiny);
					}
					
				}
			}
			
		}
		
		
		return builder.build();
	}

	public static DFA nfaeToDfa(NFAe nfae) {
		// reuse existing conversions
		return nfaToDfa(nfaeToNfa(nfae));
	}
	
	public static NFAe nfaToNfae(NFA nfa) {
		// create a new NFAe with same properties, but empty epsilon transitions
		return new NFAe(nfa.getName() + " | Converted to NFAe", nfa.getAlphabet(), nfa.getStates(), nfa.getInitialState(), nfa.getFinalStates(), nfa.getTransitionFunction(),
						new HashMap<State, HashSet<State>>());
	}
	
	public static NFA dfaToNfa(DFA dfa) {
		// almost everything is the same, just need to make mapping to a set of destiny states instead of a single destiny state
		HashMap<StateSymbolTuple, HashSet<State>> newTransitionFunction = new HashMap<StateSymbolTuple, HashSet<State>>();
		for (Map.Entry<StateSymbolTuple, State> transition : dfa.getTransitionFunction().entrySet()) {
			HashSet<State> destiny = new HashSet<State>();
			destiny.add(transition.getValue());
			newTransitionFunction.put(transition.getKey(), destiny);
		}
		return new NFA(dfa.getName() + " | Converted to NFA", dfa.getAlphabet(), dfa.getStates(), dfa.getInitialState(), dfa.getFinalStates(), newTransitionFunction);
	}
	
	public static NFAe dfaToNfae(DFA dfa) {
		// reuse existing conversions
		return nfaToNfae(dfaToNfa(dfa));
	}
	
	// NFAe directly to DFA ???? 
	/*
	public static NFA nfaeToNfa(NFAe nfae) {
		
		// Reusable things
		String newName = nfae.getName() + " | Converted to NFA";
		HashSet<Symbol> alphabet = nfae.getAlphabet();
		
		// Old things that need to be accessed frequently
		HashMap<StateSymbolTuple, HashSet<State>> oldTransitionFunction = nfae.getTransitionFunction();
		HashMap<State, HashSet<State>> epsilonTransitions = nfae.getEpsilonTransitions();
		HashSet<State> oldFinalStates = nfae.getFinalStates();
		
		
		if (nfae.hasNoEpsilonTransitions()) {
			return new NFA(newName, alphabet, nfae.getStates(), nfae.getInitialState(), nfae.getFinalStates(), nfae.getTransitionFunction());
		}
		
		NFABuilder builder = new NFABuilder();
		
		builder.setName(newName);
		builder.setAlphabet(alphabet);
		
		// Initializes new states and set new initial state
		HashSet<State> newStates = new HashSet<State>();
		HashSet<State> newFinalStates = new HashSet<State>();
		HashMap<StateSymbolTuple, HashSet<State>> newTransitionFunction = new HashMap<StateSymbolTuple, HashSet<State>>();
		
		
		HashMap<CompositeState, State> collapsedStates = new HashMap<CompositeState, State>();
		int countCollapsed = 0;
		
		LinkedList<CompositeState> statesToVisit = new LinkedList<CompositeState>();
		
		CompositeState compositeNewInitialState = new CompositeState(nfae.getEpsilonClosure(nfae.getInitialState()));
		collapsedStates.put(compositeNewInitialState, compositeNewInitialState.collapse(countCollapsed++));
		statesToVisit.add(compositeNewInitialState);
		
		// Create new transition function, updating states and final states
		while (!statesToVisit.isEmpty()) {
			// Assumptions to ensure: current already is present on both newStates and collapsedStates
			CompositeState current = statesToVisit.removeFirst();
			
			for (Symbol symbol : alphabet) {
				
				HashSet<State> possibleDestinies = new HashSet<State>();
				
				// get possible destinies when reading this symbol on this state
				for (State internalState : current.getStates()) {
					StateSymbolTuple tuple = new StateSymbolTuple(internalState, symbol);
					HashSet<State> partialDestinies = oldTransitionFunction.get(tuple);
					if (partialDestinies != null) {
						possibleDestinies.addAll(partialDestinies);
					}
				}
				
				// get all possible epsilon transitions for each possible destiny state
				HashSet<State> epsilonClosuresOfDestinies = new HashSet<State>();
				for (State possibleDestiny : possibleDestinies) {
					HashSet<State> epsilonClosure = nfae.getEpsilonClosure(possibleDestiny);
					if (epsilonClosure != null) {
						epsilonClosuresOfDestinies.addAll(epsilonClosure);
					}
				}
				
				// add epsilon closures of destinies to possible destinies
				possibleDestinies.addAll(epsilonClosuresOfDestinies);
				
				CompositeState destinyState = new CompositeState(possibleDestinies);
				
				// if this new state had never been seen before, it needs to be placed on collapsedStates, newStates and set to be visited later
				if (!collapsedStates.containsKey(destinyState)) {
					collapsedStates.put(destinyState, destinyState.collapse(countCollapsed++));
					newStates.add(collapsedStates.get(destinyState));
					statesToVisit.addLast(destinyState);
					if (compositeStateContainsFinalState(destinyState, oldFinalStates)) {
						newFinalStates.add(collapsedStates.get(destinyState));
					}
				}
				
				// add transition
				
				
				
			}
		}
		
		
		
		return builder.build();
	}
	*/
	
	private static DFA convertDeterministicNfa(NFA nfa) {
		DFABuilder builder = new DFABuilder();
		builder.setName(nfa.getName() + " | Converted to DFA");
		builder.setAlphabet(nfa.getAlphabet());
		builder.setStates(nfa.getStates());
		builder.setInitialState(nfa.getInitialState());
		builder.setFinalStates(nfa.getFinalStates());
		
		for (Map.Entry<StateSymbolTuple, HashSet<State>> transition : nfa.getTransitionFunction().entrySet()) {
			StateSymbolTuple computation = transition.getKey();
			State destiny = transition.getValue().stream().collect(Collectors.toList()).get(0);
			builder.addTransition(computation.getState(), computation.getSymbol(), destiny);
		}
		
		return builder.build();
	}
	
	private static Boolean compositeStateContainsFinalState(CompositeState cs, NFA nfa) {
		Boolean containsFinalState = false;
		for (State state : cs.getStates()) {
			if (nfa.getFinalStates().contains(state)) {
				containsFinalState = true;
			}
		}
		return containsFinalState;
	}
	
	private static Boolean compositeStateContainsFinalState(CompositeState cs, HashSet<State> finalStates) {
		Boolean containsFinalState = false;
		for (State state : cs.getStates()) {
			if (finalStates.contains(state)) {
				containsFinalState = true;
			}
		}
		return containsFinalState;
	}

}
