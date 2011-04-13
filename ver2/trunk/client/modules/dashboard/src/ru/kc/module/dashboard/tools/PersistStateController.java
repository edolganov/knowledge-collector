package ru.kc.module.dashboard.tools;

import javax.swing.JSplitPane;

import ru.kc.common.controller.Controller;
import ru.kc.module.dashboard.ui.Dashboard;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.platform.profile.Profile;
import ru.kc.platform.profile.event.ProfileLoaded;
import ru.kc.platform.profile.event.ProfilePersisting;

@Mapping(Dashboard.class)
public class PersistStateController extends Controller<Dashboard>{
	
	private static final String RIGHT_DIVIDER_LOCATION = "dashboard-right-divider-location";
	private static final String LEFT_DIVIDER_LOCATION = "dashboard-left-divider-location";
	
	JSplitPane leftSplitPane;
	JSplitPane rightSplitPane;
	
	
	@Override
	public void init() {
		
		leftSplitPane = ui.jSplitPane1;
		//leftSplitPane.setDividerLocation(180);

		rightSplitPane = ui.jSplitPane2;
	}
	
	
	@EventListener
	public void onProfileLoaded(ProfileLoaded event){
		loadState(event.profile);
	}


	@EventListener
	public void onProfilePersisting(ProfilePersisting event){
		saveState(event.profile);
	}


	private void saveState(Profile profile) {
		int rightDividerLocation = rightSplitPane.getDividerLocation();
		profile.put(RIGHT_DIVIDER_LOCATION, ""+rightDividerLocation);
		
		int leftDividerLocation = leftSplitPane.getDividerLocation();
		profile.put(LEFT_DIVIDER_LOCATION, ""+leftDividerLocation);
		
	}
	
	private void loadState(Profile profile) {
		try {
			int location = Integer.parseInt(profile.get(RIGHT_DIVIDER_LOCATION));
			rightSplitPane.setDividerLocation(location);
		}catch (Exception e) {
			//nothing
		}
		
		try {
			int location = Integer.parseInt(profile.get(LEFT_DIVIDER_LOCATION));
			leftSplitPane.setDividerLocation(location);
		}catch (Exception e) {
			//nothing
		}
		
	}

}
