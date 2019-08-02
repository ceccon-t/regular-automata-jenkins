package ufrgs.inf.formais.automata;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ufrgs.inf.formais.helper.Symbol;
import ufrgs.inf.formais.helper.State;
import ufrgs.inf.formais.helper.StateSymbolTuple;
import ufrgs.inf.formais.helper.Word;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Stream;


public class DFATest {
	
	@Before
	public void setup() {
		
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void recognizesWordsThatEndWithA() {
		DFA automaton = dfaEndsWithA();
		String[] sequence = new String[] {"a", "b", "a", "b", "a", "b", "a"};
		Word word = new Word(Stream.of(sequence).map(Symbol::new).toArray(Symbol[]::new));
		
		assertTrue(automaton.recognize(word));
	}
	
	@Test
	public void doesNotRecognizeWordsThatDoNotEndWithA() {
		DFA automaton = dfaEndsWithA();
		String[] sequence = new String[] {"a", "a", "b", "a", "b"};
		Word word = new Word(Stream.of(sequence).map(Symbol::new).toArray(Symbol[]::new));
		
		assertFalse(automaton.recognize(word));
	}
	
	private DFA dfaEndsWithA() {
		String name = "Ends With A";
		HashSet<Symbol> alphabet = new HashSet<Symbol>();
		Symbol a = new Symbol("a");
		Symbol b = new Symbol("b");
		alphabet.add(a);
		alphabet.add(b);
		HashSet<State> states = new HashSet<State>();
		State q0 = new State("q0");
		State q1 = new State("q1");
		states.add(q0);
		states.add(q1);
		State initialState = q0;
		HashSet<State> finalStates = new HashSet<State>();
		finalStates.add(q1);
		HashMap<StateSymbolTuple, State> transitionFunction = new HashMap<StateSymbolTuple, State>();
		transitionFunction.put(new StateSymbolTuple(q0, a), q1);
		transitionFunction.put(new StateSymbolTuple(q0, b), q0);
		transitionFunction.put(new StateSymbolTuple(q1, a), q1);
		transitionFunction.put(new StateSymbolTuple(q1, b), q0);
		
		return new DFA(name, alphabet, states, initialState, finalStates, transitionFunction);
	}
}