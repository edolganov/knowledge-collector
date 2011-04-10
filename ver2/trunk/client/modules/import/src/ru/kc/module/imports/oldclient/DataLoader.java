package ru.kc.module.imports.oldclient;

import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import ru.kc.module.imports.oldclient.oldmodel.Root;
import ru.kc.util.xml.ObjectToXMLConverter;
import ru.kc.util.xml.XmlStore;

public class DataLoader extends  XmlStore<Root>{

	@Override
	protected void config(ObjectToXMLConverter<Root> converter) {
		String packName = "ru.kc.module.imports.oldclient.oldmodel";
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

}
