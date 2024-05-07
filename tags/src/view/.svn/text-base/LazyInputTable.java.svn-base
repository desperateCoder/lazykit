package view;

import javax.swing.JTable;

import model.LazyInputTableModel;
import controller.LazyStructureListener;

@SuppressWarnings("serial")
public class LazyInputTable extends JTable {
	
	LazyInputTableModel model = new LazyInputTableModel(this);
	
	public LazyInputTable() {
		super();
		setFillsViewportHeight(true);
		setModel(model);
		getTableHeader().setReorderingAllowed(false);
		model.adjustColWidths();
		
	}
	
	public void addAsColumns(String[][] data) {
		model.addAsColumns(data);
	}
	public void addAsRows(String[][] data) {
		model.addAsRows(data);
	}
	public void addAsIs(String[][] data) {
		model.addAsIs(data);
	}

	public String[][] exportData() {
		return model.exportData();
	}
	
	public void reset() {
		model.reset();
	}
	public void addStructureListener(LazyStructureListener l){
		model.addStructureListener(l);
	}
}
