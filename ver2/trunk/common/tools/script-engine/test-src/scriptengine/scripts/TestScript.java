package scriptengine.scripts;

import scriptengine.Mapping;

@Mapping("test")
public class TestScript {
	
	public String getText(){
		String out = ""+this;
		//System.out.println(out);
		return out;
	}

}
