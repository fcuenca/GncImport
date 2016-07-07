package gncimport.ui.swing;

import java.net.URL;

import gncimport.transfer.MatchingText;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class RuleDefCellRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 2707482275266150989L;

	public RuleDefCellRenderer()
	{
		setHorizontalTextPosition(SwingConstants.LEFT);
	}
	
	@Override
	public void setValue(Object value)
	{
		MatchingText renderable = (MatchingText) value;
		
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