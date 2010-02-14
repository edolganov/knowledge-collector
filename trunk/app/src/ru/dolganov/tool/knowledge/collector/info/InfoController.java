package ru.dolganov.tool.knowledge.collector.info;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import model.knowledge.Dir;
import model.knowledge.Link;
import model.knowledge.NodeMeta;
import model.knowledge.TextData;

import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.annotation.ControllerInfo;
import ru.dolganov.tool.knowledge.collector.main.MainWindow;
import ru.dolganov.tool.knowledge.collector.model.HasNodeMetaParams;
import ru.dolganov.tool.knowledge.collector.tree.TreeOps;
import ru.chapaj.tool.link.collector.ui.component.PropertyTextAreaPanel;
import ru.chapaj.util.Check;
import ru.chapaj.util.swing.tree.TreeNodeAdapter;

@ControllerInfo(target=MainWindow.class)
public class InfoController extends Controller<MainWindow> implements HasNodeMetaParams{
	static enum Mode {
		link,dir,text,none
	}
	
	MainWindow ui;
	
	BasicInfo basicInfo = new BasicInfo();
	LinkInfo linkInfo = new LinkInfo();
	NoteInfo noteInfo = new NoteInfo();
	Mode curMode = Mode.none;
	
	@Override
	public void init(MainWindow ui_) {
		this.ui = ui_;
		ui.tree.addTreeNodeListener(new TreeNodeAdapter(){
			@Override
			public void onNodeSelect(DefaultMutableTreeNode node) {
				if(node == null) hide();
				else if(node.isRoot()) hide();
				else show(node);
				ui.infoPanel.validate();
				ui.infoPanel.repaint();
			}
		});
		
		basicInfo.name.label.setText("name");
		basicInfo.description.label.setText("description");
		initSaveOpByButton(basicInfo.name.textField);
		initSaveOpByButton(basicInfo.description.textArea);
		
		initArea(basicInfo.description);
		initWrap(basicInfo.description.textArea,basicInfo.wrapB);
		ActionListener actionListener = new  ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
			
		};
		basicInfo.jButton.addActionListener(actionListener);
		
		linkInfo.name.label.setText("name");
		linkInfo.description.label.setText("description");
		linkInfo.url.label.setText("url");
		initSaveOpByButton(linkInfo.name.textField);
		initSaveOpByButton(linkInfo.description.textArea);
		initSaveOpByButton(linkInfo.url.textField);
		
		initArea(linkInfo.description);
		initWrap(linkInfo.description.textArea,linkInfo.wrapB);
		linkInfo.jButton.addActionListener(actionListener);
		
		noteInfo.name.label.setText("name");
		noteInfo.description.label.setText("text");
		initSaveOpByButton(noteInfo.name.textField);
		initSaveOpByButton(noteInfo.description.textArea);
		
