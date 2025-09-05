package src;

public class Wall {

    private final boolean isVertical;
    private final int x;
    private final int y;

    public Wall(boolean isVertical, int x, int y){
        this.isVertical=isVertical;
        this.x=x;
        this.y=y;
    }

    public boolean isWallVertical(){
        return isVertical;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // are the references equal
        if (o == null) return false; // is the other object null
        if (getClass() != o.getClass()) return false; // both objects the same class
        Wall w= (Wall) o;
        return isVertical==w.isWallVertical() && x==w.getX() && y==getY();
    }

    @Override
    public int hashCode() {
        return x & y;
    }
}
