package com.variamos.gui.perspeditor.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import com.variamos.gui.maineditor.VariamosGraphEditor;
import com.variamos.gui.perspeditor.panels.PropertyParameterDialog.DialogButtonAction;
import com.variamos.hlcl.LabelingOrder;
import com.variamos.perspsupport.expressionsupport.OpersSubOperationExpType;
import com.variamos.perspsupport.instancesupport.InstAttribute;
import com.variamos.perspsupport.instancesupport.InstCell;
import com.variamos.perspsupport.instancesupport.InstElement;
import com.variamos.perspsupport.syntaxsupport.AbstractAttribute;
import com.variamos.perspsupport.syntaxsupport.SyntaxAttribute;
import com.variamos.perspsupport.types.OperationSubActionExecType;
import com.variamos.perspsupport.types.StringType;
import com.variamos.semantic.types.AttributeType;

/**
 * A class to support the property list of enumeration types on meta-modeling.
 * Initially copied from EnumerationTypeAttributeList. Part of PhD work at
 * University of Paris 1
 * 
 * @author Juan C. Mu�oz Fern�ndez <jcmunoz@gmail.com>
 * 
 * @version 1.1
 * @since 2016-03-24
 * @see com.variamos.gui.perspeditor.panels.EnumerationTypeAttributeList
 */
@SuppressWarnings("serial")
public class MetaEnumTypeAttributeList extends JList<InstAttribute> {

	/**
	 * Reference to the editor required for Dialog
	 */
	private VariamosGraphEditor editor;
	/**
	 * Reference to the InstEnumeration required to validate Id
	 */
	private InstElement element;

	private InstCell instCell;

	private String attributeName = null;

	private String enumCanonicalName = null;

	/**
			 * 
			 */
	private InstAttribute spoof = new InstAttribute("New Enum Type ...",
			new AbstractAttribute("New Enum Type ...", StringType.IDENTIFIER,
					AttributeType.SYNTAX, false, "New Enum Type ...", "", 1,
					-1, "", "", -1, "", ""), "New Enum Type ...");

	public MetaEnumTypeAttributeList(VariamosGraphEditor editor) {
		this.editor = editor;
		init(null);
	}

	@SuppressWarnings("unchecked")
	public MetaEnumTypeAttributeList(final VariamosGraphEditor editor,
			final InstCell instCell, String attributeName,
			String enumCanonicalName) {
		this.editor = editor;
		this.instCell = instCell;
		this.element = instCell.getInstElement();
		this.attributeName = attributeName;
		this.enumCanonicalName = enumCanonicalName;
		InstAttribute o = element.getInstAttributes().get(attributeName);
		if (o != null)
			init((Collection<InstAttribute>) o.getValue());
	}

