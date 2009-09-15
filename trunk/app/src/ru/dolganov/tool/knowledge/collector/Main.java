package ru.dolganov.tool.knowledge.collector;

import ru.chapaj.util.app.GenericMain;

/**
 * девиз:
 * - это не файловый менеджер. 
 * файловый менджер - это инструмент для хранения 
 * данных а у меня не данные, а знания. знаний меньше чем данных. они хранятся в 
 * одном месте. Из этого выбивается только крупные файлы, но это фигня. Иногда
 * просто нельзя отобразить знание только текстом. Нужны бинарные файлы. Для этого
 * и используется фс хранение.
 * 
 * 
 * @author jenua.dolganov
 *
 */
public class Main extends GenericMain {
	
	public static void main(String[] args) {
		setLookAndFeel();
		App.getDefault().init();
	}

}
