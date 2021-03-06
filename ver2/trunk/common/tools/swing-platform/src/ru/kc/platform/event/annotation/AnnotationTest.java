package ru.kc.platform.event.annotation;

import ru.kc.platform.domain.DomainMember;
import ru.kc.platform.domain.RootDomainMember;
import ru.kc.platform.event.Event;
import ru.kc.platform.event.EventManager;

public class AnnotationTest {
	
	static class TestEvent extends Event{
		
		String data;

		public TestEvent(String data) {
			this.data = data;
		}
		
	}
	
	public static void main(String[] args) {
		try {
			EventManager eventManager = new EventManager();
			eventManager.addObjectMethodListeners(new AnnotationTest());
			eventManager.fireEventInEDT(new RootDomainMember(new Object()), new TestEvent("test"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@LastEventListener
	public void f1(TestEvent event){
		System.out.println("call f1");
	}
	
	@EventListener
	public void f2(Object source){
		System.out.println("call f2");
	}
	
	@EventListener
	public void f3(Object source,TestEvent event){
		System.out.println("call f3");
	}
	
	@EventListener
	public void f4(){
		System.out.println("call f4");
	}
	
	@SuppressWarnings("unused")
	@EventListener
	private void f5(){
		System.out.println("call f5");
	}
	
	@LastEventListener
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
