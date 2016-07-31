package gncimport.ui.swing;

import gncimport.transfer.MatchingRule;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class RuleTable extends JTable
{	
	final Color STANDARD_BACKGROUND_COLOR = Color.LIGHT_GRAY; 
	final Color ALTERNATE_BACKGROUND_COLOR = new Color(209, 229, 255);
	final Color SELECTION_BACKGROUND_COLOR = new Color(52, 117, 237);

	public RuleTable(RuleTableModel model) 
	{
		super(model);
		
		setDefaultRenderer(MatchingRule.class, new RuleDefCellRenderer());
		setDefaultEditor(MatchingRule.class, new RuleDefCellEditor());
		
		setName("IGNORE_RULES");
		setModel(model);
	}
	
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
	{
		Component cell = super.prepareRenderer(renderer, row, column);

		if (isCellSelected(row, column)) cell.setBackground(SELECTION_BACKGROUND_COLOR);
		else if (alternatingRow(row)) cell.setBackground(ALTERNATE_BACKGROUND_COLOR);
		else cell.setBackground(STANDARD_BACKGROUND_COLOR);

		return cell;
	}
	
	private boolean alternatingRow(int row)
	{
		return row % 2 == 1;
	}
}