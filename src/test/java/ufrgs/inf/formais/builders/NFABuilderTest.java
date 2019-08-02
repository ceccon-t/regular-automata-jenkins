package ufrgs.inf.formais.builders;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ufrgs.inf.formais.automata.NFA;

public class NFABuilderTest {
	
	@Before
	public void setup() {
		
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void correctlyIdentifiesIfIsDeterministicFromBuilder() {
		NFA deterministic = deterministicNFAFromBuilder();
		assertTrue(deterministic.isDeterministic());
	}
	
	@Test
	public void correctlyIdentifiesIfIsNotDeterministicFromBuilder() {
		NFA nonDeterministic = nonDeterministicNFAFromBuilder();
		assertFalse(nonDeterministic.isDeterministic());
	}
	
	private NFA deterministicNFAFromBuilder() {
		NFABuilder builder = new NFABuilder();
		
		builder.setName("Deterministic NFA From Builder");
		builder.addSymbolListToAlphabetFromString(Arrays.asList("a"));
		builder.addStateListFromString(Arrays.asList("q0", "q1"));
		builder.setInitialStateFromString("q0");
		builder.addFinalStateListFromString(Arrays.asList("q0"));
		builder.addTransitionFromStrings("q0", "a", "q1");
		
		return builder.build();
	}

	private NFA nonDeterministicNFAFromBuilder() {
		NFABuilder builder = new NFABuilder();
		
		builder.setName("Deterministic NFA From Builder");
		builder.addSymbolListToAlphabetFromString(Arrays.asList("a"));
		builder.addStateListFromString(Arrays.asList("q0", "q1"));
		builder.setInitialStateFromString("q0");
		builder.addFinalStateListFromString(Arrays.asList("q0"));
		builder.addTransitionFromStrings("q0", "a", "q0");
		builder.addTransitionFromStrings("q0", "a", "q1");
		
		return builder.build();
	}

}
