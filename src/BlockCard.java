import java.util.*;
import java.lang.*;

public class BlockCard extends ActionCard{
	int tool;	//for binary operation
	public BlockCard(int tool){
		super(0);
		this.tool = tool;
	}

}