import java.util.*;
import java.lang.*;
import java.util.Random;


public class Card implements java.io.Serializable{
	int type;	//0 for PathCard, 1 for ActionCard
	public Card(int type){
		this.type = type;
	}
}