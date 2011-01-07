package test;

import ru.kc.tools.filepersist.PersistService;

public class PersistTest {

	public static void main(String[] args) throws Exception {
		PersistService service = new PersistService();
		service.init("e:\\_kc_test");
		
		System.out.println("done");
	}
	
}
