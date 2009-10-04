package ru.dolganov.tool.knowledge.collector;

/**
 * Слушатель системного события
 * @author jenua.dolganov
 *
 */
public interface AppListener {
	public void onAction(Object source, String action, Object... data);
}
