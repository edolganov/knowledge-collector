package ru.chapaj.util.xml;

import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.annotations.Annotations;

/**
 * Фасад для конвертора объекта в xml и обратно
 *
 * @author chapaj
 * @param <T> - класс конвертируемого объекта
 */
public class ObjectToXMLConverter<T> {
    private static final String XML_PREFFIX = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

    private XStream xstream;

    public ObjectToXMLConverter(boolean autodetectAnnotations) {
        xstream = new XStream();
        xstream.autodetectAnnotations(autodetectAnnotations);
    }
    
    public ObjectToXMLConverter() {
        this(true);
    }
    
    public ObjectToXMLConverter(Class<T> clazz){
    	this(clazz, null);
    }

    /**
     * @param clazz      класс объекта (совпадает с классом шаблона T)
     * @param clazzAlias строка - псевдоним класса в xml
     */
    public ObjectToXMLConverter(Class<T> clazz, String clazzAlias) {
        this();
        if (clazzAlias != null) xstream.alias(clazzAlias, clazz);
    }

    /**
     * если объект содержит анотации из пакета
     * <b>com.thoughtworks.xstream.annotations</b>,
     * то перед работой необходимо явно их сконфигурировать
     * 
     * без этого будет ошибка при первом считывании (т.к. xtream не знает во что преобразовать объект - его еще нет в кеше xtream)
     */
    public void configureAliases(Class<?>... clazz) {
        //Annotations.configureAliases(xstream, clazz);
        xstream.processAnnotations(clazz);
    }


    public String toXMLString(T object) {
        String out = xstream.toXML(object);
        return XML_PREFFIX + out;
    }

    @SuppressWarnings("unchecked")
    public T toObject(String xmlString) {
        T out = (T) xstream.fromXML(xmlString);
        return out;
    }

    public XStream getXStream(){
		return xstream;
	}

}
