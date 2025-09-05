package src;

public class Player {

    private boolean reachable=false;
    private final int totalWalls;
    private int wallsUsed;
    private int pos;
    private String pawnColor;
    private String playerName;
    private int playerOrder;
    private final String pawnDirection;

    public Player(int totalWalls, String pawnDirection, int pos){
        this.totalWalls=totalWalls;
        this.wallsUsed=0;
        this.pos=pos;
        this.pawnDirection=pawnDirection;
    }

    public void resetReachable(){ this.reachable=false; }

    public void setReachable(){ this.reachable=true; }

    public boolean getReachable(){ return reachable; }

    public int getWallsLeft(){
        return totalWalls-wallsUsed;
    }

    public void addWallsUsed(){
        this.wallsUsed++;
    }

    public void minusWallsUsed(){this.wallsUsed--;}

    public int getPlayerOrder(){return playerOrder;}

    public void setPlayerOrder(int i){this.playerOrder=i;}

    public void setPlayerName(String name){
        this.playerName=name;
    }

    public String getPlayerName(){
        return this.playerName;
    }
    public void setPawnColor(String pawnColor){
        this.pawnColor=pawnColor;
    }

    public String getPawnColor(){
        return pawnColor;
    }

    public String getPawnDirection(){
        return pawnDirection;
    }

    public int getPos(){
        return pos;
    }
    public void setPos(int pos){
        this.pos=pos;
    }
}
