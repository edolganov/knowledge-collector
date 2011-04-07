package ru.kc.platform.profile.event;

import ru.kc.platform.event.Event;
import ru.kc.platform.profile.Profile;

public class ProfilePersisting extends Event{
	
	public final Profile profile;

	public ProfilePersisting(Profile profile) {
		super();
		this.profile = profile;
	}
	
	

}
