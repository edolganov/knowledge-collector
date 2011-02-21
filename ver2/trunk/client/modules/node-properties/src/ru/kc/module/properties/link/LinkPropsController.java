package ru.kc.module.properties.link;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ru.kc.common.node.edit.NodeEditions;
import ru.kc.common.node.edit.event.UrlChanged;
import ru.kc.common.node.edit.event.UrlReverted;
import ru.kc.model.Link;
import ru.kc.module.properties.node.AbstractNodePropsController;
import ru.kc.module.properties.ui.LinkProps;
import ru.kc.platform.annotations.Mapping;
import ru.kc.tools.filepersist.update.UpdateRequest;
import ru.kc.tools.filepersist.update.UpdateUrl;
import ru.kc.util.Check;
import ru.kc.util.swing.os.OSUtil;

@Mapping(LinkProps.class)
public class LinkPropsController extends AbstractNodePropsController<Link, LinkProps> {
	
	@Override
	protected void init() {
		init(ui.name, ui.description, ui.save, ui.revert);
		
		ui.url.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				urlChanged();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				urlChanged();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				urlChanged();
			}
		});
		
		ui.open.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String text = ui.url.getText();
					if(!Check.isEmpty(text)){
						OSUtil.openUrl(text);
					}
				}catch (Exception ex) {
					log.error("can't open url", ex);
				}
			}
		});
	}

	protected void urlChanged() {
		if(!enabledUpdateMode) return;
		
		setButtonsEnabled(true);
		String newUrl = ui.url.getText();
		fireEventInEDT(new UrlChanged(node, newUrl));
	}
	
	@Override
	protected void extendedFillData(NodeEditions editions) {
		String url = editions.get(UpdateUrl.class);
		
		ui.url.setText(url == null? node.getUrl() : url);
		ui.url.setCaretPosition(0);
		setButtonsEnabled(url != null);
	}
	
	
	@Override
	protected void extendedRevert(NodeEditions editions) {
		editions.remove(UpdateUrl.class);
		fireEventInEDT(new UrlReverted(node));
	}
	
	@Override
	protected Collection<UpdateRequest> getExtendedUpdates() {
		String newUrl = ui.url.getText();
		UpdateRequest out = new UpdateUrl(newUrl);
		return Arrays.asList(out);
	}
	
	


}
