import java.util.*;
import java.lang.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class Server {
    static int player_num = 5;
    static int turn = 0;
    static Data message = new Data();
    static int[] hand_size = new int[player_num];
    static int sum = 0, win = 0;
    static int[] player_status = new int[player_num];
    static int gold_x, x1, x2, gold_open = 0, x1_open = 0, x2_open = 0;
    static int[] role = new int[player_num];
    static int[] rrrrran = {1, 1, 1, 1, 0, 0};

    static Socket[] socket = new Socket[player_num];
    static ObjectOutputStream[] out = new ObjectOutputStream[player_num];
    static ObjectInputStream[] in = new ObjectInputStream[player_num];
    static Map map = new Map();
    static Dealer dealer = new Dealer();

    public static void printMessage() {
        System.out.println("event = " + message.event + ", curPlayer = " + message.curPlayer
           + ", playerTo = " + message.playerTo + ", id = " + message.id + ", status = " + message.status
           + ", (x, y) = (" + message.x + ", " + message.y + "), cardIndex = " + message.cardIndex);
        return;
    }

    public static int printPathCard() {
        if (message.putCard.type != 0) return 0;
        PathCard tmp = (PathCard)message.putCard;
        System.out.println("PathCard id = " + tmp.id);
        return 1;
    }

    public static void main(String[] arg) throws Exception{
        ServerSocket listener = new ServerSocket(4000, 0, InetAddress.getByName(null));
        int next = 0;
        
        for (int i = 0; i < 6; i++) {
            Random ran = new Random();
            int index = ran.nextInt(6);
            int tmp_i = rrrrran[i];
            rrrrran[i] = rrrrran[index];
            rrrrran[index] = tmp_i;
        }

        for (int i = 0; i < player_num; i++) {
            role[i] = rrrrran[i];
            message.role[i] = rrrrran[i];
            System.out.println("role = " + message.role[i]);
            hand_size[i] = 6;
            sum += hand_size[i];
            player_status[i] = 0;
        }

        if (map.Look(0, 9)) {
            gold_x = 0;
            x1 = 2;
            x2 = 4;
        } else if (map.Look(2, 9)) {
            gold_x = 2;
            x1 = 0;
            x2 = 4;
        } else if (map.Look(4, 9)) {
            gold_x = 4;
            x1 = 0;
            x2 = 2;
        }
        System.out.println("gold_x = " + gold_x + ", gold_y = " + 9);

        try {
            for (int i = 0; i < player_num; i++)
                socket[i] = listener.accept();

            for (int i = 0; i < player_num; i++) {
                message.hand.clear();
                for (int j = 0; j < 6; j++)
                    message.hand.add(dealer.NextCard());
                System.out.println(message.hand.size());
                message.id = i;
                out[i] = new ObjectOutputStream(socket[i].getOutputStream());
                out[i].flush();
                in[i] = new ObjectInputStream(socket[i].getInputStream());
                out[i].writeObject(message);
                message = (Data)in[i].readObject();
                System.out.println(message.id);
            }

            while (sum > 0) {
                message.event = 1;
                message.curPlayer = turn;
                for (int i = 0; i < player_num; i++) {
                    out[i].writeObject(message);
                    out[i].flush();
                    System.out.println("Send message 1.");
                }

                next = 1;
                message = (Data)in[turn].readObject();
                message.curPlayer = turn;                   //Player.class can't return curPlayer
                printMessage();
                System.out.println(printPathCard());

                if (message.event == 2) {
                    if (message.putCard.type == 0) {
                        if (player_status[message.curPlayer] == 0) {
                            PathCard tmp = (PathCard)message.putCard;
                            tmp.direction = message.reverse;
                            message.putCard = tmp;
                            System.out.println("Server Card: "+ tmp.direction);
                            if(map.Put(message.x, message.y, tmp)) {
                                message.drawCard = dealer.NextCard();
                                System.out.println("drawCard.type = " + message.drawCard.type);
                                for (int i = 0; i < player_num; i++) {
                                    out[i].writeObject(message);
                                    out[i].flush();
                                    System.out.println("Send message 2.");
                                }
                                next = 1;
                            } else {
                                next = 0;
                            }
                        } else {
                            next = 0;
                        }
                    } else if (message.putCard.type == 1) {
                        ActionCard a_tmp = (ActionCard)message.putCard;
                        if (a_tmp.actionType == 2) {
                            //bomb
                            if(map.Remove(message.x, message.y)) {
                                message.event = 5;
                                message.drawCard = dealer.NextCard();
                                System.out.println("drawCard.type = " + message.drawCard.type);
                                for (int i = 0; i < player_num; i++) {
                                    out[i].writeObject(message);
                                    out[i].flush();
                                    System.out.println("Send message 5.");
                                }
                                next = 1;
                            } else {
                                next = 0;
                            }
                        } else if (a_tmp.actionType == 3) {
                            //map
                            if(map.Look(message.x, message.y)) {
                                message.id = 1;
                            } else {
                                message.id = 0;
                            }
                            message.event = 6;    
                            message.drawCard = dealer.NextCard();
                            System.out.println("drawCard.type = " + message.drawCard.type);
                            for (int i = 0; i < player_num; i++) {
                                out[i].writeObject(message);
                                out[i].flush();
                                System.out.println("Send message 6.");
                            }
                            next = 1;
                        } else {
                            next = 0;
                        }
                    } else {
                        next = 0;
                    }
                }
                else if (message.event == 3) {
                    if (message.putCard.type == 1) {
                        //System.out.println("aaa");
                        ActionCard a_tmp = (ActionCard)message.putCard;
                        if (a_tmp.actionType == 0) {
                            //System.out.println("bbb");
                            BlockCard b_tmp = (BlockCard)a_tmp;
                            if ((player_status[message.playerTo] & b_tmp.tool) == 0) {
                                //System.out.println("ccc");
                                player_status[message.playerTo] |= b_tmp.tool;
                                System.out.println("status = " + player_status[message.playerTo]);
                                message.status = player_status[message.playerTo];
                                message.tool = b_tmp.tool;
                                message.drawCard = dealer.NextCard();
                                System.out.println("drawCard.type = " + message.drawCard.type);
                                for (int i = 0; i < player_num; i++) {
                                    out[i].writeObject(message);
                                    out[i].flush();
                                    System.out.println("Send message 3.");
                                }
                                next = 1;
                            }
                            else {
                                next = 0;
                            }
                        } else if (a_tmp.actionType == 1) {
                            int ffffflag = 1;
                            RepairCard r_tmp = (RepairCard)a_tmp;                            
                            if ((player_status[message.playerTo] & r_tmp.tool) > 0) {
                                if (r_tmp.tool == 3) {
                                    if ((player_status[message.playerTo] & 1) > 0) {
                                        player_status[message.playerTo] &= 6;
                                        message.tool = 1;
                                    } else if  ((player_status[message.playerTo] & 2) > 0) {
                                        player_status[message.playerTo] &= 5;
                                        message.tool = 2;
                                    } 
                                } else if (r_tmp.tool == 5) {
                                    if ((player_status[message.playerTo] & 1) > 0) {
                                        player_status[message.playerTo] &= 6;
                                        message.tool = 1;
                                    } else if  ((player_status[message.playerTo] & 4) > 0) {
                                        player_status[message.playerTo] &= 3;
                                        message.tool = 4;
                                    }
                                } else if (r_tmp.tool == 6) {
                                    if ((player_status[message.playerTo] & 2) > 0) {
                                        player_status[message.playerTo] &= 5;
                                        message.tool = 2;
                                    } else if  ((player_status[message.playerTo] & 4) > 0) {
                                        player_status[message.playerTo] &= 3;
                                        message.tool = 4;
                                    }
                                } else {
                                    player_status[message.playerTo] &= ~(r_tmp.tool);
                                    message.tool = r_tmp.tool;
                                }
                            }
                            else {
                                ffffflag = 0;
                            }
                            if(ffffflag == 1) {
                                System.out.println("status = " + player_status[message.playerTo]);
                                message.event = 4;
                                message.status = player_status[message.playerTo];
                                message.drawCard = dealer.NextCard();
                                System.out.println("drawCard.type = " + message.drawCard.type);
                                for (int i = 0; i < player_num; i++) {
                                    out[i].writeObject(message);
                                    out[i].flush();
                                    System.out.println("Send message 4.");
                                }
                                next = 1;
                            } else {
                                next = 0;
                            }
                        }
                    } else {
                        next = 0;
                    }
                } else if (message.event == 7) {
                    for (int i = 0; i < player_num; i++) {
                        out[i].writeObject(message);
                        out[i].flush();
                        System.out.println("Send message 7.");
                    }
                    next = 1;
                }           
                
                if (next == 0) {
                    for (int i = 0; i < player_num; i++) {
                        message.event = 0;
                        out[i].writeObject(message);
                        out[i].flush();
                        System.out.println("Send message 0.");
                    }
                } else {
                    turn = (turn + 1) % player_num;
                }

                sum = 0;
                for (int i = 0; i < player_num; i++) {
                    message = (Data)in[i].readObject();
                    System.out.println("size of " + i + " = " + message.cardNum);
                    hand_size[i] = message.cardNum;
                    sum += message.cardNum;
                }

                message.event = 8;
                message.y = 9;
                if(map.Traverse(gold_x, 9)) {
                    message.x = gold_x;
                    message.id = 1;
                    if (gold_open == 0) {
                        for (int i = 0; i < player_num; i++) {
                            out[i].writeObject(message);
                            out[i].flush();
                            System.out.println("Send message 8.");
                        }
                        for (int i = 0; i < player_num; i++) {
                            message = (Data)in[i].readObject();
                            System.out.println("1size of " + i + " = " + message.cardNum);
                        }
                    }
                    gold_open = 1;
                    win = 1;
                    break;
                } else if (map.Traverse(x1, 9)) {
                    message.x = x1;
                    message.id = 0;
                    if (x1_open == 0) {
                        for (int i = 0; i < player_num; i++) {
                            out[i].writeObject(message);
                            out[i].flush();
                            System.out.println("Send message 8.");
                        }
                        for (int i = 0; i < player_num; i++) {
                            message = (Data)in[i].readObject();
                            System.out.println("2size of " + i + " = " + message.cardNum);
                        }
                    }
                    x1_open = 1;
                } else if (map.Traverse(x2, 9)) {
                    message.x = x2;
                    message.id = 0;
                    if (x2_open == 0) {
                        for (int i = 0; i < player_num; i++) {
                            out[i].writeObject(message);
                            out[i].flush();
                            System.out.println("Send message 8.");
                        }
                        for (int i = 0; i < player_num; i++) {
                            message = (Data)in[i].readObject();
                            System.out.println("3size of " + i + " = " + message.cardNum);
                        }
                    }
                    x2_open = 1;
                }
            }   //while(sum > 0)


            for (int i = 0; i < player_num; i++) {
                message.role[i] = role[i];
            }
            message.event = 9;
            if (win == 1)
                message.id = 1;
            else
                message.id = 0;
            for (int i = 0; i < player_num; i++) {
                out[i].writeObject(message);
                out[i].flush();
                System.out.println("Send message 9.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < player_num; i++)
            System.out.println(socket[i]);
    }
}
