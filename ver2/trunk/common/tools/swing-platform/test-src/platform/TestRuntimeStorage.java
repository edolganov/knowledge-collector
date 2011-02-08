package platform;

import org.junit.Test;

import ru.kc.platform.runtimestorage.RuntimeStorageService;

import junit.framework.Assert;

public class TestRuntimeStorage extends Assert{
	
	@Test
	public void autoRemoveUnusedDataFromStorage(){
		RuntimeStorageService storage = new RuntimeStorageService();
		for (int i = 0; i < 100; i++) {
			Integer domain = new Integer(i);
			for (int j = 0; j < 100; j++) {
				Integer key = new Integer(j);
				String value = "";
				storage.putWithWeakReferenceDomain(domain, key, value);
			}
		}
		
		System.gc();
		
		for (int i = 0; i < 100; i++) {
			Integer domain = new Integer(i);
			for (int j = 0; j < 100; j++) {
				Integer key = new Integer(j);
				Object removedValue = storage.get(domain, key);
				assertEquals(null, removedValue);
			}
		}
		
	}

}
