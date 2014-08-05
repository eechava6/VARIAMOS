package com.variamos.gui.pl.configurator.guiactions;

import java.awt.event.ActionEvent;

import com.mxgraph.util.mxResources;
import com.variamos.gui.maineditor.AbstractEditorAction;
import com.variamos.gui.pl.editor.ProductLineGraphEditor;

@SuppressWarnings("serial")
public class ConfigureAction extends AbstractEditorAction {

	public ConfigureAction() {
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		ProductLineGraphEditor editor = getEditor(evt);
		editor.bringUpExtension(mxResources.get("configurationTab"));
		editor.getConfigurator().configure(editor.getEditedProductLine());
		
		//editor.getConfigurator().performConfiguration();
	}
	
}