		noteInfo.jButton.addActionListener(actionListener);
		initArea(noteInfo.description);
		initWrap(noteInfo.description.textArea,noteInfo.wrapB);
	}
	
	KeyStroke keyStroke = KeyStroke.getKeyStroke("control S");
	
	private void initSaveOpByButton(JTextComponent textField) {
		textField.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(! e.isConsumed() && e.getKeyCode() == KeyEvent.VK_S && e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK){
					//System.out.println("save!");
					update();
					e.consume();
				}
			}
			
		});
		
	}

	protected void update() {
		if(Mode.none == curMode) return;
		
		HashMap<String, String> params = new HashMap<String, String>(2);
		if(Mode.dir == curMode){
			params.put(Params.name.toString(), basicInfo.name.getText());
			params.put(Params.description.toString(), basicInfo.description.getText());
		}
		else if(Mode.link == curMode){
			String name = linkInfo.name.getText();
			String url = linkInfo.url.getText();
			params.put(Params.name.toString(), name);
			params.put(Params.description.toString(), linkInfo.description.getText());
			params.put(Params.url.toString(), url);
		}
		else if(Mode.text == curMode){
			params.put(Params.name.toString(), noteInfo.name.getText());
			params.put(Params.text.toString(), noteInfo.description.getText());
		}
		else return;
		
		TreeOps.updateCurrentTreeNode(params);
		
		
	}

	Color empty = new Color(212,208,200);
	Color active = Color.WHITE;
	
	Object timerSync = new Object();
	boolean timerActive = false;
	String currentBigText;
	Timer timer = null;
	
	void initArea(final PropertyTextAreaPanel area){
		area.textArea.setBackground(empty);

		area.textArea.addFocusListener(new FocusAdapter(){
			
			@Override
			public void focusGained(FocusEvent e) {
				area.textArea.setBackground(active);
				//не делаем автосохранения текста
//				synchronized (timerSync) {
//					if(!timerActive){
//						//System.out.println("start timer");
//						currentBigText = getBigText();
//						timer = new Timer("info-controller-timer");
//						timer.schedule(new TimerTask(){
//
//							@Override
//							public void run() {
//								doTimerSaveTask();
//							}
//							
//						}, 4000, 4000);
//						timerActive = true;
//					}
//				}
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				//не делаем автосохранения текста
				//stopSaveTimer();
				checkEmptyArea(area);
			}
		});
	}
	
	void checkEmptyArea(final PropertyTextAreaPanel area){
		if(!Check.isEmpty(area.getText())){
			area.textArea.setBackground(active);
		}
		else {
			area.textArea.setBackground(empty);
		}
	}
	
	
	private void initWrap(final JTextArea text, final JButton b){
		text.setLineWrap(true);
		b.setText("unwrap");
		b.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean lineWrap = text.getLineWrap();
				if(lineWrap){
					b.setText("wrap");
					text.setLineWrap(false);
				}
				else {
					b.setText("unwrap");
					text.setLineWrap(true);
				}
			}
			
		});
	}
	

	

	protected void show(DefaultMutableTreeNode node) {
		hide();
		Object uo = node.getUserObject();
		if(! (uo instanceof NodeMeta)){
			hide();
			return;
		}
		
		NodeMeta ob = (NodeMeta)uo;
		if(ob instanceof Dir){
			curMode = Mode.dir;
			ui.infoPanel.add(basicInfo);
			basicInfo.name.setText(ob.getName());
			basicInfo.description.setText(ob.getDescription());
			checkEmptyArea(basicInfo.description);
		}
		else if(ob instanceof Link){
			curMode = Mode.link;
			Link l = (Link) ob;
			ui.infoPanel.add(linkInfo);
			linkInfo.name.setText(ob.getName());
			linkInfo.description.setText(ob.getDescription());
			linkInfo.url.setText(l.getUrl());
			checkEmptyArea(linkInfo.description);
		}
		else if(ob instanceof TextData){
			curMode = Mode.text;
			ui.infoPanel.add(noteInfo);
			noteInfo.name.setText(ob.getName());
			noteInfo.description.setText((String)dao.getExternalData(ob).get(Params.text.toString()));
			checkEmptyArea(noteInfo.description);
		}

		
	}

	protected void hide() {
		//doTimerSaveTask(true);
		//stopSaveTimer();
		ui.infoPanel.removeAll();
		curMode = Mode.none;
	}

	@SuppressWarnings("unused")
	private void stopSaveTimer() {
		synchronized (timerSync) {
			if(timerActive){
				//System.out.println("stop timer");
				timer.cancel();
				timer = null;
				timerActive = false;
			}
		}
	}
	
	private String getBigText(){
		String text = "";
		if(Mode.dir == curMode){
			text = basicInfo.description.getText();
		}
		else if(Mode.link == curMode){
			text = linkInfo.description.getText();
		}
		else if(Mode.text == curMode){
			text = noteInfo.description.getText();
		}
		return text;
	}

	@SuppressWarnings("unused")
	private void doTimerSaveTask() {
		doTimerSaveTask(false);
	}
	
	//oneTime - неудачное решение - не сохраняет при переходе от ноды и потере фокуса
	private void doTimerSaveTask(boolean oneTime) {
		synchronized (timerSync) {
			if(timerActive){
				//System.out.println("start save task");
				if(oneTime) {
					if(timer != null){
						timer.cancel();
						timer = null;
					}
					timerActive = false;
				}
				if(Mode.none == curMode) return;
				
				String newBigText = getBigText();
				if(!currentBigText.equals(newBigText)){
					//System.out.println("!save text");
					//save
					HashMap<String, String> params = new HashMap<String, String>(2);
					if(Mode.dir == curMode){
						params.put(Params.description.toString(), newBigText);
					}
					else if(Mode.link == curMode){
						params.put(Params.description.toString(), newBigText);
					}
					else if(Mode.text == curMode){
						params.put(Params.text.toString(), newBigText);
					}
					currentBigText = newBigText;
					TreeOps.updateCurrentTreeNode(params);
				}
				//else System.out.println("text not modified");
			}
		}
	}

}
