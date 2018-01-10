package sonar.bagels.api;

public interface IDeskDrawer {

	boolean isDrawerOpen();
	
	void setDrawerOpen(boolean isOpen);

	DrawerType getDrawerType();

	void onNeighbouringDrawerChanged();

}
