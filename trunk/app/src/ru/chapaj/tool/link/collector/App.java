package ru.chapaj.tool.link.collector;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.UIManager;

import ru.chapaj.tool.link.collector.controller.Controller;
import ru.chapaj.tool.link.collector.controller.IconController;
import ru.chapaj.tool.link.collector.controller.MainController;
import ru.chapaj.tool.link.collector.controller.NodeInfoController;
import ru.chapaj.tool.link.collector.controller.SearchController;
import ru.chapaj.tool.link.collector.controller.SnapshotListController;
import ru.chapaj.tool.link.collector.controller.TreeController;
import ru.chapaj.tool.link.collector.model.DataContainer;
import ru.chapaj.tool.link.collector.model.Dir;
import ru.chapaj.tool.link.collector.ui.AppFrame;

/**
 * <pre>
 * Баги:
 * - появление диалогов не в середине приложения, а где-то сбоку, но дефаулт диалоги появлются где надо - посмотртеть как они это делают
 * - бага при отсутсвии файла (не смог пока повторить)
 * - отключать глобальные клавиши при показе диалога (иначе появляется второй диалог)
 * 
 * + при создании папки создавать ее внизу папок, а не внизу ссылок
 * + при нажатии на корень не изчезают свойства ноды
 * + при нажатии Updte не ставится флаг что файл модифицирован
 * + не назначать гор.клавиши без доп.клавиш
 * + разрешить добавление папки и линка если выбрана не папка - просто переходить на предка
 * + не выполнять сохранение, если файл не изменился
 * + не удалилась пустая корневая папка
 * + не работает глобальное нажатие кнопик delete (http://forums.sun.com/thread.jspa?threadID=354202&tstart=7244 http://www.javaspecialists.co.za/archive/Issue007.html)
 * + в ноду не добавляет второй элемент (http://forums.sun.com/thread.jspa?threadID=5372395)
 * + нужен нормальный лейоут менеджер для дерева
 * 
 * 
 *___________________________________________________________________________________________
 * Таски:
 * - сортировка определенных папок (кнопка над деревом, новое свойство для Dir, проблема раскртия снепшота после сортировки)
 * - теги
 * - сериализация приложения для быстрого запуска - swing сериализуем, нужно сделать тоже для бинов и контроллеров
 * - кастомизация иконок дерева
 * - кнопка "не исчезать с экрана"
 * - выделение сохранения данных в отдельный сервис (задачи: всегда сохранять последний стейт, но при этом иметь возможность не сохранять изменения дерева: идея - делать копию дерева (Dif, Link) но не копии строк в них. Разделять инит бандл и текущую версию
 * - прокидывать параметром имя стартового снепшота
 * - доработка парсинга - при невалидной ноде пытаться двигаться дальше (но нужно знать сколько 'r' пропустить до продолжения алгоритма)
 * - логи
 * - кнопка сокрытия всех путей кроме активного
 * - попробывать альтернативную JRE как более быструю и компактуню
 * - назначение иконок для урлов (идти по ссылке - выкачивать хтмл, искать урл и выкачивать файл) 
 * - горячие списки
 * - показывать количество детей в папке (проблематично - т.к. при каждом внешнем изменеии дерева вызывается toString у Dir)
 * - сохранение позиции, размера окна при закрытии
 * - переход на hsql+hibernate
 * 
 * 
 * + открытие виндовых файлов и папок
 * + кнопка обновить снепшот
 * + перевод снепшотов на uuid
 * + категоризация снепшотов
 * + автосохранение
 * + поиск
 * + возможность перемещнеия снепшотов в листе
 * + перемещение нод верх-вниз
 * + открытие страницы по ссылке и дабл клик
 * + сворачивание в трей
 * + снепшоты - удаление
 * + при добавлении снепшота - обновлять ui
 * + переход к единому контейнеру данных
 * + панель snapshot-ов (добавление, загрузка)
 * + добавить ссылку к полю описания
 * + поле описания
 * + рефакторинг UI: переезд на единый UI класс
 * + открытие дерева из файла состояния
 * + сохранение состояния дерева
 * + drug-n-drop нод дерева
 * + горячие клавиши для создания нод
 * + создание стендэлон приложения
 * + картинки для папки и ссылки
 * + редактирование ноды
 * + отображение информации о ноде
 * + при изменении дерева подсвечивать имя с *, после сохранения убирать *
 * + отображать имя файла в заголовке
 * + обновление файла
 * + поддержка глобальных горячих клавиш
 * + удаление ноды
 * + проперти файл (последний файл)
 * + сделать создание ссылки
 * + получание дерева из файла
 * + сделать сохранение дерева
 * 
 * </pre>
 * 
 * @author jenua.dolganov
 *
 */
