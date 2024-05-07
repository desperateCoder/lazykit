package view.dialogs;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

import strings.PatternRegex;
import view.ActionCommands;
import view.LazyFrame;

@SuppressWarnings("serial")
public class LazyInputDialog extends JDialog implements ActionListener{

//	private JList<String> previewList = null;
	private JTable previewTable = null;
//	private DefaultListModel<String> previewListModel;
	private JTextArea inputArea;
	private JTextField splitField;
	private JTextField patternField;
	private JCheckBox trimCB;
	private JCheckBox ignoreNlCB;
	private JComboBox<String> addAsCombo;
	
	public LazyInputDialog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModalityType(ModalityType.MODELESS);
//		setType(JFrame.Type.UTILITY);
		JPanel contentPanel = new JPanel(new BorderLayout());
		
		JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		final double DIV_POS = 0.25;
		mainPanel.setResizeWeight(DIV_POS);
		
		JPanel previewPane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel previewLabel = new JLabel("Vorschau: ");
		previewLabel.setFont(LazyFrame.HEADER_FONT);
		previewPane.add(previewLabel, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1D;
		c.weighty = 1D;
		previewTable = new JTable(new DefaultTableModel());
		previewTable.setFillsViewportHeight(true);
		
		previewPane.add(new JScrollPane(previewTable), c);
//		previewListModel = new DefaultListModel<>();
//		previewList = new JList<String>(previewListModel);
//		previewList.setCellRenderer(new DiColorListCellRenderer(new Color(151, 202 ,255), Color.WHITE));
//		previewPane.add(new JScrollPane(previewList), c);
		
		
		mainPanel.setLeftComponent(previewPane);
		
		JPanel inputPane = new JPanel(new GridBagLayout());
		c = new GridBagConstraints();
		final int INPUT_GRID_WIDTH = 5;
		
		JLabel inputLabel = new JLabel("Dateneingabe: ");
		inputLabel.setFont(LazyFrame.HEADER_FONT);
		inputPane.add(inputLabel, c);
		
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1D;
		c.weighty = 1D;
		c.gridwidth = INPUT_GRID_WIDTH;
		c.gridheight = 5;
		inputArea = new JTextArea();
		inputPane.add(new JScrollPane(inputArea),c);
		
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 0D;
		c.weighty = 0D;
		c.gridy += c.gridheight;
		c.gridwidth = 1;
		c.gridheight = 1;
		inputPane.add(new JLabel("Einfügen als:"), c);
		
		c.gridx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		String[] entrys = new String[]{"Spalte","Zeile","Anhang"};
		addAsCombo = new JComboBox<String>(entrys);
		inputPane.add(addAsCombo, c);
		
//		trimCB = new JCheckBox();
//		inputPane.add(trimCB, c);
		
		
		c.gridx++;
		c.fill = GridBagConstraints.NONE;
		inputPane.add(new JLabel("Zeilenumbrüche ignorieren:"), c);
		
		c.gridx++;
		c.fill = GridBagConstraints.NONE;
		ignoreNlCB = new JCheckBox();
		inputPane.add(ignoreNlCB, c);
		
		c.gridx++;
		c.gridheight = 2;
		JButton previewBtn = new JButton("Vorschau");
		c.fill = GridBagConstraints.BOTH;
		previewBtn.setActionCommand(ActionCommands.INPUT_SHOW_PREVIEW);
		previewBtn.addActionListener(this);
		inputPane.add(previewBtn, c);
		
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy++; 
		c.gridheight = 1;
		inputPane.add(new JLabel("Satz-Trennung:"), c);
		
		c.gridx++;
//		c.gridwidth = 3;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL; 
		splitField = new JTextField(10);
		splitField.setText("\\n");
		inputPane.add(splitField, c);
		
		c.gridx++;
		c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE; 
		inputPane.add(new JLabel("Sätze trimmen:"), c);
		
		c.gridx++;
		trimCB = new JCheckBox();
		inputPane.add(trimCB, c);
		
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy++; 
		c.gridheight = 1;
		inputPane.add(new JLabel("Satz-RegEx:"), c);
		
		c.gridx++;
		c.gridwidth = 3;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL; 
		patternField = new JTextField(10);
		patternField.setText("{{}}");
		inputPane.add(patternField, c);
		
		mainPanel.setRightComponent(inputPane);
		mainPanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		
		contentPanel.add(mainPanel, BorderLayout.CENTER);
		
		JButton addButton = new JButton("Sätze hinzufügen!");
		addButton.addActionListener(this);
		addButton.setActionCommand(ActionCommands.INPUT_APPLY);
		JPanel btnPanel = new JPanel();
		btnPanel.add(addButton);
		contentPanel.add(btnPanel, BorderLayout.SOUTH);
		
		
		setContentPane(contentPanel);
		setTitle("Daten hinzufügen");
		addMenu();
		pack();
//		setSize(500, 500);
		setSize(getWidth(), 500);
		mainPanel.setDividerLocation(DIV_POS);
		resetForm();
	}
	
