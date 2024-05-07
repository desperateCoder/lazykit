package view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

@SuppressWarnings("serial")
public class DiColorListCellRenderer extends DefaultListCellRenderer{
	
	Color colorEven;
	Color colorOdd;
	
	public DiColorListCellRenderer(Color colorEven, Color colorOdd) {
		this.colorEven = colorEven;
		this.colorOdd = colorOdd;
	}
	
    public Component getListCellRendererComponent( JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {  
        Component c = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );  
        if (isSelected) {
			return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		}
        if ( index % 2 == 0 ) {  
            c.setBackground( colorEven );  
        }  
        else {  
            c.setBackground( colorOdd );  
        }  
        return c;  
	}
}
