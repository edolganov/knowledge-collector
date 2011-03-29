package ru.kc.common.command;

import java.awt.Frame;

import ru.kc.common.Context;
import ru.kc.common.dialog.Dialogs;
import ru.kc.platform.annotations.Inject;
import ru.kc.platform.command.AbstractCommand;
import ru.kc.platform.domain.DomainMember;
import ru.kc.tools.filepersist.Factory;
import ru.kc.tools.filepersist.PersistService;
import ru.kc.tools.filepersist.Tree;
import ru.kc.tools.filepersist.Updater;

public abstract class Command<T> extends AbstractCommand<T> {
	
	@Inject
	protected Context context;
	protected PersistService persist;
	protected Tree persistTree;
	protected Factory persistFactory;
	protected Frame rootUI;
	protected Updater updater;
	protected Dialogs dialogs;
	
	public Command() {
		super();
	}

	public Command(DomainMember domainMember) {
		super(domainMember);
	}


	@Override
	protected void beforeInvoke() {
		persist = context.persistService;
		persistTree = persist.tree();
		persistFactory = persist.factory();
		rootUI = (Frame)appContext.rootUI;
		updater = persist.updater();
		dialogs = context.dialogs;
	}

}
