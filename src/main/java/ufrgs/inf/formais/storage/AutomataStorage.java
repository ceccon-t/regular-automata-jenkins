package ufrgs.inf.formais.storage;

import java.io.File;
import java.io.IOException;

import ufrgs.inf.formais.automata.Automaton;
import ufrgs.inf.formais.automata.NFA;
import ufrgs.inf.formais.automata.NFAe;
import ufrgs.inf.formais.builders.AutomataConverter;

public class AutomataStorage {
	
	public static Automaton load(File file) throws IOException {
		Automaton automaton;
		
		NFAe nfae = NFAeStorage.load(file);
		String originalName = nfae.getName();
		
		if (nfae.hasNoEpsilonTransitions()) {
			NFA nfa = AutomataConverter.nfaeToNfa(nfae);
			automaton = nfa;
			if (nfa.isDeterministic()) {
				automaton = AutomataConverter.nfaToDfa(nfa);
			}
		} else {
			automaton = nfae;
		}
		
		// In this specific case there is no need to indicate that the automaton was converted on its name
		automaton.setName(originalName);
		return automaton;
	}

}