public class App {
	
	private static App def;
	
	public static App getDefault(){
		if(def == null) def = new App();
		return def;
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		getDefault().initUI();
	}
	
	public static final String APP_TITLE = "Link collector";
	

	private AppFrame ui;
	private DataContainer data;
	
	
	//controllers
	TreeController treeController;
	MainController mainController;
	NodeInfoController nodeInfoController;
	SnapshotListController snapshotListController;
	IconController iconController;
	Informer info;
	
	boolean hasProps,stopGlobalKeys;
	Properties props;
	File homeDir;

	
	public App() {
	}
	
	@SuppressWarnings("unchecked")
	private void initUI() {
		//conf
		homeDir = new File(".");
		
		props = new Properties();
		hasProps = false;
		try {
			props.load(new FileInputStream(new File(homeDir.getPath()+"/conf.properties")));
			hasProps = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//model
		data = new DataContainer();
		Dir root = new Dir("root");
		data.setRoot(root);
		
		//controllers

		treeController = new TreeController();
		mainController = new MainController();
		nodeInfoController = new NodeInfoController();
		snapshotListController = new SnapshotListController();
		iconController = new IconController();
		info = new Informer();
		
		
		//view
		ui = new AppFrame();
		ui.setIconImage(ImageBundle.getDefault().getAppIcon());
		ui.setLocationByPlatform(true);
		ui.initTree(data.getRoot());
		
		init(ui, 
				mainController,
				treeController,
				nodeInfoController,
				snapshotListController,
				iconController,
				new SearchController());
		
		if(hasProps){
			try{
				mainController.processProps(props);
				snapshotListController.processProps(props);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//hot keys
		new GlobalHotKeyListener();
		
		ui.setVisible(true);
		ui.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//скрывать если нажата кнопка, иначе выход
			}
		});
		
		setStopGlobalKeys(false);
		
	}
	
	public void exit(){
		mainController.actionSaveFile(true);
		System.exit(0);
	}
	
	public void show(){
		ui.setVisible(true);
		ui.toFront();
	}
	
	public void hideIfNeed() {
		if(ui.isActive()) ui.setVisible(false);
		
	}
	
	public JDialog getUI(){
		return ui;
	}
	
	private <T extends Component> void init(T ui, Controller<T>... controllers){
		for(Controller<T> c : controllers) c.init(ui);
	}
	
	public Informer informer(){
		return info;
	}

	public NodeInfoController getNodeInfoController() {
		return nodeInfoController;
	}

	public TreeController getTreeController() {
		return treeController;
	}

	public MainController getMainController() {
		return mainController;
	}
	
	public void reloadData(DataContainer newData){
		this.data = newData;
		treeController.processNewData(data);
		snapshotListController.processNewData(data);
	}

	public DataContainer getData() {
		return data;
	}
	
	/**
	 * Обновить данные до последнего корректного состояния
	 * @return
	 */
	public DataContainer refreshData(boolean full){
		snapshotListController.refreshData(data, full);
		return data;
		
	}

	public boolean canUseGlobalKeys() {
		return !stopGlobalKeys;
	}

	public void setStopGlobalKeys(boolean stopGlobalKeys) {
		this.stopGlobalKeys = stopGlobalKeys;
	}


	



}
