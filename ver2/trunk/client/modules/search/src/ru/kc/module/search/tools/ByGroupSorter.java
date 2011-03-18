package ru.kc.module.search.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.kc.model.Node;
import ru.kc.util.collection.Pair;



public class ByGroupSorter implements Iterable<Group> {
	
	GroupComparator typeComparator = new GroupComparator();
	ArrayList<Group> groups = new ArrayList<Group>();
	
	public ByGroupSorter(List<Node> unsorted) {
		Group[] sortedGroups = new Group[typeComparator.getGroupsCount()];
		for(Node node : unsorted){
			Pair<Integer, Class<?>> index = typeComparator.getIndex(node.getClass());
			Group group = getGroup(sortedGroups, index);
			group.nodes.add(node);
		}
		for(Group group : sortedGroups){
			if(group != null){
				//Collections.sort(group.nodes, new NodeByNameComparator());
				groups.add(group);
			}
		}
	}

	private Group getGroup(Group[] sortedGroups, Pair<Integer, Class<?>> index) {
		Integer arrayIndex = index.getFirst();
		Group out = sortedGroups[arrayIndex];
		if(out == null){
			out = new Group(index.getSecond(), new ArrayList<Node>());
			sortedGroups[arrayIndex] = out;
		}
		return out;
	}

	@Override
	public Iterator<Group> iterator() {
		return groups.iterator();
	}

}
