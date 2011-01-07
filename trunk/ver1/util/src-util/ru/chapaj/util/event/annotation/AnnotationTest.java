package ru.chapaj.util.event.annotation;

import ru.chapaj.util.event.Event;
import ru.chapaj.util.event.EventManager;

public class AnnotationTest {
	
	static class TestEvent extends Event<String>{

		public TestEvent(String data) {
			super(data);
		}
		
	}
	
	public static void main(String[] args) {
		try {
			EventManager eventManager = new EventManager();
			eventManager.addObjectMethodListeners(new AnnotationTest());
			eventManager.fireEvent(new Object(), new TestEvent("test"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@LastEventListener(TestEvent.class)
	public void f1(TestEvent event){
		System.out.println("call f1");
	}
	
	@EventListener(TestEvent.class)
	public void f2(Object source){
		System.out.println("call f2");
	}
	
	@EventListener(TestEvent.class)
	public void f3(Object source,TestEvent event){
		System.out.println("call f3");
	}
	
	@EventListener(TestEvent.class)
	public void f4(){
		System.out.println("call f4");
	}
	
	@EventListener(TestEvent.class)
	private void f5(){
		System.out.println("call f5");
	}
	
	@LastEventListener(TestEvent.class)
	void f6(){
		System.out.println("call f6");
	}
	
	//bad cases
	
	//@EventListener(TestEvent.class)
	public void invF1(String s){
		System.out.println("bad call!");
	}
	
	//@EventListener(TestEvent.class)
	public void invF2(String s, String s2){
		System.out.println("bad call!");
	}
	
	//@EventListener(TestEvent.class)
	public void invF3(String s, String s2, String s3){
		System.out.println("bad call!");
	}


}
