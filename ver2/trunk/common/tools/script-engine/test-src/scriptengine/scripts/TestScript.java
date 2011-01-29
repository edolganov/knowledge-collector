package scriptengine.scripts;

import ru.kc.tools.scriptengine.model.annotations.Mapping;

@Mapping("test")
public class TestScript {
	
	public String getText(){
		String out = ""+this;
		//System.out.println(out);
		return out;
	}

}
