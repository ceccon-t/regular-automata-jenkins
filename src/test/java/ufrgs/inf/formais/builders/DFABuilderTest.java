package ufrgs.inf.formais.builders;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ufrgs.inf.formais.automata.DFA;
import ufrgs.inf.formais.helper.Symbol;
import ufrgs.inf.formais.helper.Word;

public class DFABuilderTest {
	
	@Before
	public void setup() {
		
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void dfaFromBuilderRecognizesWordsThatEndWithA() {
		DFA automaton = dfaEndsWithAFromBuilder();
		String[] sequence = new String[] {"a", "b", "a", "b", "a", "b", "a"};
		Word word = new Word(Stream.of(sequence).map(Symbol::new).toArray(Symbol[]::new));
		
		assertTrue(automaton.recognize(word));
	}
	
	@Test
	public void dfaFromBuilderDoesNotRecognizeWordsThatDoNotEndWithA() {
		DFA automaton = dfaEndsWithAFromBuilder();
		String[] sequence = new String[] {"a", "a", "b", "a", "b"};
		Word word = new Word(Stream.of(sequence).map(Symbol::new).toArray(Symbol[]::new));
		
		assertFalse(automaton.recognize(word));
	}
	
    private DFA dfaEndsWithAFromBuilder() {
    	DFABuilder builder = new DFABuilder();
    	
    	builder.setName("Ends With A");
    	builder.addSymbolListToAlphabetFromString(Arrays.asList("a", "b"));
    	builder.addStateListFromString(Arrays.asList("q0", "q1"));
    	builder.setInitialStateFromString("q0");
    	builder.addFinalStateListFromString(Arrays.asList("q1"));
    	builder.addTransitionFromStrings("q0", "a", "q1");
    	builder.addTransitionFromStrings("q0", "b", "q0");
    	builder.addTransitionFromStrings("q1", "a", "q1");
    	builder.addTransitionFromStrings("q1", "b", "q0");
    	
    	return builder.build();
    }
}
