import java.awt.Container;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.lang.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.JTextArea;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Player extends JFrame implements ActionListener{
	ImageIcon icon = new ImageIcon();
    ImageIcon[] iccc = new ImageIcon[10];
    JButton role_button = new JButton();
	Image img;
    JTextArea text = new JTextArea();
	JButton[] button = new JButton[60];
    JButton[] player_button = new JButton[5];
    JButton[] status_button = new JButton[30];
	int last_press;
    JButton pass = new JButton();
	Dealer dealer = new Dealer();
    ObjectInputStream in;
    ObjectOutputStream out;

    String[] player_name = {"player1", "player2", "player3", "player4", "player5"};
	//bomb, default, map, player_background, setup_back, start
	/*String[] block_name = {"block1", "block2", "block4"};
	String[] goal_name = {"goal1", "goal2", "goal3"};
	String[] path_origin = {"path1", "path2", "path3", "path4", "path5", "path6","path7",
		"path8", "path9", "path10", "path11", "path12", "path13", "path14", "path15", "path16"};
	String[] path_reverse = {"path1_2", "path2_2", "path3_2", "path4_2", "path5_2", "path6_2","path7_2",
		"path8_2", "path9_2", "path10_2", "path11_2", "path12_2", "path13_2", "path14_2", "path15_2", "path16_2"};
	String[] repair_name = {"repair1", "repair2", "repair3", "repair4", "repair5", "repair6"};*/

	ArrayList<Card> hand = new ArrayList<Card>();
    int status;  //true = alive false = locked
    int gold;
    int id = 0;
    int role;   //0 = miner 1 = Saboteur;
    String name;

	public Player() {
    	setContentPane(new JLabel(new ImageIcon(getClass().getResource("/img/background.png"))));
		setSize(1280, 700);
        setLayout(null);
        status = 0;
        gold = 0;

        last_press = -1;

        /*for testing*/
        
        /*for testing*/

        //text field
        text.setBounds(1000,60,220,74);
        text.setText("Wait for other players...");
        text.setOpaque(false);
        add(text);
        //pass button
        pass.setBounds(995,605,250,45);
        icon = new ImageIcon(getClass().getResource("/img/pass.png"));
        pass.setIcon(new ImageIcon(icon.getImage().getScaledInstance(250,45,java.awt.Image.SCALE_SMOOTH)));
        pass.setOpaque(false);
        pass.setBorder(null);
        pass.setContentAreaFilled(false);
        pass.addActionListener(this);
        pass.setActionCommand(Integer.toString(99));
        add(pass);

        //
        role_button.setBounds(1185,175,60,60);
        role_button.setOpaque(false);
        role_button.setBorder(null);
        role_button.setContentAreaFilled(false);
        add(role_button);
        //player field
        iccc[0] = new ImageIcon(getClass().getResource("/img/lamp.png"));
        iccc[1] = new ImageIcon(getClass().getResource("/img/hammer.png"));
        iccc[2] = new ImageIcon(getClass().getResource("/img/cart.png"));
        iccc[3] = new ImageIcon(getClass().getResource("/img/lamp_block.png"));
        iccc[4] = new ImageIcon(getClass().getResource("/img/hammer_block.png"));
        iccc[5] = new ImageIcon(getClass().getResource("/img/cart_block.png"));
        iccc[6] = new ImageIcon(getClass().getResource("/img/block1.png"));
        iccc[7] = new ImageIcon(getClass().getResource("/img/block2.png"));
        iccc[8] = new ImageIcon(getClass().getResource("/img/block4.png"));
        iccc[9] = new ImageIcon(getClass().getResource("/img/default.png"));
		int bound = 10;
		for (int i = 0; i < 5; i++) {
            icon = new ImageIcon(getClass().getResource("/img/" + player_name[i] + ".png"));
			player_button[i] = new JButton(new ImageIcon(icon.getImage().getScaledInstance(120, 40,  java.awt.Image.SCALE_SMOOTH)));
			player_button[i].setBounds(0, bound, 120, 40);
            player_button[i].addActionListener(this);
            player_button[i].setActionCommand(Integer.toString(i + 60));
            player_button[i].setOpaque(false);
            player_button[i].setBorder(null);
            player_button[i].setContentAreaFilled(false);
            add(player_button[i]);
            for (int k = 0 + i * 3; k < 3 + i * 3; k++) {
                status_button[k] = new JButton(new ImageIcon(iccc[k - i * 3].getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH)));
                status_button[k].setBounds(2 + (k - i * 3) * 38, bound + 40, 35, 35);
                status_button[k].setOpaque(false);
                status_button[k].setBorder(null);
                status_button[k].setContentAreaFilled(false);
                add(status_button[k]);
            }
			bound += 85;
		}

		//map field
		icon = new ImageIcon(getClass().getResource("/img/default.png"));
		int x, y;
		for (int j = 0; j < 5; j++) {
			x = 160;
			y = 30 + j * 120;
			for (int i = j * 10; i < 10 + j * 10; i++) {
				button[i] = new JButton(new ImageIcon(icon.getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));
				button[i].setBounds(x, y, 80, 120);
				button[i].addActionListener(this);
				button[i].setActionCommand(Integer.toString(i));
				// System.out.println("action: " + Integer.toString(i) + " is set");
				x += 80;
			}
		}

		icon = new ImageIcon(getClass().getResource("/img/start.png"));
		button[21].setIcon(new ImageIcon(icon.getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));
		icon = new ImageIcon(getClass().getResource("/img/setup_back.png"));
		button[9].setIcon(new ImageIcon(icon.getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));
		button[29].setIcon(new ImageIcon(icon.getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));
		button[49].setIcon(new ImageIcon(icon.getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));

		// field
        icon = new ImageIcon(getClass().getResource("/img/player_background.png"));
		button[50] = new JButton(new ImageIcon(icon.getImage().getScaledInstance(180, 60,  java.awt.Image.SCALE_SMOOTH)));
        button[50].setOpaque(false);
        button[50].setBorder(null);
        button[50].setContentAreaFilled(false);
		button[50].setBounds(990, 175, 180, 60);

		//block card field
		icon = new ImageIcon(getClass().getResource("/img/default.png"));		
		for(int i = 0; i < 3; i++){
			button[i + 51] = new JButton(new ImageIcon(icon.getImage().getScaledInstance(60, 90,  java.awt.Image.SCALE_SMOOTH)));
			button[i + 51].setBounds((1020 + i * 70), 250, 60, 90);
		}

		//player's card field
		for(int i = 0; i < 6; i++){
            icon = new ImageIcon(getClass().getResource("/img/default.png"));
			button[i + 54] = new JButton(new ImageIcon(icon.getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));
			button[i + 54].setBounds((995 + (i % 3) * 85), (350 + 125 * (i / 3)), 80, 120);
			button[i + 54].addActionListener(this);
			button[i + 54].setActionCommand(Integer.toString(i + 54));

		}
        //for(int i = 0;i < 6;i++){
            //button[i + 54].setVisible(false);
        //}

		for (int i = 0; i < 60; i++) {
			button[i].setOpaque(false);
			button[i].setBorder(null);
			button[i].setContentAreaFilled(false);
			add(button[i]);
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
	}
	public static void main(String[] args) throws Exception {
		//JFrame frame = new Player();
        Player tmp = new Player();
        tmp.play();

	}
    public void actionPerformed(ActionEvent ae) {
        try{
        	int actionNum = Integer.parseInt(ae.getActionCommand());
        	//press hand
        	if((actionNum < 60) && (actionNum >= 54)){
        		if(actionNum == last_press){
        			// ImageIcon tt = new ImageIcon(getClass().getResource(findCardPNG(dealer.NextCard())));
        			// button[actionNum].setIcon(new ImageIcon(tt.getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));
                    if(hand.get(actionNum-54).type == 0){
                        PathCard tmp = (PathCard)hand.get(actionNum-54);
                        if(tmp.direction == 0)
                            tmp.direction = 1;
                        else if (tmp.direction == 1)
                            tmp.direction = 0;
                  
                        hand.set(actionNum-54,tmp);
                        tmp = (PathCard)hand.get(actionNum-54);
                        //System.out.println(tmp.direction);
                    }

                    ImageIcon tt = new ImageIcon(getClass().getResource(findCardPNG(hand.get(actionNum-54))));
                    button[actionNum].setIcon(new ImageIcon(tt.getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));
        			System.out.println("reverse card " + (actionNum - 54));
        		}
        		else{
        			last_press = actionNum;
        			System.out.println("choose " + (actionNum - 54));
        		}
        	}
        	//press map
        	else if((actionNum <= 49) && (actionNum >= 0)){
        		if((last_press < 60) && (last_press >= 54)){
                    Data message = new Data();
                    message.event = 2;
                    message.x = (actionNum/10);
                    message.y = (actionNum%10);
                    message.cardIndex = last_press-54;
                    message.putCard = hand.get(last_press-54);
                    if(message.putCard.type ==0){
                        PathCard tmp = (PathCard)message.putCard;
                        message.reverse = tmp.direction;
                    }
                    out.writeObject(message);
                    out.flush();
        			System.out.println("send index(" + (actionNum / 10) + "," + (actionNum % 10) + ") and card" + (last_press-54) +" to server" );
                    // icon = new ImageIcon(getClass().getResource(findCardPNG(hand.get(last_press-54))));
                    // button[actionNum].setIcon(new ImageIcon(icon.getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));
                    // hand.set(last_press-54,dealer.NextCard());
                    // icon = new ImageIcon(getClass().getResource(findCardPNG(hand.get(last_press-54))));
                    // button[last_press].setIcon(new ImageIcon(icon.getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));

        			// last_press = -1;
        		}
        		else
        			System.out.println("press (" + (actionNum / 10) + "," + (actionNum % 10) + "), do nothing");
        	}
            //press player
            else if((actionNum < 70) && (actionNum >= 60)) {
                if((last_press < 60) && (last_press >= 54)){
                    Data message = new Data();
                    message.event = 3;
                    message.playerTo = actionNum - 60;
                    message.cardIndex = last_press-54;
                    message.putCard = hand.get(last_press-54);
                    out.writeObject(message);
                    out.flush();

                    System.out.println("send block/repair query " + (actionNum - 60));
                }
                System.out.println("press player" + (actionNum - 60));
            }
            else if (actionNum == 99){
                Data message = new Data();
                message.event = 7;
                out.writeObject(message);
                out.flush();
                System.out.println("choose pass");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public String findCardPNG(Card card){
    	if(card.type == 0){
    		//path card
    		PathCard tmp = (PathCard)card; 
    		if(tmp.id == 3)
    			if(tmp.direction == 0)
    				return "/img/path11.png";
    			else if(tmp.direction == 1)
    				return "/img/path11_2.png";
    			else
    				return "/img/fault.png";
    		else if(tmp.id == 4)
    			if(tmp.direction == 0)
    				return "/img/path13.png";
    			else if(tmp.direction == 1)
    				return "/img/path13_2.png";
    			else
    				return "/img/fault.png";
    		else if(tmp.id == 5)
    			if(tmp.direction == 0)
    				return "/img/path15.png";
    			else if(tmp.direction == 1)
    				return "/img/path15_2.png";
    			else
    				return "/img/fault.png";
    		else if(tmp.id == 6)
    			if(tmp.direction == 0)
    				return "/img/path14.png";
    			else if(tmp.direction == 1)
    				return "/img/path14_2.png";
    			else
    				return "/img/fault.png";
    		else if(tmp.id == 7)
    			if(tmp.direction == 0)
    				return "/img/path10.png";
    			else if(tmp.direction == 1)
    				return "/img/path10_2.png";
    			else
    				return "/img/fault.png";
    		else if(tmp.id == 8)
    			if(tmp.direction == 0)
    				return "/img/path16.png";
    			else if(tmp.direction == 1)
    				return "/img/path16_2.png";
    			else
    				return "/img/fault.png";
    		else if(tmp.id == 9)
    			if(tmp.direction == 0)
    				return "/img/path12.png";
    			else if(tmp.direction == 1)
    				return "/img/path12_2.png";
    			else
    				return "/img/fault.png";
    		else if(tmp.id == 10)
    			if(tmp.direction == 0)
    				return "/img/path5.png";
    			else if(tmp.direction == 1)
    				return "/img/path5_2.png";
    			else
    				return "/img/fault.png";
    		else if(tmp.id == 11)
    			if(tmp.direction == 0)
    				return "/img/path7.png";
    			else if(tmp.direction == 1)
    				return "/img/path7_2.png";
    			else
    				return "/img/fault.png";
    		else if(tmp.id == 12)
    			if(tmp.direction == 0)
    				return "/img/path9.png";
    			else if(tmp.direction == 1)
    				return "/img/path9_2.png";
    			else
    				return "/img/fault.png";
    		else if(tmp.id == 13)
    			if(tmp.direction == 0)
    				return "/img/path2.png";
    			else if(tmp.direction == 1)
    				return "/img/path2_2.png";
    			else
    				return "/img/fault.png";
    		else if(tmp.id == 14)
    			if(tmp.direction == 0)
    				return "/img/path3.png";
    			else if(tmp.direction == 1)
    				return "/img/path3_2.png";
    			else
    				return "/img/fault.png";
    		else if(tmp.id == 15)
    			if(tmp.direction == 0)
    				return "/img/path6.png";
    			else if(tmp.direction == 1)
    				return "/img/path6_2.png";
    			else
    				return "/img/fault.png";
    		else if(tmp.id == 16)
    			if(tmp.direction == 0)
    				return "/img/path4.png";
    			else if(tmp.direction == 1)
    				return "/img/path4_2.png";
    			else
    				return "/img/fault.png";
    		else if(tmp.id == 17)
    			if(tmp.direction == 0)
    				return "/img/path1.png";
    			else if(tmp.direction == 1)
    				return "/img/path1_2.png";
    			else
    				return "/img/fault.png";
    		else if(tmp.id == 18)
    			if(tmp.direction == 0)
    				return "/img/path8.png";
    			else if(tmp.direction == 1)
    				return "/img/path8_2.png";
    			else
    				return "/img/fault.png";
    		else
    			return "/img/fault.png";

    	}
    	else if (card.type == 1){
    		//action card
    		ActionCard t = (ActionCard)card;
    		if(t.actionType == 0){
    			//BlockCard
    			BlockCard tmp = (BlockCard)card;
    			if(tmp.tool == 1)
    				return "/img/block1.png";
    			else if(tmp.tool == 2)
    				return "/img/block2.png";
    			else if(tmp.tool == 4)
    				return "/img/block4.png";
    			else
    				return "/img/fault.png";
    		}
    		else if(t.actionType == 1){
    			//RepairCard
    			RepairCard tmp = (RepairCard)card;
    			if(tmp.tool == 1)
    				return "/img/repair1.png";
    			else if(tmp.tool == 2)
    				return "/img/repair2.png";
    			else if(tmp.tool == 3)
    				return "/img/repair3.png";
    			else if(tmp.tool == 4)
    				return "/img/repair4.png";
    			else if(tmp.tool == 5)
    				return "/img/repair5.png";
    			else if(tmp.tool == 6)
    				return "/img/repair6.png";
    			else
    				return "/img/fault.png";
    		}
    		else if(t.actionType == 2){
    			//BombCard
    			return "/img/bomb.png";
    		}
    		else if(t.actionType == 3){
    			//MapCard
    			return "/img/map.png";
    		}
    		else
    			return "/img/fault.png";
    	}
    	else{
    		return "/img/fault.png";
    	}
    }
    // public String findStatusPNG(int status){

    // }
    public void play() throws Exception{
        Socket socket = new Socket("127.0.0.1", 4000);
        int playerID = -1;
        int cardNum = 6;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        int final_flag = 0;
        Data message;

        try {
            message = (Data)in.readObject();
            System.out.println(message.hand.size());
            playerID = message.id;

            for(int i = 0;i < 6;i++){
                Card tmp = new Card(-1);
                tmp = message.hand.get(i);
                hand.add(tmp);
            }
            if(message.role[playerID] == 0){
                ImageIcon ro = new ImageIcon(getClass().getResource("/img/saboteur.png"));
                role_button.setIcon(new ImageIcon(ro.getImage().getScaledInstance(60,60,java.awt.Image.SCALE_SMOOTH)));
            }
            else if(message.role[playerID] == 1){
                ImageIcon ro = new ImageIcon(getClass().getResource("/img/miner.png"));
                role_button.setIcon(new ImageIcon(ro.getImage().getScaledInstance(60,60,java.awt.Image.SCALE_SMOOTH)));
            }
            role_button.setOpaque(false);
            role_button.setBorder(null);
            role_button.setContentAreaFilled(false);

            System.out.println(hand.size());

            for(int i = 0; i < 6; i++){
                ImageIcon ic = new ImageIcon(getClass().getResource(findCardPNG(hand.get(i))));
                button[i + 54].setIcon(new ImageIcon(ic.getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));
                button[i + 54].setOpaque(false);
                button[i + 54].setBorder(null);
                button[i + 54].setContentAreaFilled(false);

            }
            System.out.println("my ID = " + message.id);
            ImageIcon ii = new ImageIcon(getClass().getResource("/img/" + player_name[playerID] + ".png"));
            this.button[50].setIcon(new ImageIcon(ii.getImage().getScaledInstance(180, 60,  java.awt.Image.SCALE_SMOOTH)));
            button[50].setOpaque(false);
            button[50].setBorder(null);
            button[50].setContentAreaFilled(false);
            out.writeObject(message);
            out.flush();
        
            // JFrame frame = new Player();
            while(true){
                message = (Data)in.readObject();
                switch(message.event){
                    case 0:
                        text.setText("Action denied");
                        last_press = -1;
                        message.cardNum = cardNum;
                        out.writeObject(message);
                        out.flush();
                        break;
                    case 1:
                        System.out.println("receive event 1");
                        // text.setText("player " + (message.curPlayer+1) + "'s turn");
                        if(message.curPlayer == playerID){
                            System.out.println("curPlayer = playerID");
                            for(int i = 0;i < 6;i++){
                                this.button[i+54].setVisible(true);
                            }
                        } else {
                            for(int i = 0;i < 6;i++){
                                this.button[i+54].setVisible(false);
                            }
                        }
                        break;
                    case 2:
                        System.out.println("receive event 2");
                        ImageIcon icon;
                        if(message.curPlayer == playerID){
                            text.setText("Yout put a card on map(" + message.x + "," + message.y + ")");
                        }
                        else{
                            text.setText("player " + (message.curPlayer+1) + " put a card on map(" + message.x + "," + message.y + ")");
                        }
                        PathCard temp =  (PathCard)message.putCard;
                        temp.direction = message.reverse;
                        icon = new ImageIcon(getClass().getResource(findCardPNG(temp)));
                        button[message.x*10 + message.y].setIcon(new ImageIcon(icon.getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));
                        if(message.curPlayer == playerID){
                            System.out.println("message.curPlayer = " + message.curPlayer);
                            hand.set(message.cardIndex,message.drawCard);
                            icon = new ImageIcon(getClass().getResource(findCardPNG(hand.get(last_press-54))));
                            this.button[last_press].setIcon(new ImageIcon(icon.getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));
                            if(hand.get(message.cardIndex).type == -1){
                                cardNum--;
                            }
                            last_press = -1;
                        }
                        message.cardNum = cardNum;
                        out.writeObject(message);
                        out.flush();
                        break;
                    case 3:
                        System.out.println("receive event 3");
                        if(message.playerTo == playerID){
                            if(message.tool == 1){
                                text.setText("Your lamp is blocked" +"\n" + " by player " + (message.curPlayer+1));
                            }
                            else if(message.tool == 2){
                                text.setText("Your hammer is blocked" +"\n" + " by player " + (message.curPlayer+1));
                            }
                            else if(message.tool == 4){
                                text.setText("Your cart is blocked" +"\n" + " by player " + (message.curPlayer+1));
                            }
                        }
                        else{
                            if(message.tool == 1){
                                text.setText("player " + (message.playerTo+1) + "s' lamp is blocked" +"\n" + " by player " + (message.curPlayer+1));
                            }
                            else if(message.tool == 2){
                                text.setText("player " + (message.playerTo+1) + "s' hammer is blocked" +"\n" + " by player " + (message.curPlayer+1));
                            }
                            else if(message.tool == 4){
                                text.setText("player " + (message.playerTo+1) + "s' cart is blocked" +"\n" + " by player " + (message.curPlayer+1));
                            }
                        }
                        if(message.curPlayer == playerID){
                            System.out.println("message.curPlayer = " + message.curPlayer);
                            hand.set(message.cardIndex,message.drawCard);
                            icon = new ImageIcon(getClass().getResource(findCardPNG(hand.get(last_press-54))));
                            this.button[last_press].setIcon(new ImageIcon(icon.getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));
                            if(hand.get(message.cardIndex).type == -1){
                                cardNum--;
                            }
                            last_press = -1;
                        }
                        if((message.status & 1) > 0){
                            status_button[message.playerTo*3 + 0].setIcon(new ImageIcon(iccc[3].getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH)));
                            if(message.playerTo == playerID){
                                button[51].setIcon(new ImageIcon(iccc[6].getImage().getScaledInstance(60, 90,  java.awt.Image.SCALE_SMOOTH)));
                            }
                        }
                        else if((message.status & 1) == 0){
                            status_button[message.playerTo*3 + 0].setIcon(new ImageIcon(iccc[0].getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH)));
                            if(message.playerTo == playerID){
                                button[51].setIcon(new ImageIcon(iccc[9].getImage().getScaledInstance(60, 90,  java.awt.Image.SCALE_SMOOTH)));
                            }
                        }
                        if((message.status & 2) > 0){
                            status_button[message.playerTo*3 + 1].setIcon(new ImageIcon(iccc[4].getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH)));
                            if(message.playerTo == playerID){
                                button[52].setIcon(new ImageIcon(iccc[7].getImage().getScaledInstance(60, 90,  java.awt.Image.SCALE_SMOOTH)));
                            }
                        }
                        else if((message.status & 2) == 0){
                            status_button[message.playerTo*3 + 1].setIcon(new ImageIcon(iccc[1].getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH)));
                            if(message.playerTo == playerID){
                                button[52].setIcon(new ImageIcon(iccc[9].getImage().getScaledInstance(60, 90,  java.awt.Image.SCALE_SMOOTH)));
                            }
                        }
                        if((message.status & 4) > 0){
                            status_button[message.playerTo*3 + 2].setIcon(new ImageIcon(iccc[5].getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH)));
                            if(message.playerTo == playerID){
                                button[53].setIcon(new ImageIcon(iccc[8].getImage().getScaledInstance(60, 90,  java.awt.Image.SCALE_SMOOTH)));
                            }
                        }
                        else if((message.status & 4) == 0){
                            status_button[message.playerTo*3 + 2].setIcon(new ImageIcon(iccc[2].getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH)));
                            if(message.playerTo == playerID){
                                button[53].setIcon(new ImageIcon(iccc[9].getImage().getScaledInstance(60, 90,  java.awt.Image.SCALE_SMOOTH)));
                            }
                        }
                        for(int i = 51;i < 54;i++){
                            button[i].setOpaque(false);
                            button[i].setBorder(null);
                            button[i].setContentAreaFilled(false);
                        }
                        message.cardNum = cardNum;
                        out.writeObject(message);
                        out.flush();
                        break;
                    case 4:
                        System.out.println("receive event 4");
                        if(message.playerTo == playerID){
                            if(message.tool == 1){
                                text.setText("Your lamp is repaired" +"\n" + " by player " + (message.curPlayer+1));
                            }
                            else if(message.tool == 2){
                                text.setText("Your hammer is repaired" +"\n" + " by player " + (message.curPlayer+1));
                            }
                            else if(message.tool == 4){
                                text.setText("Your cart is repaired" +"\n" + " by player " + (message.curPlayer+1));
                            }
                        }
                        else{
                            if(message.tool == 1){
                                text.setText("player " + (message.playerTo+1) + "'s' lamp is repaired" +"\n" + " by player " + (message.curPlayer+1));
                            }
                            else if(message.tool == 2){
                                text.setText("player " + (message.playerTo+1) + "'s' hammer is repaired" +"\n" + " by player " + (message.curPlayer+1));
                            }
                            else if(message.tool == 4){
                                text.setText("player " + (message.playerTo+1) + "'s' cart is repaired" +"\n" + " by player " + (message.curPlayer+1));
                            }
                        }
                        if(message.curPlayer == playerID){
                            System.out.println("message.curPlayer = " + (message.curPlayer+1));
                            hand.set(message.cardIndex,message.drawCard);
                            icon = new ImageIcon(getClass().getResource(findCardPNG(hand.get(last_press-54))));
                            this.button[last_press].setIcon(new ImageIcon(icon.getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));
                            if(hand.get(message.cardIndex).type == -1){
                                cardNum--;
                            }
                            last_press = -1;
                        }
                        if((message.status & 1) > 0){
                            status_button[message.playerTo*3 + 0].setIcon(new ImageIcon(iccc[3].getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH)));
                            if(message.playerTo == playerID){
                                button[51].setIcon(new ImageIcon(iccc[6].getImage().getScaledInstance(60, 90,  java.awt.Image.SCALE_SMOOTH)));
                            }
                        }
                        else if((message.status & 1) == 0){
                            status_button[message.playerTo*3 + 0].setIcon(new ImageIcon(iccc[0].getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH)));
                            if(message.playerTo == playerID){
                                button[51].setIcon(new ImageIcon(iccc[9].getImage().getScaledInstance(60, 90,  java.awt.Image.SCALE_SMOOTH)));
                            }
                        }
                        if((message.status & 2) > 0){
                            status_button[message.playerTo*3 + 1].setIcon(new ImageIcon(iccc[4].getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH)));
                            if(message.playerTo == playerID){
                                button[52].setIcon(new ImageIcon(iccc[7].getImage().getScaledInstance(60, 90,  java.awt.Image.SCALE_SMOOTH)));
                            }
                        }
                        else if((message.status & 2) == 0){
                            status_button[message.playerTo*3 + 1].setIcon(new ImageIcon(iccc[1].getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH)));
                            if(message.playerTo == playerID){
                                button[52].setIcon(new ImageIcon(iccc[9].getImage().getScaledInstance(60, 90,  java.awt.Image.SCALE_SMOOTH)));
                            }
                        }
                        if((message.status & 4) > 0){
                            status_button[message.playerTo*3 + 2].setIcon(new ImageIcon(iccc[5].getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH)));
                            if(message.playerTo == playerID){
                                button[53].setIcon(new ImageIcon(iccc[8].getImage().getScaledInstance(60, 90,  java.awt.Image.SCALE_SMOOTH)));
                            }
                        }
                        else if((message.status & 4) == 0){
                            status_button[message.playerTo*3 + 2].setIcon(new ImageIcon(iccc[2].getImage().getScaledInstance(35, 35,  java.awt.Image.SCALE_SMOOTH)));
                            if(message.playerTo == playerID){
                                button[53].setIcon(new ImageIcon(iccc[9].getImage().getScaledInstance(60, 90,  java.awt.Image.SCALE_SMOOTH)));
                            }
                        }
                        for(int i = 51;i < 54;i++){
                            button[i].setOpaque(false);
                            button[i].setBorder(null);
                            button[i].setContentAreaFilled(false);
                        }
                        message.cardNum = cardNum;
                        out.writeObject(message);
                        out.flush();
                        break;
                    case 5:
                        if(message.curPlayer == playerID){
                            text.setText("You bomb map(" + message.x + "," + message.y + ")");
                        }
                        else{
                            text.setText("player " + (message.curPlayer+1) + " bomb map(" + message.x + "," + message.y + ")");
                        }
                        System.out.println("receive event 5");
                        button[message.x*10 + message.y].setIcon(new ImageIcon(iccc[9].getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));
                        button[message.x*10 + message.y].setOpaque(false);
                        button[message.x*10 + message.y].setBorder(null);
                        button[message.x*10 + message.y].setContentAreaFilled(false);
                        if(message.curPlayer == playerID){
                            System.out.println("message.curPlayer = " + (message.curPlayer+1));
                            hand.set(message.cardIndex,message.drawCard);
                            icon = new ImageIcon(getClass().getResource(findCardPNG(hand.get(last_press-54))));
                            this.button[last_press].setIcon(new ImageIcon(icon.getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));
                            if(hand.get(message.cardIndex).type == -1){
                                cardNum--;
                            }
                            last_press = -1;
                        }
                        message.cardNum = cardNum;
                        out.writeObject(message);
                        out.flush();
                        break;
                    case 6:
                        System.out.println("receive event 6");
                        if(message.curPlayer == playerID){
                            if(message.id == 0){
                                System.out.println("id = 0");
                                text.setText("map(" + message.x + "," + message.y + ") is not gold");
                            }
                            else if(message.id == 1){
                                System.out.println("id = 1");
                                text.setText("map(" + message.x + "," + message.y + ") is gold");

                            }
                            System.out.println("message.curPlayer = " + message.curPlayer);
                            hand.set(message.cardIndex,message.drawCard);
                            icon = new ImageIcon(getClass().getResource(findCardPNG(hand.get(last_press-54))));
                            this.button[last_press].setIcon(new ImageIcon(icon.getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));
                            if(hand.get(message.cardIndex).type == -1){
                                cardNum--;
                            }
                            last_press = -1;
                        }
                        else{
                            text.setText("player " + (message.curPlayer+1) + " looked map(" + message.x + "," + message.y + ")");
                        }
                        message.cardNum = cardNum;
                        out.writeObject(message);
                        out.flush();
                        break;
                    case 7:
                        if(message.curPlayer == playerID){
                            text.setText("You pass this round");
                        }
                        else{
                            text.setText("player " + (message.curPlayer+1) + " pass this round");
                        }
                        System.out.println("receive event 7");
                        message.cardNum = cardNum;
                        out.writeObject(message);
                        out.flush();
                        break;
                    case 8:
                        if(message.id == 0){
                            text.setText("map(" + message.x + "," + message.y + ") is stone");
                        }
                        else if (message.id == 1){
                            ImageIcon ico = new ImageIcon(getClass().getResource("/img/goal1.png"));
                            button[message.x*10 + message.y].setIcon(new ImageIcon(ico.getImage().getScaledInstance(80, 120,  java.awt.Image.SCALE_SMOOTH)));
                        }
                        message.cardNum = cardNum;
                        out.writeObject(message);
                        out.flush();
                        break;
                    case 9:
                        final_flag = 1;
                        String a = "player ";
                        if(message.id == 0){
                            for(int i = 0; i < 5;i++){
                                if(message.role[i] == 0){
                                    a = a + Integer.toString(i+1) + " ";
                                }
                            }
                            a  = a + "win";
                        }
                        else if(message.id == 1){
                            for(int i = 0; i < 5;i++){
                                if(message.role[i] == 1){
                                    a = a + Integer.toString(i+1) + " ";
                                }
                            }
                            a  = a + "win";
                        }
                        text.setText(a);
                        break;
                }
                if(final_flag == 1)
                    break;
            } 
        } catch (IOException e) {
            e.printStackTrace();
        }      
    }
}
