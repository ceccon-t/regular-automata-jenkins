package ufrgs.inf.formais.automata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ufrgs.inf.formais.helper.State;
import ufrgs.inf.formais.helper.StateSymbolTuple;
import ufrgs.inf.formais.helper.Symbol;

public class NFATest {
	
	@Before
	public void setup() {
		
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void correctlyIdentifiesIfIsDeterministic() {
		NFA deterministic = deterministicNFA();
		assertTrue(deterministic.isDeterministic());
	}
	
	@Test
	public void correctlyIdentifiesIfIsNotDeterministic() {
		NFA nonDeterministic = nonDeterministicNFA();
		assertFalse(nonDeterministic.isDeterministic());
	}
	
	
	private NFA deterministicNFA() {
		String name = "Deterministic NFA";
		Symbol a = new Symbol("a");
		HashSet<Symbol> alphabet = new HashSet<Symbol>(Arrays.asList(a));
		State q0 = new State("q0");
		State q1 = new State("q1");
		HashSet<State> states = new HashSet<State>(Arrays.asList(q0, q1));
		State initialState = q0;
		HashSet<State> finalStates = new HashSet<State>(Arrays.asList(q0));
		HashMap<StateSymbolTuple, HashSet<State>> transitionFunction = new HashMap<StateSymbolTuple, HashSet<State>>();
		transitionFunction.put(new StateSymbolTuple(q0, a), new HashSet<State>(Arrays.asList(q1)));
		return new NFA(name, alphabet, states, initialState, finalStates, transitionFunction);
	}

	private NFA nonDeterministicNFA() {
		String name = "Non-Deterministic NFA";
		Symbol a = new Symbol("a");
		HashSet<Symbol> alphabet = new HashSet<Symbol>(Arrays.asList(a));
		State q0 = new State("q0");
		State q1 = new State("q1");
		HashSet<State> states = new HashSet<State>(Arrays.asList(q0, q1));
		State initialState = q0;
		HashSet<State> finalStates = new HashSet<State>(Arrays.asList(q0));
		HashMap<StateSymbolTuple, HashSet<State>> transitionFunction = new HashMap<StateSymbolTuple, HashSet<State>>();
		HashSet<State> atQ0ReadingAGoesTo = new HashSet<State>(Arrays.asList(q0, q1));
		transitionFunction.put(new StateSymbolTuple(q0, a), atQ0ReadingAGoesTo);
		return new NFA(name, alphabet, states, initialState, finalStates, transitionFunction);
	}
	
}
