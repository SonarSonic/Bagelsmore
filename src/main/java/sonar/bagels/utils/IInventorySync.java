package sonar.bagels.utils;
//this was just easier
public interface IInventorySync {
	int getField(int id);

	void setField(int id, int value);

	int getFieldCount();
}
