package ufrgs.inf.formais.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import ufrgs.inf.formais.automata.NFAe;
import ufrgs.inf.formais.builders.NFAeBuilder;
import ufrgs.inf.formais.helper.State;
import ufrgs.inf.formais.helper.StateSymbolTuple;
import ufrgs.inf.formais.helper.Symbol;

public class NFAeStorage {
	
	public static void save(NFAe nfae, String pathToSave) throws IOException {
		String formattedNfae = toFileFormat(nfae);
		Files.write(Paths.get(pathToSave), formattedNfae.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
	}
	
	public static NFAe load(File file) throws IOException {
		/*
		 * Expects a nfae specification of the following format, on a text file:
		 * 
		 * Name=({comma-separated alphabet},{comma-separated states},initial state,{comma-separated final states})
		 * Prog
		 * (state,symbol)=state		one or more lines in this format, defining the transition function (for epsilon transitions, leave symbol empty)
		 * 
		 * 
		 * Example:
		 * 
		 * AUTOMATON=({a,b},{q0,q1,q2,q3},q0,{q1,q3})
		 * Prog
		 * (q0,a)=q1
		 * (q0,b)=q2
		 * (q1,b)=q2
		 * (q2,a)=q3
		 * (q2,a)=q2
		 * (q3,a)=q3
		 * (q3,)=q2
		 * (q3,b)=q2
		 * 
		 */
		
		NFAeBuilder builder = new NFAeBuilder();
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		int initPos;
		int lastPos;
		
		String top = br.readLine();
		
		String[] header = top.split("=");
		
		String definitions = header[1].substring(header[1].indexOf('(')+1, header[1].indexOf(')'));
		
		// Load name
		builder.setName(header[0]);
		
		// Load alphabet
		initPos = definitions.indexOf('{')+1;
		lastPos = definitions.indexOf('}');
		String[] symbols = definitions.substring(initPos, lastPos).split(",");
		builder.addSymbolListToAlphabetFromString(Arrays.asList(symbols));
		
		// Load set of states 
		initPos = definitions.indexOf('{', lastPos)+1;
		lastPos = definitions.indexOf('}', initPos);
		String[] states = definitions.substring(initPos, lastPos).split(",");
		builder.addStateListFromString(Arrays.asList(states));
		
		// Load initial state
		initPos = lastPos+2;
		lastPos = definitions.indexOf(',', initPos);
		builder.setInitialStateFromString(definitions.substring(initPos, lastPos).trim());
		
		// Load final states
		initPos = definitions.indexOf('{', lastPos) + 1;
		lastPos = definitions.indexOf('}', initPos);
		String[] finalStates = definitions.substring(initPos, lastPos).split(",");
		builder.addFinalStateListFromString(Arrays.asList(finalStates));
		
		br.readLine(); // skip 'Prog' line

		// Load transition function
		String transition;
		while ((transition = br.readLine()) != null) {
			String initState = transition.substring(transition.indexOf('(')+1, transition.indexOf(',')).trim();
			String symbol = transition.substring(transition.indexOf(',')+1, transition.indexOf(')')).trim();
			String destinyState = transition.substring(transition.indexOf("=")+1).trim();
			if (symbol.equals("")) {
				builder.addEpsilonTransitionFromStrings(initState, destinyState);
			} else {
				builder.addTransitionFromStrings(initState, symbol, destinyState);
			}
		}
		
		br.close();
		
		return builder.build();
	}

	private static String toFileFormat(NFAe nfae) {
		StringBuilder sb = new StringBuilder();
		
		// Name
		sb.append(nfae.getName() + "=");
		
		// Definition
		sb.append("(");
		//   Alphabet
		sb.append("{");
		int symbolsSoFar = 0;
		int symbolsTotal = nfae.getAlphabet().size();
		for (Symbol symbol : nfae.getAlphabet()) {
			sb.append(symbol);
			symbolsSoFar++;
			if (symbolsSoFar < symbolsTotal) sb.append(","); 
		}
		sb.append("},");
		//   States
		sb.append("{");
		//     All states
		int statesSoFar = 0;
		int statesTotal = nfae.getStates().size();
		for (State state : nfae.getStates()) {
			sb.append(state);
			statesSoFar++;
			if (statesSoFar < statesTotal) sb.append(",");
		}
		//     Initial State
		sb.append("}," + nfae.getInitialState() + ",");
		//     Final States
		sb.append("{");
		int finalStatesSoFar = 0;
		int finalStatesTotal = nfae.getFinalStates().size();
		for (State finalState : nfae.getFinalStates()) {
			sb.append(finalState);
			finalStatesSoFar++;
			if (finalStatesSoFar < finalStatesTotal) sb.append(",");
		}
		sb.append("}");
		sb.append(")\n");
		
		// Transition function
		sb.append("Prog");
		sb.append("\n");
		for (Map.Entry<StateSymbolTuple, HashSet<State>> transition : nfae.getTransitionFunction().entrySet()) {
			State originState = transition.getKey().getState();
			Symbol symbol = transition.getKey().getSymbol();
			for (State destinationState : transition.getValue()) {
				sb.append("(" + originState + "," + symbol + ")=" + destinationState + "\n");
			}
		}
		// Epsilon transitions
		for (Map.Entry<State, HashSet<State>> epsilonTransition : nfae.getEpsilonTransitions().entrySet()) {
			State originState = epsilonTransition.getKey();
			for (State destinationState : epsilonTransition.getValue()) {
				sb.append("(" + originState + ",)=" + destinationState + "\n");
			}
		}
		
		return sb.toString();
	}
}
