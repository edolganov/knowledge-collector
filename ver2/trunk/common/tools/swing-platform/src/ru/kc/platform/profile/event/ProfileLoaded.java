package ru.kc.platform.profile.event;

import ru.kc.platform.event.Event;
import ru.kc.platform.profile.Profile;

public class ProfileLoaded extends Event{
	
	public final Profile profile;

	public ProfileLoaded(Profile profile) {
		super();
		this.profile = profile;
	}
	
	

}
