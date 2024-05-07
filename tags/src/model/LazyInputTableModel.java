package model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import controller.LazyStructureListener;

@SuppressWarnings("serial")
public class LazyInputTableModel extends AbstractTableModel {
	private HashMap<Integer, List<String>> data = new HashMap<Integer, List<String>>();
	private int rowCount = 0;
	private JTable table;
	private ArrayList<LazyStructureListener> structureListener = new ArrayList<LazyStructureListener>();
	
	public LazyInputTableModel(JTable table) {
		this.table = table;
	}
	
	public void addStructureListener(LazyStructureListener l){
		structureListener.add(l);
		notifyStructureListeners();
		
	}
	
	@Override
	public String getColumnName(int column) {
		if (column==0) {
			return "Nr.";
		}
		return "$"+(column);
	}
	@Override
	public int getColumnCount() {
		return this.data.size()+2;
	}
	@Override
	public int getRowCount() {
		return rowCount + 1;
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (column < 1) {
			return row+1;
		}
		if (column > data.size() || row >= rowCount || data.get(column-1).size() <= row) {
			return "";
		}
		return data.get(column-1).get(row);
	}
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex!=0;
	}
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//		if (columnIndex==0) {
//			return;
//		}
		int index = columnIndex-1;
		if (index >= data.size()) {
			data.put(index, getEmptyList());
			fireTableStructureChanged();
			
			
		}
		List<String> list = data.get(index);
		list = checkListSize(list);
		
		if (rowIndex >= rowCount) {
			rowCount ++;
			list.add(aValue.toString());
			fireTableDataChanged();
		} else {
			list.set(rowIndex, aValue.toString());	
			fireTableCellUpdated(rowIndex, columnIndex);
		}
	}
	
	
	private List<String> checkListSize(List<String> list) {
		int size = list.size();
		
		if (size < rowCount) { //liste auffuellen
			for (int i = size; i < rowCount; i++) {
				list.add("");
			}
		}
		return list;
	}

	private List<String> getEmptyList() {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < rowCount; i++) {
			list.add("");
		}
		return list;
	}
	
	public void removeColumn(int col){
		if (col<0 || col >= data.size()) {
			return;
		}
		data.remove(col);
		for (int i = col; i < data.size(); i++) {
			data.put(i, data.get(i+1));
			data.remove(i+1);
		}
	}
	
	public void removeRow(int row){
		if (row < 0 || row >= rowCount) {
			return;
		}
		Set<Integer> keySet = data.keySet();
		for (Iterator<Integer> iterator = keySet.iterator(); iterator.hasNext();) {
			Integer integer = iterator.next();
			List<String> list = data.get(integer);
			list = checkListSize(list);
			list.remove(row);
			data.put(integer, list);
		}
		rowCount--;
	}
	@Override
	public void fireTableDataChanged() {
		super.fireTableDataChanged();
		adjustColWidths();
	}
	@Override
	public void fireTableStructureChanged() {
		super.fireTableStructureChanged();
		adjustColWidths();
		notifyStructureListeners();
	}
	private void notifyStructureListeners(){
		String[] cols = new String[getColumnCount()-1];
		for (int i = 0; i < cols.length; i++) {
			cols[i] = getColumnName(i);
			if (i==0) {
				cols[i] = "$0";
			}
		}
		for (LazyStructureListener l : structureListener) {
			l.columnsChanged(cols);
		}		
	}

	public void adjustColWidths() {
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setMaxWidth(50);
	}

	public void addAsColumns(String[][] strings) {
		int index = data.size()+1;
		for (int i = 0; i < strings.length; i++) {
			for (int j = 0; j < strings[i].length; j++) {
				setValueAt(strings[i][j], i, index+j);
			}
		}
//		fireTableDataChanged();
		fireTableStructureChanged();
	}
	public void addAsRows(String[][] strings) {
		int index = rowCount;
		for (int i = 0; i < strings.length; i++) {
			for (int j = 0; j < strings[i].length; j++) {
				setValueAt(strings[i][j], index+j, i+1);
			}
		}
		fireTableDataChanged();
//		fireTableStructureChanged();
	}
	
	public void addAsIs(String[][] strings) {
		int index = rowCount;
		for (int i = 0; i < strings.length; i++) {
			for (int j = 0; j < strings[i].length; j++) {
				setValueAt(strings[i][j], index+i, j+1);
			}
		}
		fireTableDataChanged();
//		fireTableStructureChanged();
	}

	public String[][] exportData() {
		int colCount =data.size()+1;
		String[][] exportData = new String[rowCount][colCount];
		
		for (int row = 0; row < rowCount; row++) {
			for (int col = 0; col < colCount; col++) {
				exportData[row][col] = getValueAt(row, col).toString();
			}
		}
		
		
		
		return exportData;
	}

	public void reset() {
		rowCount=0;
		data.clear();
		fireTableStructureChanged();
	}
	
}
