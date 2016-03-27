import java.util.*;
import java.lang.*;

public class PathCard extends Card{
    int id;                             // 0= null card
	boolean up,down,left,right;	        //whether the path is open for up, down, left, and right
    int direction;                      //down = 0 up = 1;
    boolean pass;
    boolean goal;
	public PathCard(int id, boolean up, boolean down, boolean left, boolean right, boolean pass, boolean goal){
		super(0);
        this.id = id;
        this.direction = 0;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.pass = pass;
        this.goal = goal;
	}
}
