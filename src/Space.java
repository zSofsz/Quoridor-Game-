package src;

public class Space {
    private final int spaceNumber;
    //private boolean hasPawn;

    public Space(int spaceNumber){
        this.spaceNumber=spaceNumber;
        //this.hasPawn=false;
    }

    public int getSpaceNumber(){
        return spaceNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // are the references equal
        if (o == null) return false; // is the other object null
        if (getClass() != o.getClass()) return false; // both objects the same class
        Space s= (Space) o;
        return spaceNumber==s.getSpaceNumber();
    }

    @Override
    public int hashCode() {
        return spaceNumber;
    }

}
