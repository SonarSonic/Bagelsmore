package sonar.bagels.parts;

public interface IDeskDrawer extends IDeskPart {

	public boolean isDrawerOpen();
	
	public void setDrawerOpen(boolean isOpen);

	public DrawerPosition getDrawerPosition();

	public void setDrawerPosition(DrawerPosition position);

	public DrawerType getDrawerType();
}