	private void addMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu tools = new JMenu("Werkzeuge");
		JMenuItem searchItem = new JMenuItem("Suchen und ersetzen");
		searchItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		searchItem.addActionListener(this);
		searchItem.setActionCommand(ActionCommands.INPUT_MENU_SEARCH);
		tools.add(searchItem);
		menuBar.add(tools);
		setJMenuBar(menuBar);
	}

	private void resetForm() {
		trimCB.setSelected(true);
//		splitField.setText("\\n");
//		addAsCombo.setSelectedIndex(0);
//		previewListModel.clear();
		DefaultTableModel model = ((DefaultTableModel)(previewTable.getModel()));
		model.setRowCount(0);
		model.setColumnCount(0);
		inputArea.setText("");
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();
		if (ActionCommands.INPUT_SHOW_PREVIEW.equals(c)) {
			String[][] data = getSplittedData();
			DefaultTableModel model = ((DefaultTableModel)previewTable.getModel());
			if (data.length > 0) {
				model.setRowCount(0);
				model.setColumnCount(0);
//				previewListModel.clear();
			}
			for (String[] s : data) {
				if (s.length>previewTable.getColumnCount()) {
					for (int i = previewTable.getColumnCount(); i < s.length; i++) {
						model.addColumn((i+1)+"");
					}
				}
				model.addRow(s);
//				previewListModel.addElement(s.length()<1?" ":s);					
			}
			model.fireTableStructureChanged();
			previewTable.validate();
			previewTable.repaint();
		} else if (ActionCommands.INPUT_APPLY.equals(c)) {
			String[][] data = getSplittedData();
			if (((String)addAsCombo.getSelectedItem()).equals("Zeile")) {
				LazyFrame.getInstance().addAsRows(data);
			} else if (((String)addAsCombo.getSelectedItem()).equals("Spalte")) {
				LazyFrame.getInstance().addAsColumns(data);
			} else {
				LazyFrame.getInstance().addAsIs(data);
			}
//			setVisible(false);
			dispose();
			resetForm();
		} else if (ActionCommands.INPUT_MENU_SEARCH.equals(c)) {
			JOptionPane.showMessageDialog(this, "Noch nicht implementiert :-P");
		}
	}
	
	private String[][] getSplittedData(){
		String text = inputArea.getText();
		
		if (ignoreNlCB.isSelected()) {
			text = text.replaceAll("\n", "");
		}
		String regEx = splitField.getText().replace("\\n", "(\\r?\\n)");
		String[] splittedData = text.split(regEx);
		
		for (int i = 0; i < splittedData.length; i++) {
			splittedData[i] = splittedData[i].replace("\n", "\\n");
		}
		
		String [][] ret = new String[splittedData.length][];
		String pattern = patternField.getText();
		for (int i = 0; i < ret.length; i++) {
			ret[i] = getSplittedRow(splittedData[i], pattern);
		}
		return ret;
	}
	
	private String[] getSplittedRow(String input, String pattern) {
		String[] strs = PatternRegex.splitStrings(input, pattern);
		if (trimCB.isSelected()) {
			for (int i = 0; i < strs.length; i++) {
				strs[i] = strs[i].trim();
			}
		}
		return strs;
	}
	
	

	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		LazyFrame.setNimbusLAF();
//		LazyInputDialog frame = new LazyInputDialog();
//		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		LazyFrame.centerFrame(frame);
//		frame.setVisible(true);
//	}

}
