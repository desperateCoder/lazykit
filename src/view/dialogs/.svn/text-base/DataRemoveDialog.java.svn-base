package view.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.LazyInputTableModel;

@SuppressWarnings("serial")
public class DataRemoveDialog extends JPanel {

	private static final DataRemoveDialog INSTANCE = new DataRemoveDialog();
	
	private JTextField rowsTF = new JTextField(15);
	private JTextField colsTF = new JTextField(15);
	private static boolean error = false;
	
	
	public DataRemoveDialog() {
		super(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		final int GRID_WIDTH = 3;
		final String toolTipText = "z.B. \"5\", \"3-5\", \"3,5,7\", \"1,5-3,7-9\"";
		
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = GRID_WIDTH;
		add(new JLabel("Zeilen:"), c);
		
		c.gridy = 1; 
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		rowsTF.setToolTipText(toolTipText);
		add(rowsTF, c);

		c.weightx = 0.0;
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = GRID_WIDTH;
		add(new JLabel("Spalten:"), c);
		 
		c.gridy++;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		colsTF.setToolTipText(toolTipText);
		add(colsTF, c);
	}
	public String getRowsText(){
		return rowsTF.getText();
	}
	public String getColsText(){
		return colsTF.getText();
	}
	public void reset(){
		clearCols();
		clearRows();
	}
	public void clearRows() {
		rowsTF.setText("");
	}
	public void clearCols(){
		colsTF.setText("");
	}
	
	public static void openDialog(LazyInputTableModel model){
		int option = JOptionPane.showOptionDialog(null
				, INSTANCE, "Was?", JOptionPane.OK_CANCEL_OPTION
				, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (option==JOptionPane.OK_OPTION) {
			removeRows(INSTANCE.getRowsText(), model);
			removeCols(INSTANCE.getColsText(), model);
		}
		if (error) {
			error = !error;
			openDialog(model);
		}
	}
	
	
	private static void removeCols(String colsText, LazyInputTableModel model) {
		int[] numbers;
		try {
			numbers = parseNumberString(colsText);
		} catch (IllegalArgumentException e) {
			error = true;
			JOptionPane.showMessageDialog(null, e.getMessage()
					, "Fehler", JOptionPane.ERROR_MESSAGE);
			return;
		}
		INSTANCE.clearCols();
		for (int i : numbers) {
			model.removeColumn(i-1);
		}
		model.fireTableStructureChanged();
	}
	
	private static void removeRows(String rowsText, LazyInputTableModel model) throws IllegalArgumentException{
		int[] numbers;
		try {
			numbers = parseNumberString(rowsText);
		} catch (IllegalArgumentException e) {
			error = true;
			JOptionPane.showMessageDialog(null, e.getMessage()
					, "Fehler", JOptionPane.ERROR_MESSAGE);
			return;
		}
		INSTANCE.clearRows();
		for (int i : numbers) {
			model.removeRow(i-1);
		}
		model.fireTableDataChanged();
	}
	
	private static int[] parseNumberString(String str){
		if (str.length()<1) {
			return new int[0];
		}
		String numberString = trimString(str);
		if (!numberString.matches("[0-9, -]*") 
				|| numberString.matches("(--)")
				|| numberString.matches("(,-)")
				|| numberString.matches("(-,)")) {
			throw new IllegalArgumentException("Die übergebene Zeichenkette \""+
					numberString + "\" ist kein gültige Spanne.");
		}
		
		ArrayList<Integer> numbers = new ArrayList<>();
		
		String[] split = numberString.split("(,)");
		for (int i = 0; i < split.length; i++) {
			String s = split[i];
			if (s.length()<1) {
				continue;
			}
			String[] minusSplit = s.split("(-)");
			if (minusSplit.length < 2) {
				numbers.add(Integer.parseInt(minusSplit[0]));
			} else {
				int bigger= Integer.parseInt(minusSplit[0]);
				int smaller = Integer.parseInt(minusSplit[1]);
				if (bigger < smaller) {
					int tmp = bigger;
					bigger = smaller;
					smaller = tmp;
				}
				for (int j = smaller; j <= bigger; j++) {
					numbers.add(j);
				}
			}
		}
		
		Collections.sort(numbers);
		
		int size = numbers.size();
		int[] numArray = new int[size];
		int temp = -1;
		for (int i = 0; i < size; i++) {
			int intVal = numbers.get(i).intValue();
			if (intVal < 1) {
				continue;
			}
			if (temp == intVal) {
				throw new IllegalArgumentException("Die Zahl \""+
						intVal + "\" ist im Bereich\n\""+
						numberString + "\" doppelt vorhanden.");
			}
			numArray[size-i-1] = intVal;
			temp = intVal;
		}
		
		return numArray;
	}
	
	private static String trimString(String in){
		String ret = in.trim();
		ret = ret.replaceAll(" ", "");
		return ret;
	}
//	public static void main(String[] args) {
//		System.out.println(trimString("10-5, 2, 15, 3"));
//	}
}
