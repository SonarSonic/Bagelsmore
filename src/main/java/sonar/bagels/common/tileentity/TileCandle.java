package sonar.bagels.common.tileentity;

import sonar.core.integration.multipart.TileSonarMultipart;
import sonar.core.network.sync.SyncTagType;

public class TileCandle extends TileSonarMultipart {
	
	public SyncTagType.BOOLEAN isActive = new SyncTagType.BOOLEAN(0);
	
	{
		syncList.addPart(isActive);
	}

	public boolean isBurning() {
		return isActive.getObject();
	}
}
