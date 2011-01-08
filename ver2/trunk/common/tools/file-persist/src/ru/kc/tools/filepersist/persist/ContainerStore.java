package ru.kc.tools.filepersist.persist;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.util.xml.ObjectToXMLConverter;
import ru.kc.util.xml.XmlStore;

public class ContainerStore extends XmlStore<Container>{

	@Override
	protected void config(ObjectToXMLConverter<Container> converter) {
		String packName = "ru.kc.tools.filepersist";
		Reflections reflections = new Reflections(new ConfigurationBuilder()
        .setUrls(ClasspathHelper.getUrlsForPackagePrefix(packName))
        .setScanners(new TypeAnnotationsScanner())
        .filterInputsBy(new FilterBuilder.Include(FilterBuilder.prefix(packName))));
		
		Set<Class<?>> all = reflections.getTypesAnnotatedWith(XStreamAlias.class);
		if(all == null || all.size() == 0) throw new IllegalStateException("no data model classes with annotation @XStreamAlias");
		//log.info("found "+all.size()+" classes");
		
		for (Class<?> clazz : all) {
			converter.configureAliases(clazz);
		}
	}
	
	
	public void save(Container container) throws IOException {
		File file = container.getFile();
		saveFile(file, container);
	}
	
	public Container load(File file) throws IOException{
		Container container = loadFile(file);
		container.setFile(file);
		return container;
	}

}
