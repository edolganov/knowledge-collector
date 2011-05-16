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
	
	private static class StackElem {
		Component component;
		public StackElem(Component component) {
			super();
			this.component = component;
		}
		@Override
		public String toString() {
			return "Tab"+"@"+component.hashCode();
		}
		
		
	}
	
	Listener listener;
	
	//model
	ArrayList<StackElem> stack = new ArrayList<StackElem>();
	StackElem curStackHead = null;

	
	public void setListener(Listener listener) {
		this.listener = listener; 
	}

	public void setCurrent(Component tab) {
		addToStack(tab);
		printPrevStack();
	}

	private void addToStack(Component tab) {
		checkForInsert(tab);
		
		ArrayList<StackElem> oldStack = stack;
		StackElem oldCur = curStackHead;
		ArrayList<StackElem> newStack = new ArrayList<StackElem>();
		for(StackElem elem : oldStack){
			if( ! elem.equals(oldCur)){
				newStack.add(elem);
			} else {
				newStack.add(oldCur);
				break;
			}
		}
		newStack.add(new StackElem(tab));
		
		stack = newStack;
		curStackHead = stack.get(stack.size()-1);
		
	}

	private void checkForInsert(Component tab) {
		if(stack.size() > 0 && stack.get(stack.size()-1).equals(tab)){
			throw new IllegalArgumentException("element already last in stack: "+tab);
		}
	}

	public Component removeAndGetNextToView(Component tab) {
		checkForRemove();
		clearStack(tab);
		printPrevStack();
		return curStackHead != null? curStackHead.component : null;
	}
	
	private void checkForRemove() {
		if(stack.size() == 0 || curStackHead == null){
			throw new IllegalArgumentException("stack is empty");
		}
	}
	
	public void clearStack(Component toDelete){
		ArrayList<StackElem> oldStack = stack;
		StackElem oldCur = curStackHead;
		ArrayList<StackElem> newStack = new ArrayList<StackElem>();
		for(StackElem elem : oldStack){
			if( ! elem.component.equals(toDelete)){
				newStack.add(elem);
			}
		}
		
		StackElem newCur = null;
		Component cur = oldCur.component;
		if( ! cur.equals(toDelete)){
			newCur = oldCur;
		} else {
			int curPrevIndex = oldStack.indexOf(oldCur);
			if(curPrevIndex > 0){
				newCur =  oldStack.get(curPrevIndex - 1);
			} 
			else if(curPrevIndex < stack.size()-1){
				newCur =  oldStack.get(curPrevIndex + 1);
			}
		}
		
		stack = newStack;
		curStackHead = newCur;
	}



	public void prevTabRequest() {
		log.debug("prevTabRequest. cur="+curStackHead);
		if(canPrevTab()){
			int index = stack.indexOf(curStackHead);
			StackElem prev = stack.get(index - 1);
			listener.onPrevSelected(prev.component);
			curStackHead = prev;
		}
	}

	public void nextTabRequest() {
		log.debug("nextTabRequest. cur="+curStackHead);
		if(canNextTab()){
			int index = stack.indexOf(curStackHead);
			StackElem next = stack.get(index + 1);
			listener.onNextSelected(next.component);
			curStackHead = next;
		}
	}

	public boolean canPrevTab() {
		if(stack.size() > 1){
			int index = stack.indexOf(curStackHead);
			return index > 0;
		}
		return false;
	}
	
	public boolean canNextTab() {
		if(stack.size() > 1){
			int index = stack.indexOf(curStackHead);
			return index < stack.size()-1;
		}
		return false;
	}
	
	private void printPrevStack() {
		log.debug("stack: "+stack);
	}

}
