import java.lang.*;
import java.util.*;

public class Map{
    ArrayList<ArrayList<PathCard>> Dock = new ArrayList<ArrayList<PathCard>> ();
    boolean visited[][] = new boolean [5][10];
    //Card Direction
    private boolean R(PathCard p){
        if(p.direction==0)
            return p.right;
        else
            return p.left;
        
    }
    private boolean L(PathCard p){
        if(p.direction==0)
            return p.left;
        else
            return p.right;
    }
    private boolean U(PathCard p){
        if(p.direction==0)
            return p.up;
        else
            return p.down;
    }
    private boolean D(PathCard p){
        if(p.direction==0)
            return p.down;
        else
            return p.up;
    }
    //DFS
    private boolean DFS(int x, int y){
        //terminate status
        if(x==2 && y==1)
            return true;
        boolean tmp1=false, tmp2=false, tmp3=false, tmp4=false;
        visited[x][y] = true;
        boolean tmpUp, tmpDown, tmpLeft, tmpRight;

        //recursive
        if(!Dock.get(x).get(y).pass)
            return false;
        if(x!=0 && U(Dock.get(x).get(y)) && !visited[x-1][y] && D(Dock.get(x-1).get(y)))
            tmp1=DFS(x-1, y);
        if(x!=4 && D(Dock.get(x).get(y)) && !visited[x+1][y] && U(Dock.get(x+1).get(y)))
            tmp2=DFS(x+1, y);
        if(y!=0 && L(Dock.get(x).get(y)) && !visited[x][y-1] && R(Dock.get(x).get(y-1)))
            tmp3=DFS(x, y-1);
        if(y!=9 && R(Dock.get(x).get(y)) && !visited[x][y+1] && L(Dock.get(x).get(y+1)))
            tmp4=DFS(x, y+1);
        return tmp1 || tmp2 || tmp3 || tmp4;
    }
    private void InsertCard(int i, int j, PathCard c){
        ArrayList<PathCard> t = (ArrayList<PathCard>)Dock.get(i).clone();
        t.set(j, c);
        Dock.set(i,t);
    }
    // init
    public Map(){
        ArrayList<PathCard> tmp = new ArrayList<PathCard>();
        Random ran = new Random();
        for(int i=0; i<10; i++)
            tmp.add(new PathCard(0 ,false, false, false, false, false, false));
                                 //up , down , left , right, pass , goal
        for(int i=0; i<5; i++){
            Dock.add(tmp);
        }
        InsertCard(0,9,new PathCard(2, true, true, true, true, true, false));      //add goal
        InsertCard(2,9,new PathCard(2, true, true, true, true, true, false));      //add goal
        InsertCard(4,9,new PathCard(2, true, true, true, true, true, false));      //add goal
        //add start
        InsertCard(2,1,new PathCard(1,true, true, true, true, true, false));
        //add goal(gold)
        InsertCard((ran.nextInt(3)*2),9, new PathCard(2, true, true, true, true, true, true));
    }
    // Remove
    public boolean Remove(int x, int y){
        if(Dock.get(x).get(y).id==0 || Dock.get(x).get(y).id == 2 || Dock.get(x).get(y).id == 1)
            return false;
        InsertCard(x,y, new PathCard(0, false, false, false, false, false, false));
        return true;
    }
    // Look
    public boolean Look(int x, int y){
        return Dock.get(x).get(y).goal;
    }
    //Traverse: wheather this pos can go back to the start or not
    public boolean Traverse(int x, int y){
        for(int i=0; i<5; i++)
            for(int j=0; j<10; j++)
                visited[i][j]=false;
        PathCard tmp = Dock.get(x).get(y);
        InsertCard(x,y,new PathCard(0, true, true, true, true, true, false));
        boolean ans = DFS(x,y); 
        InsertCard(x,y,tmp);
        return ans;
    }
    //Match: 
    public boolean Match(int x, int y, PathCard t){
        boolean tmp1 = false, tmp2 = false, tmp3 = false, tmp4 = false;
        if(Dock.get(x).get(y).id !=0)
            return false;
        if(x!=0 && U(t) && ((Dock.get(x-1).get(y).id!=0 && D(Dock.get(x-1).get(y))) || Dock.get(x-1).get(y).id == 0 || Dock.get(x-1).get(y).id==2))
            tmp1 = true;
        else if(x!=0 && !U(t) && ((Dock.get(x-1).get(y).id!=0 && !D(Dock.get(x-1).get(y))) || Dock.get(x-1).get(y).id == 0 || Dock.get(x-1).get(y).id==2)){
            tmp1 = true;
            System.out.println("fuck");
            }
        else if(x==0)
            tmp1 = true;
        if(x!=4 && D(t) && ((Dock.get(x+1).get(y).id!=0 && U(Dock.get(x+1).get(y))) || Dock.get(x+1).get(y).id == 0 || Dock.get(x+1).get(y).id==2))
            tmp2 = true;
        else if(x!=4 && !D(t) && ((Dock.get(x+1).get(y).id!=0 && !U(Dock.get(x+1).get(y))) || Dock.get(x+1).get(y).id == 0 || Dock.get(x+1).get(y).id==2))
            tmp2 = true;
        else if(x==4)
            tmp2 = true;
        if(y!=0 && L(t) && ((Dock.get(x).get(y-1).id!=0 && R(Dock.get(x).get(y-1))) || Dock.get(x).get(y-1).id == 0 || Dock.get(x).get(y-1).id==2))
            tmp3 = true;
        else if(y!=0 && !L(t) && ((Dock.get(x).get(y-1).id!=0 && !R(Dock.get(x).get(y-1))) || Dock.get(x).get(y-1).id == 0 || Dock.get(x).get(y-1).id==2))
            tmp3 = true;
        else if(y==0)
            tmp3 = true;
        if(y!=9 && R(t) && ((Dock.get(x).get(y+1).id!=0 && L(Dock.get(x).get(y+1))) || Dock.get(x).get(y+1).id == 0 || Dock.get(x).get(y+1).id==2))
            tmp4 = true;
        else if(y!=9 && !R(t) && ((Dock.get(x).get(y+1).id!=0 && !L(Dock.get(x).get(y+1))) || Dock.get(x).get(y+1).id == 0 || Dock.get(x).get(y+1).id==2))
            tmp4 = true;
        else if (y==9)
            tmp4 = true;
        return tmp1 && tmp2 && tmp3 && tmp4;
    }
    //Put
    public boolean Put(int x, int y, PathCard p){
        System.out.print(p.id);
        System.out.print(p.up);
        System.out.print(p.down);
        System.out.print(p.left);
        System.out.print(p.right);
        System.out.print(p.pass);
        System.out.print(p.direction);
        System.out.print("\n");

        if(Traverse(x,y) && Match(x,y,p)){
            InsertCard(x,y,p);
            return true;
        }   
        else
            return false;
    }
    //Show
    public void Show(){
        for(int i=0; i <5; i++){
            for(int j=0; j<10; j++){
                //System.out.print("("+i+","+j+")");
                System.out.print(Dock.get(i).get(j).id + " " );
            }
            System.out.print("\n");
        }

    }
       
}
