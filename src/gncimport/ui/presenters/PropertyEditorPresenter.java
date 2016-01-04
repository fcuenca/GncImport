package gncimport.ui.presenters;

import gncimport.interactors.PropertyEditInteractor;
import gncimport.ui.TxView;

import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class PropertyEditorPresenter implements PropertyEditInteractor.OutPort
{
	private TxView _view;

	public PropertyEditorPresenter(TxView view)
	{
		this._view = view;
	}

	@Override
	public void editProperties(final List<String> ignoreList)
	{
		TableModel tableModel = new AbstractTableModel() 
		{			
			private static final long serialVersionUID = 9060984673285510233L;

			@Override
			public int getColumnCount()
			{
				return 1;
			}

			@Override
			public int getRowCount()
			{
				return ignoreList.size();
			}

			@Override
			public String getColumnName(int col)
			{
				return "Description Pattern";
			}

			@Override
			public Object getValueAt(int row, int col)
			{
				return ignoreList.get(row);
			}
			
//			@Override
//			public boolean isCellEditable(int row, int col)
//			{
//				return true;
//			}
//
//			@Override
//			public void setValueAt(Object value, int row, int col)
//			{
//				ignoreList.set(row, (String) value);
//			}
		};
		
		_view.editProperties(tableModel);
	}
}