	private void init(Collection<InstAttribute> varAttributes) {
		setModel(new DefaultListModel<InstAttribute>());
		final DefaultListModel<InstAttribute> model = (DefaultListModel<InstAttribute>) getModel();
		model.addElement(spoof);

		if (varAttributes != null)
			for (InstAttribute v : varAttributes)
				model.addElement(v);

		setSize(new Dimension(150, 550));
		setPreferredSize(new Dimension(100, 580));
		setMaximumSize(new Dimension(100, 580));

		addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					int index = locationToIndex(evt.getPoint());
					InstAttribute v = null;

					if (index != 0)
						v = getModel().getElementAt(index);

					editItem(v);
				}
			}
		});
		setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(
					@SuppressWarnings("rawtypes") JList list, Object value,
					int index, boolean isSelected, boolean cellHasFocus) {
				JLabel lbl = (JLabel) super.getListCellRendererComponent(list,
						value, index, isSelected, cellHasFocus);
				Object attvalue = ((InstAttribute) value).getValue();
				if (attvalue instanceof OperationSubActionExecType)
					lbl.setText(((OperationSubActionExecType) attvalue)
							.toString());
				if (attvalue instanceof LabelingOrder)
					lbl.setText(((LabelingOrder) attvalue).toString());
				return lbl;
			}
		});
	}

	protected void editItem(InstAttribute instAttribute) {
		final boolean insert = (instAttribute == null);

		final InstAttribute instName = new InstAttribute("enumName",
				new SyntaxAttribute("EnumNameValue", "Enumeration",
						AttributeType.SYNTAX, false, "Value Name",
						enumCanonicalName, "", 1, -1, "", "", -1, "", ""), "");

		if (insert) {
			// TODO move validation to a method on InstEnumeration
			@SuppressWarnings("unchecked")
			Collection<InstAttribute> instAttributes = (Collection<InstAttribute>) element
					.getInstAttributes().get(attributeName).getValue();
			int i = 1;
			/*
			 * while (notFound) { for (InstAttribute i : instAttributes) {
			 * if(i.getIdentifier().equals("enum"+i)) break; }
			 * //instAttributes.contains(new InstAttribute("enum"+i++))); ) }
			 */
			while (instAttributes.contains(new InstAttribute("enum" + i))) {
				i++;// TODO fix ids? verify they are different
			}

			// Name
			instAttribute = new InstAttribute("enum" + i,
					new AbstractAttribute("EnumValue", StringType.IDENTIFIER,
							AttributeType.SYNTAX, false, "Enumeration Value",
							"", 1, -1, "", "", -1, "", ""), "");
		} else {
			if (attributeName.equals("exptype")) {
				String split[] = ((OpersSubOperationExpType) instAttribute
						.getValue()).getExpressionType().toString().split("-");
				instName.setValue(split[0]);
			} else {
				String split[] = ((String) instAttribute.getValue()).split("-");
				instName.setValue(split[0]);
			}
		}
		final InstAttribute finalInstAttribute = instAttribute;

		// HACK for accesing a non-final variable inside of an inner class
		final InstAttribute[] buffer = { finalInstAttribute };

		// SetDomain metaDomain = new SetDomain();
		// metaDomain.setIdentifier("MetaDomain");

		// DomainRegister reg = editor.getDomainRegister();
		// for( Type d : reg.getRegisteredDomains() )
		// metaDomain.getElements().add(d.getIdentifier());
		// reg.registerDomain(metaDomain);

		// String domainRepresentation = "0, 1";
		// if(!insert)
		// = var.getDomain().getStringRepresentation();

		final PropertyParameterDialog dialog = new PropertyParameterDialog(130,
				300, editor, element, instName);
		dialog.setOnAccept(new DialogButtonAction() {
			@SuppressWarnings("unchecked")
			@Override
			public boolean onAction() {
				// This calls Pull on each parameter
				try {
					dialog.getParameters();
				} catch (NumberFormatException n) {
					JOptionPane.showMessageDialog(dialog,
							"Value is not a valid number",
							"Number Format Error", JOptionPane.ERROR_MESSAGE,
							null);
					return false;
				}
				InstAttribute v = buffer[0];
				List<InstAttribute> attributes = null;
				if (attributeName.equals("exptype")) {
					OperationSubActionExecType.valueOf((String) instName
							.getValue());
					OperationSubActionExecType ex = OperationSubActionExecType
							.valueOf((String) instName.getValue());
					v.setValue(new OpersSubOperationExpType(ex));

					attributes = ((List<InstAttribute>) element
							.getInstAttributes().get(attributeName).getValue());
				} else {
					v.setValue((String) instName.getValue());

					attributes = ((List<InstAttribute>) element
							.getInstAttributes().get(attributeName).getValue());
				}
				if (insert) {
					((DefaultListModel<InstAttribute>) getModel())
							.insertElementAt(v, getModel().getSize() - 1);
					attributes.add(v);
				}

				afterAction();
				return true;
			}
		});

		dialog.setOnDelete(new DialogButtonAction() {
			@SuppressWarnings("unchecked")
			@Override
			public boolean onAction() {
				// This calls Pull on each parameter
				dialog.getParameters();
				InstAttribute v = buffer[0];
				List<InstAttribute> attributes = ((List<InstAttribute>) element
						.getInstAttributes().get(attributeName).getValue());

				attributes.remove(v);

				afterAction();
				return true;
			}
		});

		dialog.setOnCancel(new DialogButtonAction() {

			@Override
			public boolean onAction() {
				afterAction();
				return true;
			}
		});
		dialog.center();
	}

	protected void afterAction() {

		final InstCell finalInstCell = instCell;
		new Thread() {
			public void run() {
				try {
					sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				editor.editPropertiesRefas(finalInstCell);
			}
		}.start();

		updateUI();
	}

}
