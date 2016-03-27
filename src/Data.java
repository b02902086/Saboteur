import java.util.*;
import java.lang.*;
import java.io.*;

public class Data implements java.io.Serializable {
    int event;
    int curPlayer;
    int playerTo;
    int id;
    int tool;
    int status;
    int reverse;
    int x,y;	//map index
    Card putCard = new Card(-1);
    int cardIndex;
    Card drawCard = new Card(-1);
    int[] role = new int[5];
    int cardNum;
    ArrayList<Card> hand = new ArrayList<Card>();

     
    public Data(){
    	this.event = -1;
    	this.curPlayer = -1;
    	this.playerTo = -1;
    	this.status = -1;
    	this.x = -1;
    	this.y = -1;
        this.reverse = 0;
    	this.cardIndex = -1;
        this.tool = -1;
    }
}
