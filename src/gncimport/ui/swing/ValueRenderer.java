package gncimport.ui.swing;

import gncimport.transfer.WholeValue;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class ValueRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 2707482275266150989L;

	public ValueRenderer()
	{
		setHorizontalTextPosition(SwingConstants.LEFT);
	}
	
	@Override
	public void setValue(Object value)
	{
		WholeValue renderable = (WholeValue) value;
		
		setText(renderable.displayText());
		
		if(!renderable.isValid())
		{
			setToolTipText(renderable.hint());
			
			URL iconUrl = getClass().getResource("warning.png");
			setIcon(new ImageIcon(iconUrl, "Invalid rule definition"));
		}
		else
		{
			setToolTipText(null);
			setIcon(null);
		}
	}
}