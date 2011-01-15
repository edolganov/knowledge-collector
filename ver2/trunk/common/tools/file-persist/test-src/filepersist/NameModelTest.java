package filepersist;

import org.junit.Test;

import ru.kc.tools.filepersist.persist.model.NameModel;

import junit.framework.Assert;

public class NameModelTest extends Assert {
	
	@Test
	public void nameIteration(){
		NameModel model = new NameModel();
		
		assertEquals("000.xml", model.first(".xml"));
		assertEquals("001.xml", model.next("000.xml"));
		assertEquals("010.xml", model.next("009.xml"));
		assertEquals("100.xml", model.next("099.xml"));
		assertEquals("1000.xml", model.next("999.xml"));
		assertEquals("10000.xml", model.next("9999.xml"));
		
		assertEquals("000", model.first());
		assertEquals("001", model.next("000"));
		assertEquals("010", model.next("009"));
		assertEquals("100", model.next("099"));
		assertEquals("1000", model.next("999"));
		assertEquals("10000", model.next("9999"));
		
		assertEquals("001.", model.next("000."));
		assertEquals("010.", model.next("009."));
		assertEquals("100.", model.next("099."));
		assertEquals("1000.", model.next("999."));
		assertEquals("10000.", model.next("9999."));
		

	}

}
