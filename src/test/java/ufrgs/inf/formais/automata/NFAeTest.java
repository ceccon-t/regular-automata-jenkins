package ufrgs.inf.formais.automata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import ufrgs.inf.formais.helper.State;
import ufrgs.inf.formais.helper.StateSymbolTuple;
import ufrgs.inf.formais.helper.Symbol;
import ufrgs.inf.formais.helper.Word;

public class NFAeTest {
	
	@Before
	public void setup() {
		
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void correctlyIncludesOriginStateOnEpsilonClosure() {
		NFAe testNFAe = zeroToTheAOneToTheBTwoToTheCNFAe();
		State stateWithNoEmptyTransitions = new State("q2");
		HashSet<State> epsilonClosureOfState = testNFAe.getEpsilonClosure(stateWithNoEmptyTransitions);
		
		assertEquals(new HashSet<>(Arrays.asList(stateWithNoEmptyTransitions)), epsilonClosureOfState);
	}
	
	@Test
	public void correctlyCalculatesEpsilonClosureWithDepthOne() {
		NFAe testNFAe = zeroToTheAOneToTheBTwoToTheCNFAe();
		State stateWithEmptyTransitionOfDepthOne = new State("q1");
		HashSet<State> epsilonClosureOfState = testNFAe.getEpsilonClosure(stateWithEmptyTransitionOfDepthOne);
		
		assertEquals(new HashSet<>(Arrays.asList(stateWithEmptyTransitionOfDepthOne, new State("q2"))), epsilonClosureOfState);
	}
	
	@Test
	public void correctlyCalculatesEpsilonClosureWithDepthTwo() {
		NFAe testNFAe = zeroToTheAOneToTheBTwoToTheCNFAe();
		State stateWithEmptyTransitionOfDepthTwo = new State("q0");
		HashSet<State> epsilonClosureOfState = testNFAe.getEpsilonClosure(stateWithEmptyTransitionOfDepthTwo);
		
		assertEquals(new HashSet<>(Arrays.asList(stateWithEmptyTransitionOfDepthTwo, new State("q1"), new State("q2"))), epsilonClosureOfState);
	}
	
	@Test
	public void correctlyRecognizesWordThatBelongsToLanguage() {
		NFAe testNFAe = zeroToTheAOneToTheBTwoToTheCNFAe();
		assertTrue(testNFAe.recognize(new Word("000111222", "")));
	}
	
	@Test
	public void correctlyDoesNotRecognizeWordThatDoesNotBelongToLanguage() {
		NFAe testNFAe = zeroToTheAOneToTheBTwoToTheCNFAe();
		assertFalse(testNFAe.recognize(new Word("000102", "")));
	}
	
	private NFAe zeroToTheAOneToTheBTwoToTheCNFAe() {
		/* NFAe to recognize words of the form 0^a1^b2^c, with a, b and c >= 0
		 * 
		 * Basically, q0 reads arbitrarily many 0s, q1 the same for 1s and q2 for 2s.
		 * q0 connects to q1 through an epsilon transition and q1 to q2.
		 * Because there is no connection back, the concatenation order is recognized correctly.
		 * 
		 * 0 to the A, 1 to the B, 2 to the C=({0,1,2},{q0,q1,q2},q0,{q0,q1,q2})
		 * Prog
		 * (q0,0)=q0
		 * (q0,)=q1
		 * (q1,1)=q1
		 * (q1,)=q2
		 * (q2,2)=q2
		*/
		String name = "0 to the A, 1 to the B, 2 to the C";
		String[] symbolRepresentations = new String[] {"0", "1", "2"};
		HashSet<Symbol> alphabet = new HashSet<Symbol>(Stream.of(symbolRepresentations).map(Symbol::new).collect(Collectors.toList()));
		State q0 = new State("q0");
		State q1 = new State("q1");
		State q2 = new State("q2");
		HashSet<State> states = new HashSet<State>(Arrays.asList(q0, q1, q2));
		State initialState = q0;
		HashSet<State> finalStates = states;
		HashMap<StateSymbolTuple, HashSet<State>> transitionFunction = new HashMap<>();
		transitionFunction.put(new StateSymbolTuple(q0, new Symbol("0")), new HashSet<State>(Arrays.asList(q0)));
		transitionFunction.put(new StateSymbolTuple(q1, new Symbol("1")), new HashSet<State>(Arrays.asList(q1)));
		transitionFunction.put(new StateSymbolTuple(q2, new Symbol("2")), new HashSet<State>(Arrays.asList(q2)));
		HashMap<State, HashSet<State>> epsilonTransitions = new HashMap<>();
		epsilonTransitions.put(q0, new HashSet<>(Arrays.asList(q1)));
		epsilonTransitions.put(q1, new HashSet<>(Arrays.asList(q2)));
		
		return new NFAe(name, alphabet, states, initialState, finalStates, transitionFunction, epsilonTransitions);
	}

}
