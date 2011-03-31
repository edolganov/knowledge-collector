package ru.kc.module.snapshots.tools;

import ru.kc.common.controller.Controller;
import ru.kc.common.tree.TreeService;
import ru.kc.common.tree.event.GetTreeServiceRequest;
import ru.kc.model.Node;
import ru.kc.module.snapshots.command.CreateTreeNodes;
import ru.kc.module.snapshots.command.OpenSnapshot;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.TreeNode;
import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.common.event.AppClosing;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.tools.filepersist.update.SetProperty;

import com.google.gson.Gson;

@Mapping(SnapshotsPanel.class)
public class CurrentStateController extends Controller<SnapshotsPanel> {

	
	private static final String CUR_SNAPSHOT_KEY = "cur-snapshot";

	@Override
	protected void init() {}
	
	@Override
	protected void afterAllInited() {
		Node owner = getOwner();
		if(owner == null){
			return;
		}
		
		Snapshot lastState = loadSnapshot(owner);
		if(lastState != null){
			invokeSafe(new OpenSnapshot(lastState));
		}
		
	}
	
	private Snapshot loadSnapshot(Node owner) {
		String data = owner.getProperty(CUR_SNAPSHOT_KEY);
		if(data == null){
			return null;
		}
		
		Snapshot out = new Gson().fromJson(data, Snapshot.class);
		return out;
	}
	
	

	@EventListener
	public void onClosing(AppClosing event){
		saveCurTreeToSnapshot();
	}

	private void saveCurTreeToSnapshot() {
		Node owner = getOwner();
		if(owner == null){
			return;
		}
		
		TreeNode root = invokeSafe(new CreateTreeNodes()).result;
		if(root == null){
			return;
		}
		
		Snapshot snapshot = new Snapshot();
		snapshot.setRoot(root);
		
		saveSnapshot(owner, snapshot);
	}
	
	private Node getOwner(){
		TreeService service = invokeSafe(new GetTreeServiceRequest()).result;
		if(service != null){
			Node node = service.getRoot().getUserObject(Node.class);
			return node;
		}
		return null;
	}

	private void saveSnapshot(Node owner, Snapshot snapshot) {
		try {
			String data = new Gson().toJson(snapshot);
			updater.update(owner, new SetProperty(CUR_SNAPSHOT_KEY, data));
		} catch (Exception e) {
			log.error("", e);
		}
	}
	

	

}
