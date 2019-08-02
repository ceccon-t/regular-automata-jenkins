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
import java.util.Map;

import ufrgs.inf.formais.automata.DFA;
import ufrgs.inf.formais.builders.DFABuilder;
import ufrgs.inf.formais.helper.State;
import ufrgs.inf.formais.helper.StateSymbolTuple;
import ufrgs.inf.formais.helper.Symbol;

public class DFAStorage {
	
	public static void save(DFA dfa, String pathToSave) throws IOException {
		String formattedDfa = toFileFormat(dfa);
		Files.write(Paths.get(pathToSave), formattedDfa.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
	}
	
	public static DFA load(File file) throws IOException  {
		/*
		 * Expects a dfa specification of the following format, on a text file:
		 * 
		 * Name=({comma-separated alphabet},{comma-separated states},initial state,{comma-separated final states})
		 * Prog
		 * (state,symbol)=state		one or more lines in this format, defining the transition function
		 * 
		 * 
		 * Example:
		 * 
		 * AUTOMATON=({a,b},{q0,q1},q0,{q1})
		 * Prog
		 * (q0,a)=q1
		 * (q0,b)=q0
		 * (q1,a)=q1
		 * (q1,b)=q0
		 * 
		 */
		
		DFABuilder builder = new DFABuilder();
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String[] header = br.readLine().split("=");
		builder.setName(header[0]);
		
		String definitions = header[1].substring(header[1].indexOf('(')+1, header[1].indexOf(')'));
		
		int initPos = 1;
		int lastPos = definitions.indexOf('}');
		String[] symbols = definitions.substring(initPos, lastPos).split(",");
		builder.addSymbolListToAlphabetFromString(Arrays.asList(symbols));
		
		initPos = lastPos+3;
		lastPos = definitions.indexOf('}', initPos);
		String[] states = definitions.substring(initPos, lastPos).split(",");
		builder.addStateListFromString(Arrays.asList(states));
		
		initPos = lastPos+2;
		lastPos = definitions.indexOf(',', initPos);
		builder.setInitialStateFromString(definitions.substring(initPos, lastPos));
		
		initPos = lastPos+2;
		lastPos = definitions.indexOf('}', initPos);
		String[] finalStates = definitions.substring(initPos, lastPos).split(",");
		builder.addFinalStateListFromString(Arrays.asList(finalStates));
		
		br.readLine(); // skip 'Prog' line
		
		String transition;
		while ((transition = br.readLine()) != null) {
			String initState = transition.substring(transition.indexOf('(')+1, transition.indexOf(','));
			String symbol = transition.substring(transition.indexOf(',')+1, transition.indexOf(')'));
			String destinyState = transition.substring(transition.indexOf("=")+1).trim();
			builder.addTransitionFromStrings(initState, symbol, destinyState);
		}
		
		br.close();
		
		return builder.build();
	}
	
	private static String toFileFormat(DFA dfa) {
		StringBuilder sb = new StringBuilder();
		
		// Name
		sb.append(dfa.getName() + "=");
		
		// Definition
		sb.append("(");
		//   Alphabet
		sb.append("{");
		int symbolsSoFar = 0;
		int symbolsTotal = dfa.getAlphabet().size();
		for (Symbol symbol : dfa.getAlphabet()) {
			sb.append(symbol);
			symbolsSoFar++;
			if (symbolsSoFar < symbolsTotal) sb.append(","); 
		}
		sb.append("},");
		//   States
		sb.append("{");
		//     All states
		int statesSoFar = 0;
		int statesTotal = dfa.getStates().size();
		for (State state : dfa.getStates()) {
			sb.append(state);
			statesSoFar++;
			if (statesSoFar < statesTotal) sb.append(",");
		}
		//     Initial State
		sb.append("}," + dfa.getInitialState() + ",");
		//     Final States
		sb.append("{");
		int finalStatesSoFar = 0;
		int finalStatesTotal = dfa.getFinalStates().size();
		for (State finalState : dfa.getFinalStates()) {
			sb.append(finalState);
			finalStatesSoFar++;
			if (finalStatesSoFar < finalStatesTotal) sb.append(",");
		}
		sb.append("}");
		sb.append(")\n");
		
		// Transition function
		sb.append("Prog");
		sb.append("\n");
		for (Map.Entry<StateSymbolTuple, State> transition : dfa.getTransitionFunction().entrySet()) {
			State state = transition.getKey().getState();
			Symbol symbol = transition.getKey().getSymbol();
			sb.append("(" + state + "," + symbol + ")=" + transition.getValue() + "\n");
		}
		
		return sb.toString();
	}

}
