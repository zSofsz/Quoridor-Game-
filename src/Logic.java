package src;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Logic {
    
    public List<Space> possibleMoves(Player p, Board b){
        List<Space> spaces=b.getLinksOfSpace(p.getPos());
        List<Space> possibleSpaces=new ArrayList<>();
        List<Space> pawnsSpaces=new ArrayList<>();
        int[] pawnsLocations=b.getPawnsLocations();
        int pawns=0;

        for (Space space : spaces) {
            possibleSpaces.add(new Space(space.getSpaceNumber()));
        }

        //verify if there are any pawns in any of the possible spaces
        for(Space s : possibleSpaces){
            //System.out.println(s.getSpaceNumber());
            for(int i=0;i<4;i++){
                if(s.getSpaceNumber()==pawnsLocations[i]) pawns++;
            }
        }
        if(pawns==0) return possibleSpaces;

        //remove the pawns from the possible moves
        for (Space s : spaces) {
            for (int j = 0; j < 4; j++) {
                if (s.getSpaceNumber() == pawnsLocations[j]) {
                    pawnsSpaces.add(s);
                    possibleSpaces.remove(s);
                }
            }
        }

        //calculate possible moves
        outerloop:
        for(Space s : pawnsSpaces){
            List<Space> aux=b.getLinksOfSpace(s.getSpaceNumber());
            if(aux.size()-1==0) continue;
            
            if(s.getSpaceNumber()>p.getPos() && s.getSpaceNumber()-p.getPos()==1){ //right position
                //verify if pawn is on the edge
                if(s.getSpaceNumber()%9==0) continue;

                //verify if pawn can jump over the other pawn
                for (Space space : aux) {
                    if ((space.getSpaceNumber() - s.getSpaceNumber()) == 1) {
                        pawns=0;
                        for(int j=0;j<4;j++){
                            if(space.getSpaceNumber()==pawnsLocations[j]) pawns++;
                        }
                        if(pawns==0){
                            possibleSpaces.add(space);
                        }
                        continue outerloop;
                    }
                }

                //if it can't jump, verify if it can move diagonally
                for (Space space : aux) {
                    if ((space.getSpaceNumber()-s.getSpaceNumber()==9) || (space.getSpaceNumber()-s.getSpaceNumber()==-9)){
                        pawns=0;
                        for(int j=0;j<4;j++){
                            if(space.getSpaceNumber()==pawnsLocations[j]) pawns++;
                        }
                        if(pawns==0) possibleSpaces.add(space);
                    }
                }
            }
            else if(s.getSpaceNumber()>p.getPos()){ //down position
                if(s.getSpaceNumber()>72) continue;

                for (Space space : aux) {
                    if ((space.getSpaceNumber() - s.getSpaceNumber()) == 9) {
                        pawns=0;
                        for(int j=0;j<4;j++){
                            if(space.getSpaceNumber()==pawnsLocations[j]) pawns++;
                        }
                        if(pawns==0){
                            possibleSpaces.add(space);
                        }
                        continue outerloop;
                    }
                }
                for (Space space : aux) {
                    if ((space.getSpaceNumber()-s.getSpaceNumber()==1) || (space.getSpaceNumber()-s.getSpaceNumber()==-1)){
                        pawns=0;
                        for(int j=0;j<4;j++){
                            if(space.getSpaceNumber()==pawnsLocations[j]) pawns++;
                        }
                        if(pawns==0) possibleSpaces.add(space);
                    }
                }
            }
            else if(s.getSpaceNumber()<p.getPos() && p.getPos()-s.getSpaceNumber()==1){ //left position
                if(s.getSpaceNumber() ==1 || s.getSpaceNumber() ==10 || s.getSpaceNumber() ==19 || s.getSpaceNumber() ==28 ||
                   s.getSpaceNumber() ==37 || s.getSpaceNumber() ==46 || s.getSpaceNumber() ==55 || s.getSpaceNumber() ==64 ||
                   s.getSpaceNumber() ==73) continue;

                for (Space space : aux) {
                    if ((space.getSpaceNumber() - s.getSpaceNumber()) == -1) {
                        pawns=0;
                        for(int j=0;j<4;j++){
                            if(space.getSpaceNumber()==pawnsLocations[j]) pawns++;
                        }
                        if(pawns==0){
                            possibleSpaces.add(space);
                        }
                        continue outerloop;
                    }
                }
                for (Space space : aux) {
                    if ((space.getSpaceNumber()-s.getSpaceNumber()==9) || (space.getSpaceNumber()-s.getSpaceNumber()==-9)){
                        pawns=0;
                        for(int j=0;j<4;j++){
                            if(space.getSpaceNumber()==pawnsLocations[j]) pawns++;
                        }
                        if(pawns==0) possibleSpaces.add(space);
                    }
                }
            }
            else if(s.getSpaceNumber()<p.getPos()){ // up position
                if(s.getSpaceNumber()<10) continue;

                for (Space space : aux) {
                    if ((space.getSpaceNumber() - s.getSpaceNumber()) == -9) {
                        pawns=0;
                        for(int j=0;j<4;j++){
                            if(space.getSpaceNumber()==pawnsLocations[j]) pawns++;
                        }
                        if(pawns==0){
                            possibleSpaces.add(space);
                        }
                        continue outerloop;
                    }
                }
                for (Space space : aux) {
                    if ((space.getSpaceNumber()-s.getSpaceNumber()==1) || (space.getSpaceNumber()-s.getSpaceNumber()==-1)){
                        pawns=0;
                        for(int j=0;j<4;j++){
                            if(space.getSpaceNumber()==pawnsLocations[j]) pawns++;
                        }
                        if(pawns==0) possibleSpaces.add(space);
                    }
                }
            }
        }
        //possibleSpaces=removeDuplicates(possibleSpaces);

        return possibleSpaces;
    }

    public <T> List<T> removeDuplicates(List<T> list){
        List<T> newList = new ArrayList<>();
  
        for (T element : list) {
            if (!newList.contains(element)){
                newList.add(element);
            }
        }
        return newList;
    }

    public int putWall(Board b, Dimension dim, Point location, JLabel rect, boolean isVertical){
        int x=-1,y=-1;

        if(b.getCurrentPlayer().getWallsLeft()==0) return 0;

        if(!isVertical){
            if(dim.height<1000){
                for(int i=0;i<8;i++){
                    if(location.getX()-380<65*(i+1) && location.getX()-380>65*i) x=i;
                    if(location.getY()-130<65*(i+1) && location.getY()-130>65*i) y=i;
                }
                if(x==-1 || y==-1 || location.getY()<140 || location.getY()>630){
                    rect.setVisible(false);
                    return 0;
                }
                for(int i=0;i<7;i++){
                    if(location.getY()>165+66*i && location.getY()<205+66*i){
                        rect.setVisible(false);
                        return 0;
                    }
                }
            }
            else{
                for(int i=0;i<8;i++){
                    if(location.getX()-500<100*(i+1) && location.getX()-500>100*i) x=i;
                    if(location.getY()-150<100*(i+1) && location.getY()-150>100*i) y=i;
                }
                if(x==-1 || y==-1 || location.getY()<185 || location.getY()>905){
                    rect.setVisible(false);
                    return 0;
                }
                for(int i=0;i<7;i++){
                    if(location.getY()>220+98*i && location.getY()<275+98*i){
                        rect.setVisible(false);
                        return 0;
                    }
                }
            }
            for(Wall w : b.getWalls()){
                if(w.getX()==x && w.getY()==y){
                    rect.setVisible(false);
                    return 0;
                }
                if(!w.isWallVertical()){
                    if((w.getX()==x-1 && w.getY()==y) || (w.getX()==x+1 && w.getY()==y)){
                        rect.setVisible(false);
                        return 0;
                    }
                }
            }
            if(dim.height<1000){
                rect.setBounds(398+66*x,151+66*y,110,20);
            }
            else{
                rect.setBounds(550+98*x,200+98*y,150,30);
            }
            b.addWall(false,x,y);
            removeWallLink(b,x,y,false);
        }
        else{
            if(dim.height<1000){
                for(int i=0;i<8;i++){
                    if(location.getX()-420<65*(i+1) && location.getX()-420>65*i) x=i;
                    if(location.getY()-90<65*(i+1) && location.getY()-90>65*i) y=i;
                }
                if(x==-1 || y==-1 || location.getX()<430 || location.getX()>920){
                    rect.setVisible(false);
                    return 0;
                }
                for(int i=0;i<7;i++){
                    if(location.getX()>455+66*i && location.getX()<495+66*i){
                        rect.setVisible(false);
                        return 0;
                    }
                }
            }
            else{
                for(int i=0;i<8;i++){
                    if(location.getX()-550<100*(i+1) && location.getX()-550>100*i) x=i;
                    if(location.getY()-100<100*(i+1) && location.getY()-100>100*i) y=i;
                }
                if(x==-1 || y==-1 || location.getX()<595 || location.getX()>1315){
                    rect.setVisible(false);
                    return 0;
                }
                for(int i=0;i<7;i++){
                    if(location.getX()>630+98*i && location.getX()<685+98*i){
                        rect.setVisible(false);
                        return 0;
                    }
                }
            }
            for(Wall w : b.getWalls()){
                if(w.getX()==x && w.getY()==y){
                    rect.setVisible(false);
                    return 0;
                }
                if(w.isWallVertical()){
                    if((w.getX()==x && w.getY()==y-1) || (w.getX()==x && w.getY()==y+1)){
                        rect.setVisible(false);
                        return 0;
                    }
                }

            }
            if(dim.height<1000){
                rect.setBounds(442+66*x,106+66*y,20,110);
            }
            else{
                rect.setBounds(610+98*x,140+98*y,30,150);
            }

            b.addWall(true,x,y);
            removeWallLink(b,x,y,true);
        }
        b.getCurrentPlayer().addWallsUsed();

        for(Player p : b.getPlayers()){
            switch (p.getPawnDirection()){
                case "right":
                    for(int i=9;i<82;i+=9){
                        if(b.isReachable(p.getPos(),i)) p.setReachable();
                    }
                    break;
                case "left":
                    for(int i=1;i<74;i+=9){
                        if(b.isReachable(p.getPos(),i)) p.setReachable();
                    }
                    break;
                case "up":
                    for(int i=1;i<10;i++){
                        if(b.isReachable(p.getPos(),i)) p.setReachable();
                    }
                    break;
                case "down":
                    for(int i=73;i<82;i++){
                        if(b.isReachable(p.getPos(),i)) p.setReachable();
                    }
                    break;
            }
        }

        int reachable=0;
        for(Player p : b.getPlayers()){
            if(p.getReachable()){
                p.resetReachable();
                reachable++;
            }
        }

        if(reachable==b.getPlayers().size()) return 1;
        else{
            b.removeWall(isVertical,x,y);
            rect.setVisible(false);
            b.getCurrentPlayer().minusWallsUsed();
            return 0;
        }
    }

    public void removeWallLink(Board b, int x, int y, boolean isVertical){
        int k=(x+1)+y*9;
        //System.out.println(k+" "+x+" "+y);
        if(!isVertical){
            b.removeLink(k,k+9);
            b.removeLink(k+1,k+10);
        }
        else{
            b.removeLink(k,k+1);
            b.removeLink(k+9,k+10);
        }
    }

    public boolean isGameOver(Board board){
        Player p=board.getCurrentPlayer();
        
        switch(p.getPawnDirection()){
            case "up":
                if(p.getPos()<10) return true;
                break;
            case "down":
                if(p.getPos()>72) return true;
                break;
            case "right":
                if(p.getPos()%9==0) return true;
                break;
            case "left":
                if(p.getPos() ==1 || p.getPos() ==10 || p.getPos() ==19 || p.getPos() ==28 || p.getPos() ==37 ||
                   p.getPos() ==46 || p.getPos() ==55 || p.getPos() ==64 || p.getPos() ==73) return true;
                break;
        }
        
       return false; 
    }
}
