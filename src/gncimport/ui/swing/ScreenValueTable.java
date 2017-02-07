package gncimport.ui.swing;

import gncimport.transfer.ScreenValue;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class ScreenValueTable extends JTable
{

	final Color STANDARD_BACKGROUND_COLOR = Color.LIGHT_GRAY;
	final Color ALTERNATE_BACKGROUND_COLOR = new Color(209, 229, 255);
	final Color SELECTION_BACKGROUND_COLOR = new Color(52, 117, 237);

	public ScreenValueTable(TableModel model, String name) 
	{
		setName(name);
		setModel(model);
		
		setDefaultRenderer(ScreenValue.class, new ValueRenderer());
		setDefaultEditor(ScreenValue.class, new ValueEditor());
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