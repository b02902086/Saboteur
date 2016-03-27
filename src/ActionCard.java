import java.util.*;
import java.lang.*;

public class ActionCard extends Card{
	int actionType;	// 0 for BlockCard, 1 for RepairCard, 2 for BombCard, 3 for MapCard
	public ActionCard(int action){
		super(1);
		this.actionType = action;
	}
}