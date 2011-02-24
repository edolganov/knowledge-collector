package ru.kc.main;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.NodeContainer;
import ru.kc.common.node.event.OpenNodeRequest;
import ru.kc.main.tab.TabModule;
import ru.kc.model.Node;
import ru.kc.model.Text;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.platform.module.Module;
import ru.kc.platform.ui.tabbedform.MainForm;
import ru.kc.platform.ui.tabbedform.TabbedPanel;
import ru.kc.platform.ui.tabs.TabbedWrapper;
import ru.kc.platform.ui.tabs.TabbedWrapper.TabsListener;

@Mapping(MainForm.class)
public class TabsController extends Controller<MainForm> {
	
	JTabbedPane tabs;
	TabbedWrapper tabsWrapper;

	@Override
	public void init() {
		TabbedPanel root = ui.root;
		tabs = root.tabs;
		tabs.remove(root.testTab1);
		tabs.remove(root.testTab2);
		
		tabsWrapper = new TabbedWrapper(tabs);
		tabsWrapper.addTab(createTab("dashboard"), "dashboard", false);
		tabsWrapper.addTab(createTab("dashboard"), "test concurrent modification");
		
		tabsWrapper.addListener(new TabsListener() {
			
			@Override
			public void onClosed(Component comp, int index, String text) {
				Module.removeAllListneres(comp);
			}
			
			@Override
			public boolean canClose(Component comp, int index, String text) {
				//default icon, custom title
				int n = JOptionPane.showConfirmDialog(
					ui,
				    "Close? "+text,
				    "???",
				    JOptionPane.YES_NO_OPTION);


				return n == 0;
			}
		});
		
	}
	
	@EventListener(OpenNodeRequest.class)
	public void onOpenNodeRequest(OpenNodeRequest event){
		Node node = event.node;
		Component existTab = findExistNodeTab(node);
		if(existTab != null){
			focusRequest(existTab);
		} else {
			Component createdTab = createTab(node);
			focusRequest(createdTab);
		}

	}
	
	private Component findExistNodeTab(Node node) {
		for (int i = 0; i < tabs.getComponentCount(); i++) {
			Component tab = tabs.getComponent(i);
			if(tab instanceof TabModule){
				Component component = ((TabModule)tab).getComponent();
				if(component instanceof NodeContainer<?>){
					Node candidat = ((NodeContainer<?>) component).getNode();
					if(candidat.equals(node)){
						return tab;
					}
				}
			}
		}
		return null;
	}
	
	
	private void focusRequest(Component component) {
		tabs.setSelectedComponent(component);
	}



	private TabModule createTab(Node node) {
		if(node instanceof Text){
			TabModule tab = createTab("text-editor");
			tabsWrapper.addTab(tab, node.getName(), true);
			Component component = tab.getComponent();
			setNode(component,node);
			return tab;
		}
		else throw new IllegalArgumentException("unknow type for open tab: "+node);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setNode(Component component, Node node) {
		if(component instanceof NodeContainer<?>){
			((NodeContainer) component).setNode(node);
		}
		
	}

	private TabModule createTab(String mapping){
		Component component = (Component)instanceByMapping(mapping);
		return createTab(component);
	}
	
	private TabModule createTab(Component component){
		TabModule tab = new TabModule();
		tab.setAppContext(appContext);
		tab.setComponent(component);
		return tab;
	}
	


}
