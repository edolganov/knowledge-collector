package ru.kc.common.search.event;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import ru.kc.model.Node;
import ru.kc.platform.domain.annotation.DomainSpecific;
import ru.kc.platform.event.Request;

@DomainSpecific
public class SearchRequest extends Request<List<Node>>{
	
	public final List<String> likeElements;

	public SearchRequest(String query) {
		ArrayList<String> elements = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(query);
		while(tokenizer.hasMoreTokens()){
			elements.add(tokenizer.nextToken());
		}
		likeElements = elements;
	}

	public SearchRequest(List<String> likeElements) {
		this.likeElements = likeElements;
	}
	
	
	
	

}
