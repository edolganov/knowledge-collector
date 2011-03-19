package shapshots;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;

import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;

import junit.framework.Assert;

public class JsonTest extends Assert {
	
	@Test
	public void beanToJsonAndBack(){
		ArrayList<Snapshot> snapshots = new ArrayList<Snapshot>();
		snapshots.add(new Snapshot("1","2","3"));
		snapshots.add(new Snapshot("4","5","6"));
		
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
		assertEquals("1 2 3", snapshotsCopy.get(0).listToString());
		assertEquals("4 5 6", snapshotsCopy.get(1).listToString());
	}

}
