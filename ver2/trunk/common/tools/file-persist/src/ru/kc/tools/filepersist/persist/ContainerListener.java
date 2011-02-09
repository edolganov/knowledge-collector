package ru.kc.tools.filepersist.persist;

import ru.kc.tools.filepersist.model.impl.Container;

public interface ContainerListener {

	void onSaved(Container container);

}
