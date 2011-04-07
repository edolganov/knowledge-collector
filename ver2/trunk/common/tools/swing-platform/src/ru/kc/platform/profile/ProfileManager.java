package ru.kc.platform.profile;

import java.io.File;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.domain.RootDomainMember;
import ru.kc.platform.event.EventManager;
import ru.kc.platform.profile.event.ProfileLoaded;
import ru.kc.platform.profile.event.ProfilePersisting;
import ru.kc.util.xml.ObjectToXMLConverter;
import ru.kc.util.xml.XmlStore;

public class ProfileManager {
	
	private static final Log log = LogFactory.getLog(ProfileManager.class);
	
	private ProfileImpl profile;
	private File dir = new File("./settings/");
	private File file = null;
	private String magicFileName;
	private XmlStore<ProfileImpl> store = new XmlStore<ProfileImpl>() {
		
		@Override
		protected void config(ObjectToXMLConverter<ProfileImpl> converter) {
			converter.configureAliases(ProfileImpl.class);
		}
	};
	private EventManager eventManager;
	
	public ProfileManager(EventManager eventManager) {
		super();
		this.eventManager = eventManager;
	}

	public void load() {
		dir.mkdir();
		
		try {
			file = initFile();
			if(!file.exists()) return;
			profile = store.loadFile(file);
			
			if(profile != null){
				eventManager.fireEventInEDT(new RootDomainMember(this), new ProfileLoaded(profile));
			}
			
		} catch (Exception e) {
			log.error("", e);
		}
		
	}

	private File initFile() {
		Properties systemProps = System.getProperties();
		File[] roots = File.listRoots();
		if(roots == null) roots = new File[0];
		
		//специфичное для данного компьютера имя
		magicFileName = "" 
		 + "--"
		 + systemProps.getProperty("os.name")
		 + "--"
		 + Runtime.getRuntime().availableProcessors()
		 + "--"
		 + (roots.length > 0 ? ""+roots[0].getTotalSpace() : systemProps.getProperty("java.io.tmpdir"));
		
		return new File("./settings/comp-"+magicFileName.hashCode()+".xml");
	}

	public void persist(){
		try {
			if(profile == null){
				profile = new ProfileImpl();
			}
			profile.put("cur-system", magicFileName);
			eventManager.fireEventInEDT(new RootDomainMember(this), new ProfilePersisting(profile));
			
			store.saveFile(file, profile, false);
		} catch (Exception e) {
			log.error("", e);
		}
		
	}

}
