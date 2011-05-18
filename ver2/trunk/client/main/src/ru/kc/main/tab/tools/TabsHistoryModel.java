package ru.kc.main.tab.tools;

import java.awt.Component;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class TabsHistoryModel {
	
	private static final Log log = LogFactory.getLog(TabsHistoryModel.class);
	
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
			return component.toString()+"@"+component.hashCode();
		}
		
		
	}
	
	Listener listener;
	boolean debugMode = false;
	
	//model
	ArrayList<StackElem> stack = new ArrayList<StackElem>();
	StackElem curStackHead = null;

	
	public void setListener(Listener listener) {
		this.listener = listener; 
	}

	public void addToStack(Component tab) {
		if(isAlreadyLast(tab)) {
			return;
		}
		
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
		printStack();
		
	}

	private boolean isAlreadyLast(Component toInsert) {
		if(stack.size() > 0){
			StackElem last = stack.get(stack.size()-1);
			if(last.equals(curStackHead) && last.component.equals(toInsert)){
				return true;
			}
		}
		return false;
	}

	public Component removeAndGetNextToView(Component tab) {
		checkForRemove();
		removeFromStack(tab);
		printStack();
		return curStackHead != null? curStackHead.component : null;
	}
	
	private void checkForRemove() {
		if(stack.size() == 0 || curStackHead == null){
			throw new IllegalArgumentException("stack is empty");
		}
	}
	
	private void removeFromStack(Component toDelete){
		ArrayList<StackElem> oldStack = stack;
		StackElem oldCur = curStackHead;
		ArrayList<StackElem> newStack = new ArrayList<StackElem>();
		StackElem prevElem = new StackElem(null); //stub
		for(int i= stack.size()-1; i > -1; i--){
			StackElem elem = stack.get(i);
			if( ! elem.component.equals(toDelete)){
				if(! elem.component.equals(prevElem.component)){
					newStack.add(0, elem);
					prevElem = elem;					
				}
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
		if(canPrevTab()){
			int index = stack.indexOf(curStackHead);
			StackElem prev = stack.get(index - 1);
			curStackHead = prev;
			
			listener.onPrevSelected(prev.component);
			printStack();
		}
	}

	public void nextTabRequest() {
		if(canNextTab()){
			int index = stack.indexOf(curStackHead);
			StackElem next = stack.get(index + 1);
			curStackHead = next;
			
			listener.onNextSelected(next.component);
			printStack();
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
	
	private void printStack() {
		if(! debugMode) return;
		
		String toPrint = null;
		if(stack.size() == 0){
			toPrint = "[]";
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for (int i = 0; i < stack.size()-1; i++) {
				StackElem elem = stack.get(i);
				sb.append(wrapElem(elem)).append(", ");
			}
			sb.append(wrapElem(stack.get(stack.size()-1))).append("]");
			toPrint = sb.toString();
		}
		log.debug("stack: "+toPrint);
	}
	
	private String wrapElem(StackElem elem){
		if(elem.equals(curStackHead)){
			return ">> "+elem+" <<";
		} else {
			return elem.toString();
		}
	}

}
