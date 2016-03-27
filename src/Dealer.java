import java.util.*;
import java.lang.*;
import java.util.*;

public class Dealer{
	ArrayList<Card> deck = new ArrayList<Card>();
	public Dealer(){
		setDeck();
	}
	public void setDeck(){
		//path cards
		deck.add(new PathCard(10,false,true,false,false,false,false));
		deck.add(new PathCard(11,true,true,true,true,false,false));
		deck.add(new PathCard(12,false,true,true,true,false,false));
		deck.add(new PathCard(13,false,true,true,false,false,false));
		deck.add(new PathCard(14,true,true,false,false,false,false));
		deck.add(new PathCard(15,true,true,true,false,false,false));
		deck.add(new PathCard(16,false,false,true,false,false,false));
		deck.add(new PathCard(17,false,true,false,true,false,false));
		deck.add(new PathCard(18,false,false,true,true,false,false));
		for(int i = 0;i < 3;i++){
			deck.add(new PathCard(3,false,false,true,true,true,false));
		}
		for(int i = 0;i < 4;i++){
			deck.add(new PathCard(4,false,true,false,true,true,false));
			deck.add(new PathCard(5,true,true,false,false,true,false));
		}
		for(int i = 0;i < 5;i++){
			deck.add(new PathCard(6,false,true,true,false,true,false));
			deck.add(new PathCard(7,true,true,true,true,true,false));
			deck.add(new PathCard(8,true,true,false,true,true,false));
			deck.add(new PathCard(9,false,true,true,true,true,false));
		}
		//action cards
		deck.add(new RepairCard(3));
		deck.add(new RepairCard(5));
		deck.add(new RepairCard(6));
		for(int i = 0;i < 2;i++){
			deck.add(new RepairCard(1));
			deck.add(new RepairCard(2));
			deck.add(new RepairCard(4));
		}
		for(int i = 0;i < 3;i++){
			deck.add(new BlockCard(1));
			deck.add(new BlockCard(2));
			deck.add(new BlockCard(4));
		}
		//bomb cards
		for(int i = 0;i < 3;i++){
			deck.add(new BombCard());
		}
		//map cards
		for(int i = 0;i < 6;i++){
			deck.add(new MapCard());
		}
	}
	public Card NextCard(){
		Random ran = new Random();
		if(!deck.isEmpty())
			return deck.remove(ran.nextInt(deck.size()));
		else{
			return (new Card(-1));
		}		
	}
}

