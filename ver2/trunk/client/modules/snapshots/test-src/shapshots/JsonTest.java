package shapshots;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;

import junit.framework.Assert;

public class JsonTest extends Assert {
	
	@Test
	public void beanToJsonAndBack(){
		ArrayList<Snapshot> snapshots = new ArrayList<Snapshot>();
		snapshots.add(new Snapshot());
		snapshots.add(new Snapshot());
		
		SnapshotDir dir = new SnapshotDir();
		dir.setName("dir1");
		dir.setOpen(true);
		dir.setSnapshots(snapshots);
		
		String json = new Gson().toJson(dir);
		SnapshotDir copy = new Gson().fromJson(json, SnapshotDir.class);
		
		assertNotNull(copy);
		assertEquals("dir1", copy.getName());
		assertEquals(true, copy.isOpen());
		
		List<Snapshot> snapshotsCopy = copy.getSnapshots();
		assertNotNull(snapshotsCopy);
		assertEquals(2, snapshotsCopy.size());
	}
	
	@Test
	public void listToJsonAndBack(){
		SnapshotDir dir1 = new SnapshotDir();
		dir1.setName("dir1");
		SnapshotDir dir2 = new SnapshotDir();
		dir2.setName("dir2");
		
		ArrayList<SnapshotDir> list = new ArrayList<SnapshotDir>();
		list.add(dir1);
		list.add(dir2);
		String data = new Gson().toJson(list);
		
		Type listType = new TypeToken<List<SnapshotDir>>(){}.getType();
		List<SnapshotDir> copy = new Gson().fromJson(data, listType);
		assertNotNull(copy);
		assertEquals(2, copy.size());
		assertEquals("dir1", copy.get(0).getName());
		assertEquals("dir2", copy.get(1).getName());
		
	}

}
