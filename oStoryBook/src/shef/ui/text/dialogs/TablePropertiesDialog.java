/*
 * Created on Dec 21, 2005
 *
 */
package shef.ui.text.dialogs;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import shef.ui.OptionDialog;
import shef.ui.ShefUtils;
import shef.ui.text.actions.HTMLElementPropertiesAction;
import storybook.i18n.I18N;

public class TablePropertiesDialog extends OptionDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

//    private static final I18n i18n = I18n.getInstance("shef.ui.text.dialogs");
	private static Icon icon = ShefUtils.getIconX32("table"); //$NON-NLS-1$
	private static String title = I18N.getMsg("shef.table_properties"); //$NON-NLS-1$
	private static String desc = I18N.getMsg("shef.table_properties_desc"); //$NON-NLS-1$

	private TableAttributesPanel tableProps = new TableAttributesPanel();
	private RowAttributesPanel rowProps = new RowAttributesPanel();
	private CellAttributesPanel cellProps = new CellAttributesPanel();
	private HTMLElementPropertiesAction elemProp;
	public boolean doRemoveTable;

	public TablePropertiesDialog(Frame parent) {
		super(parent, title, desc, icon);
		elemProp=null;
		init();
	}

	public TablePropertiesDialog(Dialog parent, HTMLElementPropertiesAction elemProp) {
		super(parent, title, desc, icon);
		this.elemProp=elemProp;
		init();
	}

	public TablePropertiesDialog(Frame parent, HTMLElementPropertiesAction elemProp) {
		super(parent, title, desc, icon);
		this.elemProp=elemProp;
		init();
	}

	private void init() {
		Border emptyBorder = new EmptyBorder(5, 5, 5, 5);
		Border titleBorder = BorderFactory.createTitledBorder(I18N.getMsg("shef.table_properties")); //$NON-NLS-1$

		tableProps.setBorder(BorderFactory.createCompoundBorder(emptyBorder, titleBorder));
		rowProps.setBorder(emptyBorder);
		cellProps.setBorder(emptyBorder);

		JTabbedPane tabs = new JTabbedPane();
		tabs.add(tableProps, I18N.getMsg("shef.table"));         //$NON-NLS-1$
		tabs.add(rowProps, I18N.getMsg("shef.row")); //$NON-NLS-1$
		tabs.add(cellProps, I18N.getMsg("shef.cell")); //$NON-NLS-1$

		JButton suppr=new JButton(I18N.getMsg("shef.table.remove"));
		suppr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				doRemoveTable=true;
				dispose();
			}
		});
		JPanel panel=new JPanel();
		panel.add(tabs);
		panel.add(suppr);
		setContentPane(panel);
		setSize(450, 400);
		setResizable(false);
	}

	public void setTableAttributes(Map at) {
		tableProps.setAttributes(at);
	}

	public void setRowAttributes(Map at) {
		rowProps.setAttributes(at);
	}

	public void setCellAttributes(Map at) {
		cellProps.setAttributes(at);
	}

	public Map getTableAttributes() {
		return tableProps.getAttributes();
	}

	public Map getRowAttribures() {
		return rowProps.getAttributes();
	}

	public Map getCellAttributes() {
		return cellProps.getAttributes();
	}
}
