package ru.kc.main.tab.tools;

import java.awt.Component;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class PrevNextTabModel {
	
	private static final Log log = LogFactory.getLog(PrevNextTabModel.class);
	
	public static interface Listener {
		
		void onPrevSelected(Component tab);
		
		void onNextSelected(Component tab);
		
	}
	
	Listener listener;
	
	//model
	ArrayList<Component> stack = new ArrayList<Component>();
	int prevHead = -1;

	
	public void setListener(Listener listener) {
		this.listener = listener; 
	}

	public void setCurrent(Component tab) {
		addToStack(tab);
		printPrevStack();
	}

	private void addToStack(Component tab) {
		checkForInsert(tab);
		ArrayList<Component> newStack = new ArrayList<Component>();
		for(int i=0; i < prevHead+1; ++i){
			newStack.add(stack.get(i));
		}
		newStack.add(tab);
		
		stack = newStack;
		prevHead = stack.size()-1;
		
	}

	private void checkForInsert(Component tab) {
		if(stack.size() > 0 && stack.get(stack.size()-1).equals(tab)){
			throw new IllegalArgumentException("element already last in stack: "+tab);
		}
	}

	public Component removeAndGetNextToView(Component tab) {
		Component nextToView = findNextAfterClearStack(tab);
		clearStack(tab);
		printPrevStack();
		return nextToView;
	}
	
	public Component findNextAfterClearStack(Component toDelete){
		if(prevHead > -1){
			Component cur = stack.get(prevHead);
			if( ! cur.equals(toDelete)){
				return cur;
			} else {
				if(prevHead > 0){
					return stack.get(prevHead - 1);
				} 
				else if(prevHead < stack.size()-1){
					return stack.get(prevHead + 1);
				}
			}
		}
		return null;
	}
	
	public void clearStack(Component tab){
		ArrayList<Component> newStack = new ArrayList<Component>();
		for(int i=0; i < stack.size(); ++i){
			Component comp = stack.get(i);
//			if(comp.)
		}
	}



	public void prevTabRequest() {
		log.info("prevTabRequest()");
	}

	public void nextTabRequest() {
		log.info("nextTabRequest()");
	}

	public boolean canPrevTab() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean canNextTab() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void printPrevStack() {
		log.debug("prev stack: "+stack);
	}

}
