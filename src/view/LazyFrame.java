package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import controller.LazyPatternInterpreter;
import controller.LazyStructureListener;

import model.LazyInputTableModel;
import view.dialogs.DataRemoveDialog;
import view.dialogs.LazyInputDialog;

@SuppressWarnings("serial")
public class LazyFrame extends JFrame implements ActionListener,
		LazyStructureListener, ItemListener {

	public static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 14);
	private static LazyFrame INSTANCE;
	private JTextArea outputField = new JTextArea();
	private JTextArea patternField = new JTextArea();
	private JScrollPane outputScroll = null;
	private LazyInputTable inputTable = new LazyInputTable();
	private LazyInputDialog inputFrame = null;
	private LazyPatternInterpreter interpreter = new LazyPatternInterpreter();
	private JComboBox<String> fieldsCombo = new JComboBox<>();

	public LazyFrame() {

		JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JSplitPane leftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		// ############### Linke Seite
		// ######## Oben
		JPanel upper = new JPanel();
		upper.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		final int GRID_WIDTH = 6;
		// final int ICON_SIZE = 16;
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = GRID_WIDTH;
		JLabel label = new JLabel("Eingabe:");
		label.setFont(HEADER_FONT);
		upper.add(label, c);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 0.8;
		c.gridy = 1;
		inputTable.addStructureListener(this);
		upper.add(new JScrollPane(inputTable), c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridheight = 1;
		c.gridwidth = GRID_WIDTH / 2;
		c.gridy++;
		JButton removeDataBtn = new JButton("Daten entfernen...");
		// removeDataBtn.setIcon(resizeImage(getIcon("./icons/remove.png"),
		// ICON_SIZE, ICON_SIZE));
		removeDataBtn.addActionListener(this);
		removeDataBtn.setActionCommand(ActionCommands.MAIN_REMOVE_DATA_BTN);
		upper.add(removeDataBtn, c);

		c.gridx = 3;
		JButton addDataBtn = new JButton("Daten hinzufügen...");
		// addDataBtn.setIcon(resizeImage(getIcon("./icons/add.png"), ICON_SIZE,
		// ICON_SIZE));
		addDataBtn.addActionListener(this);
		addDataBtn.setActionCommand(ActionCommands.MAIN_ADD_DATA_BTN);
		upper.add(addDataBtn, c);

		// ######## Unten
		JPanel lower = new JPanel();
		lower.setLayout(new GridBagLayout());
		c = new GridBagConstraints();

		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = GRID_WIDTH;
		label = new JLabel("Muster:");
		label.setFont(HEADER_FONT);
		lower.add(label, c);

		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 0.2;
		lower.add(new JScrollPane(patternField), c);

		c.fill = GridBagConstraints.NONE;
		c.weighty = 0.0;
		c.gridy++;
		c.gridx = 0;
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 1;
		lower.add(new JLabel("Feld hinzufügen:"), c);

		// c.gridx = c.gridwidth;
		// c.gridwidth = GRID_WIDTH - c.gridwidth;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridwidth = GRID_WIDTH - 1;
		fieldsCombo.addItemListener(this);
		lower.add(fieldsCombo, c);

		leftSplit.setResizeWeight(0.8);
		leftSplit.setLeftComponent(upper);
		leftSplit.setRightComponent(lower);

		// ############### Rechte Seite
		JPanel right = new JPanel();
		right.setLayout(new GridBagLayout());
		c = new GridBagConstraints();

		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = GRID_WIDTH;
		label = new JLabel("Ausgabe:");
		label.setFont(HEADER_FONT);
		right.add(label, c);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridy = 1;
		outputScroll = new JScrollPane(outputField);
		right.add(outputScroll, c);

		mainSplit.setRightComponent(right);
		mainSplit.setLeftComponent(leftSplit);
		mainSplit.setResizeWeight(0.5);
		mainSplit.setBorder(BorderFactory.createRaisedSoftBevelBorder());

		JPanel mainPane = new JPanel(new GridBagLayout());
		c = new GridBagConstraints();

		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		mainPane.add(mainSplit, c);

		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridy = 1;
		JButton goBtn = new JButton("Los!");
		goBtn.addActionListener(this);
		goBtn.setActionCommand(ActionCommands.MAIN_GO_BTN);
		mainPane.add(goBtn, c);

		addMenu();

		setContentPane(mainPane);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("LazyKit Copy & Waste");
		// icon
		setIconImage(getImage("./icons/icon_large.png"));
		pack();
		setSize(getWidth() + 100, getHeight());
		centerFrame(this);
		setVisible(true);
		mainSplit.setDividerLocation(0.5);
		leftSplit.setDividerLocation(0.7);
	}

	private void addMenu() {
		JMenuBar bar = new JMenuBar();
		JMenu fileMenu = new JMenu("Datei");
		JMenuItem resetItem = new JMenuItem("Tabelle zurücksetzen");
		resetItem.addActionListener(this);
		resetItem.setActionCommand(ActionCommands.MAIN_RESET);
		resetItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,
				ActionEvent.CTRL_MASK));
		fileMenu.add(resetItem);

		JMenu toolsMenu = new JMenu("Werkzeuge");
		JMenuItem searchItem = new JMenuItem("Suchen...");
		searchItem.addActionListener(this);
		searchItem.setActionCommand(ActionCommands.MAIN_SEARCH);
		searchItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				ActionEvent.CTRL_MASK));
		toolsMenu.add(searchItem);

		bar.add(fileMenu);
		bar.add(toolsMenu);

		setJMenuBar(bar);
	}

	public static void setNimbusLAF() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					return;
				}
			}
			System.out.println("Nimbus L&F nicht gefunden.");
		} catch (Exception e) {
			System.out.println("Fehler beim setzen des L&F:");
			e.printStackTrace();
		}
	}

	public void addAsColumns(String[][] data) {
			inputTable.addAsColumns(data);	
	}

	public void addAsRows(String[][] data) {
		inputTable.addAsRows(data);
	}
	
	public void addAsIs(String[][] data) {
		inputTable.addAsIs(data);
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		int index = fieldsCombo.getSelectedIndex();
		if (event.getStateChange() == ItemEvent.SELECTED && index != 0) {
			String txt = "";
			if (index == 1) {// JS
				txt = "{JS  JS}";
			} else {
				String item = event.getItem().toString();
				txt = "{" + item + "}";
			}
			insertAtCaretPosition(patternField, txt);
			fieldsCombo.setSelectedIndex(0);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (ActionCommands.MAIN_ADD_DATA_BTN.equals(e.getActionCommand())) {
			if (inputFrame == null) {
				inputFrame = new LazyInputDialog();
				centerFrame(inputFrame);
			}
			if (inputFrame.isVisible()) {
				inputFrame.setVisible(false);
			}
			inputFrame.setVisible(true);
		} else if (ActionCommands.MAIN_REMOVE_DATA_BTN.equals(e
				.getActionCommand())) {
			DataRemoveDialog.openDialog((LazyInputTableModel) inputTable
					.getModel());
		} else if (ActionCommands.MAIN_GO_BTN.equals(e.getActionCommand())) {
			String out = interpreter.fillPattern(patternField.getText(),
					inputTable.exportData());
			outputField.setText(out);
		} else if (ActionCommands.MAIN_RESET.equals(e.getActionCommand())) {
			int ret = JOptionPane.showConfirmDialog(this,
					"Soll die Tabelle wirklich geleert werden?", "Sicher?",
					JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.YES_OPTION) {
				inputTable.reset();
				outputField.setText("");
			}
		} else if (ActionCommands.MAIN_SEARCH.equals(e.getActionCommand())) {
			JOptionPane.showMessageDialog(this, "Noch nicht implementiert :-P");
		}
	}

	public static void centerFrame(Window frame) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height
				/ 2 - frame.getSize().height / 2);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		setNimbusLAF();
		UIManager.put("Table.showGrid", true);
		INSTANCE = new LazyFrame();
	}

	public static LazyFrame getInstance() {
		return INSTANCE;
	}

	private BufferedImage getImage(String relativePath) {
		InputStream stream = getClass().getResourceAsStream(relativePath);
		try {
			return ImageIO.read(stream);
		} catch (IOException e) {
			System.out
					.println("Datei \"" + relativePath + "\" nicht gefunden.");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Fehler beim lesen der Datei.");
		}
		return null;
	}

	@Override
	public void columnsChanged(String[] cols) {
		DefaultComboBoxModel<String> model = ((DefaultComboBoxModel<String>) fieldsCombo
				.getModel());
		model.removeAllElements();
		model.addElement("-- Bitte Wählen --");
		model.addElement("JS");
		for (String s : cols) {
			model.addElement(s);
		}
	}

	// private ImageIcon getIcon(String relativePath){
	// URL url = getClass().getResource(relativePath);
	// return new ImageIcon(url);
	// }
	//
	// private static ImageIcon resizeImage(ImageIcon icon, int width, int
	// height){
	// Image img = icon.getImage();
	// BufferedImage bi = new BufferedImage(img.getWidth(null),
	// img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	// Graphics g = bi.createGraphics();
	// g.drawImage(img, 0, 0, width, height, null);
	// return new ImageIcon(bi);
	//
	// }
	//
	private void insertAtCaretPosition(JTextComponent field, String text) {
		try {
			field.getDocument().insertString(field.getCaretPosition(), text,
					null);
		} catch (BadLocationException e) {
			System.out.println("Text konnte nicht eingefuegt werden!");
			e.printStackTrace();
		}
	}

}
