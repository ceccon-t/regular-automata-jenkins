package ufrgs.inf.formais;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

import ufrgs.inf.formais.automata.Automaton;
import ufrgs.inf.formais.automata.DFA;
import ufrgs.inf.formais.automata.NFA;
import ufrgs.inf.formais.automata.NFAe;
import ufrgs.inf.formais.builders.AutomataConverter;
import ufrgs.inf.formais.helper.State;
import ufrgs.inf.formais.helper.StateSymbolTuple;
import ufrgs.inf.formais.helper.Symbol;
import ufrgs.inf.formais.helper.Word;
import ufrgs.inf.formais.storage.AutomataStorage;

public class App  {
	
	private static Automaton automaton;
	
    public static void main( String[] args ) {
    	
    	automaton = dfaEndsWithA();
    	
    	JFileChooser fileChooser = new JFileChooser();
    	
    	// Main panel
    	JFrame mainFrame = new JFrame();
    	JPanel mainPanel = new JPanel();
    	JTable wordsTable = createTable();
    	
    	// Top main panel
    	JPanel topMainPanel = new JPanel();
    	JTextField userInputField = new JTextField(20);
    	userInputField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addUserInputToTable(userInputField, wordsTable);
			}
		});
    	
    	JButton addWordBtn = new JButton("Add");
    	addWordBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addUserInputToTable(userInputField, wordsTable);
			}
    	});
    	
    	JButton loadFileOfWordsBtn = new JButton("Load words");
    	loadFileOfWordsBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					loadListOfWordsIntoTable(wordsTable, getListOfWordsFromFile(selectedFile));
				}
			}
    	});
    	
    	JButton clearWordsBtn = new JButton("Clear words");
    	clearWordsBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DefaultTableModel tableModel = (DefaultTableModel) wordsTable.getModel();
				tableModel.getDataVector().removeAllElements();
				tableModel.fireTableDataChanged();
			}
    	});
    	
    	JButton decideBtn = new JButton("Decide");
    	decideBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (automaton != null) {
					DefaultTableModel tableModel = (DefaultTableModel) wordsTable.getModel();
					for (int i = 0; i < tableModel.getRowCount(); i++) {
						String word = (String) tableModel.getValueAt(i, 0);
						String result = automaton.recognize(new Word(word, "")) ? "ACCEPTED" : "REJECTED";
						tableModel.setValueAt(result, i, 1);
					}
				}
			}
    	});
    	
    	topMainPanel.add(userInputField);
    	topMainPanel.add(addWordBtn);
    	topMainPanel.add(loadFileOfWordsBtn);
    	topMainPanel.add(clearWordsBtn);
    	topMainPanel.add(decideBtn);
    	
    	mainPanel.setLayout(new BorderLayout());
    	mainPanel.add(BorderLayout.NORTH, topMainPanel);
    	mainPanel.add(BorderLayout.CENTER, new JScrollPane(wordsTable));
    	
    	// Bottom panel
    	JPanel automatonPanel = new JPanel();
    	
    	JPanel automatonOperationsPanel = new JPanel();
    	JPanel automatonInfoPanel = new JPanel();
    	
    	JLabel automatonNameLabel = new JLabel();
    	automatonNameLabel.setText(automaton.getName());
    	JLabel automatonShortTypeLabel = new JLabel();
    	automatonShortTypeLabel.setText("Type: " + automaton.getShortType());
    	JLabel automatonNumberOfStatesLabel = new JLabel();
    	automatonNumberOfStatesLabel.setText("Number of states: " + automaton.getStates().size());
    	
    	JButton fileChooserBtn = new JButton("Load automaton");
    	fileChooserBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					try {
						automaton = AutomataStorage.load(selectedFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					updateAutomatonDisplay(automaton, automatonNameLabel, automatonShortTypeLabel, automatonNumberOfStatesLabel);
					cleanTableResults(wordsTable);
				}
			}
    	});
    	
    	JButton convertToDfaBtn = new JButton("Convert to DFA");
    	convertToDfaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (automaton instanceof NFAe) {
					automaton = AutomataConverter.nfaeToDfa((NFAe) automaton);
				} else if(automaton instanceof NFA) {
					automaton = AutomataConverter.nfaToDfa((NFA) automaton);
				}
				updateAutomatonDisplay(automaton, automatonNameLabel, automatonShortTypeLabel, automatonNumberOfStatesLabel);
			}
    	});
    	
    	JButton convertToNfaBtn = new JButton("Convert to NFA");
    	convertToNfaBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (automaton instanceof DFA) {
					automaton = AutomataConverter.dfaToNfa((DFA) automaton);
				} else if (automaton instanceof NFAe) {
					automaton = AutomataConverter.nfaeToNfa((NFAe) automaton);
				}
				updateAutomatonDisplay(automaton, automatonNameLabel, automatonShortTypeLabel, automatonNumberOfStatesLabel);
			}
    	});
    	
    	JButton convertToNfaeBtn = new JButton("Convert to NFAe");
    	convertToNfaeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (automaton instanceof DFA) {
					automaton = AutomataConverter.dfaToNfae((DFA) automaton);
				} else if (automaton instanceof NFA && !(automaton instanceof NFAe)) {
					automaton = AutomataConverter.nfaToNfae((NFA) automaton);
				}
				updateAutomatonDisplay(automaton, automatonNameLabel, automatonShortTypeLabel, automatonNumberOfStatesLabel);
			}
    	});
    	
    	automatonOperationsPanel.add(fileChooserBtn);
    	automatonOperationsPanel.add(convertToDfaBtn);
    	automatonOperationsPanel.add(convertToNfaBtn);
    	automatonOperationsPanel.add(convertToNfaeBtn);
    	
    	automatonInfoPanel.setLayout(new GridBagLayout());
    	automatonInfoPanel.add(automatonNameLabel);
    	GridBagConstraints infoPanelConstraints = new GridBagConstraints();
    	infoPanelConstraints.gridy = 1;
    	automatonInfoPanel.add(automatonShortTypeLabel, infoPanelConstraints);
    	infoPanelConstraints.gridy = 2;
    	automatonInfoPanel.add(automatonNumberOfStatesLabel, infoPanelConstraints);
    	
    	automatonPanel.setLayout(new BorderLayout());
    	automatonPanel.add(BorderLayout.NORTH, automatonOperationsPanel);
    	automatonPanel.add(BorderLayout.SOUTH, automatonInfoPanel);
    	
    	mainFrame.getContentPane().add(BorderLayout.CENTER, mainPanel);
    	mainFrame.getContentPane().add(BorderLayout.SOUTH, automatonPanel);
    	
    	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	mainFrame.setMinimumSize(new Dimension(800, 600));
    	mainFrame.setVisible(true);
    	mainFrame.setTitle("Regular Automata");
    	
    }
    
    private static String getAutomatonNameDisplay(String name) {
    	return (name.length() <= 100 ) ? name : name.substring(0, 96) + "..." ;
    }
    
    private static void updateAutomatonDisplay(Automaton automaton, JLabel automatonNameLabel, JLabel automatonShortTypeLabel, JLabel automatonNumberOfStatesLabel) {
		String automatonName = automaton.getName();
		automatonNameLabel.setText(getAutomatonNameDisplay(automatonName));
		automatonNameLabel.setToolTipText(automatonName);
		automatonShortTypeLabel.setText("Type: " + automaton.getShortType());
		automatonNumberOfStatesLabel.setText("Number of states: " + automaton.getStates().size());
    }
    
    private static List<String> getListOfWordsFromFile(File file) {
    	ArrayList<String> listOfWords = new ArrayList<String>();
    	Scanner scanner;
		try {
			scanner = new Scanner(file);
	    	while (scanner.hasNextLine()) {
	    		listOfWords.add(scanner.nextLine());
	    	}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	return listOfWords;
    }
    
    private static void loadListOfWordsIntoTable(JTable table, List<String> words) {
    	DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
    	for (String word : words) {
    		tableModel.addRow(new Object[] {word, ""});
    	}
    }
    
    private static JTable createTable() {
    	DefaultTableModel tableModel = new DefaultTableModel(new Object[] {"Word", "Result"}, 0);
    	JTable table = new JTable(tableModel);
    	
    	InputMap inputMap = table.getInputMap();
    	ActionMap actionMap = table.getActionMap();
    	
    	inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "Delete");
    	inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "Delete");
    	
    	actionMap.put("Delete", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tableModel.removeRow(table.getSelectedRow());
			}
    	});
    	
    	return table;
    }
    
    private static void cleanTableResults(JTable table) {
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			tableModel.setValueAt("", i, 1);
		}
    }
    
    private static void addUserInputToTable(JTextField inputField, JTable table) {
		String userInput = inputField.getText();
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.addRow(new Object[] {userInput, ""});
    }
    
	public static DFA dfaEndsWithA() {
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
