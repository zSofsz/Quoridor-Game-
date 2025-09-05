package src;

import java.util.*;

public class Board {
    private final Map<Space,List<Space>> spaces= new HashMap<>();
    private final List<Wall> walls=new ArrayList<>();
    private int[] pawnsLocations=new int[4]; //{Blue,Red,Green,Yellow}
    private final List<Player> players=new ArrayList<>();
    private int turnOrder=1;

    public Board(){
        //makes all the spaces
        for(int i=1;i<=81;i++){
            addSpace(i);
        }
        //makes the vertical links
        for(int i=1;i<=72;i++){
            addLink(i, i+9);
        }
        //makes the horizontal links
        for(int i=1;i<=80;i++){
            if(i%9==0) continue;
            addLink(i, i+1);
        }
    }

    public int getTurnOrder(){
        return this.turnOrder;
    }
    public void setTurnOrder(int order){
        this.turnOrder=order;
    }
    //STATE MACHINE
    //1->BLUE / 2->RED / 3->GREEN / 4->YELLOW
    public void nextTurn(){
        if(players.size()==4){
            switch (turnOrder) {
                case 1 -> setTurnOrder(2);
                case 2 -> setTurnOrder(3);
                case 3 -> setTurnOrder(4);
                case 4 -> setTurnOrder(1);
            }
        }
        else{
            switch (turnOrder) {
                case 1 -> setTurnOrder(2);
                case 2 -> setTurnOrder(1);
            }
        }
    }

    public Player getCurrentPlayer(){
        if(turnOrder==1){
            for(Player p : players){
                if(p.getPlayerOrder()==1) return p;
            }
        }
        else if(turnOrder==2) {
            for(Player p : players){
                if(p.getPlayerOrder()==2) return p;
            }
        }
        else if(turnOrder==3){
            for(Player p : players){
                if(p.getPlayerOrder()==3) return p;
            }
        }
        else if(turnOrder==4){
            for(Player p : players){
                if(p.getPlayerOrder()==4) return p;
            }
        }

        return null;
    }
    public List<Player> getPlayers(){
        return players;
    }

    public List<Wall> getWalls(){
        return this.walls;
    }

    public void removeWall(boolean isVertical, int x, int y){
        walls.remove(new Wall(isVertical,x,y));

        int k=(x+1)+y*9;
        if(!isVertical){
            addLink(k,k+9);
            addLink(k+1,k+10);
        }
        else{
            addLink(k,k+1);
            addLink(k+9,k+10);
        }
    }

    public void addWall(boolean isVertical, int x, int y){
        walls.add(new Wall(isVertical,x,y));
    }

    public void setPawnsLocations(int[] pawnsLocation){
        this.pawnsLocations=pawnsLocation;
    }

    public void setOnePawnLocation(int x, String direction){
        switch (direction) {
            case "up" -> this.pawnsLocations[0] = x;
            case "down" -> this.pawnsLocations[1] = x;
            case "right" -> this.pawnsLocations[2] = x;
            case "left" -> this.pawnsLocations[3] = x;
        }
    }

    public int[] getPawnsLocations(){
        return pawnsLocations;
    }

    public void addSpace(int spaceNumber){
        spaces.putIfAbsent(new Space(spaceNumber), new ArrayList<>());
    }

    public void addLink(int spaceNumber1, int spaceNumber2){
        Space s1=new Space(spaceNumber1);
        Space s2=new Space(spaceNumber2);
        spaces.get(s1).add(s2);
        spaces.get(s2).add(s1);
    }

    public void removeLink(int spaceNumber1, int spaceNumber2){
        Space s1=new Space(spaceNumber1);
        Space s2=new Space(spaceNumber2);
        List<Space> es1=spaces.get(s1);
        List<Space> es2=spaces.get(s2);

        if(es1!=null) es1.remove(s2);
        if(es2!=null) es2.remove(s1);
    }

    public List<Space> getLinksOfSpace(int spaceNumber){
        return spaces.get(new Space(spaceNumber));
    }

    public boolean isReachable(int s, int d) {
        LinkedList<Integer> queue = new LinkedList<>();

        boolean[] visited = new boolean[82];
        visited[s]=true;
        queue.add(s);

        Space space;
        Iterator<Space> i;
        while (queue.size()!=0){
            s = queue.poll();
            i=getLinksOfSpace(s).listIterator();
            while(i.hasNext()){
                space=i.next();
                if(space.getSpaceNumber()==d) return true;
                if(!visited[space.getSpaceNumber()]){
                    visited[space.getSpaceNumber()]=true;
                    queue.add(space.getSpaceNumber());
                }
            }
        }
        return false;
    }
}
