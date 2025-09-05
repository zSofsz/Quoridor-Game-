package Tests;

import src.Board;
import src.Logic;
import src.Player;
import src.Space;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.util.Objects;

class LogicTest {

    private final Logic logic=new Logic();

    @Test
    void possibleMoves() {
        Player p=new Player(10,"right",14);
        Board board=new Board();

        List<Space> spaces=logic.possibleMoves(p,board);
        for(Space s : spaces){
            if(s.getSpaceNumber()!=13 && s.getSpaceNumber()!=15 && s.getSpaceNumber()!=5 && s.getSpaceNumber()!=23) Assertions.fail();
        }

        board.removeLink(14,15);
        board.removeLink(14,5);

        spaces=logic.possibleMoves(p,board);
        for(Space s : spaces){
            if(s.getSpaceNumber()==5 || s.getSpaceNumber()==15) Assertions.fail();
        }

    }

    @Test
    void removeDuplicates() {
        List<Space> spaces=new ArrayList<>();
        List<Space> newSpaces;
        List<Space> expected=new ArrayList<>();

        spaces.add(new Space(1));
        spaces.add(new Space(1));
        spaces.add(new Space(2));
        spaces.add(new Space(2));
        spaces.add(new Space(3));

        expected.add(new Space(1));
        expected.add(new Space(2));
        expected.add(new Space(3));

        newSpaces=logic.removeDuplicates(spaces);

        Assertions.assertEquals(expected,newSpaces);
    }

    @Test
    void putWall() {
        JFrame frame=new JFrame();
        Dimension dim=new Dimension(1920,1080);
        frame.setSize(dim.width,dim.height);
        Board board=new Board();

        //POSITION 0,1 HOR
        JLabel rect=new JLabel();
        rect.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/rectHor.png"))));
        rect.setBounds(0, 0, 150, 30);
        frame.getContentPane().add(rect);

        logic.putWall(board,dim,new Point(555,300),rect,false);

        Assertions.assertEquals(550,rect.getX());
        Assertions.assertEquals(298,rect.getY());
        Assertions.assertTrue(rect.isVisible());

        //POSITION 0,1 (SAME) HOR
        JLabel rect2=new JLabel();
        rect2.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/rectHor.png"))));
        rect2.setBounds(0, 0, 150, 30);
        frame.getContentPane().add(rect2);

        logic.putWall(board,dim,new Point(555,300),rect2,false);

        Assertions.assertFalse(rect2.isVisible());

        //POSITION 0,2 HOR
        JLabel rect3=new JLabel();
        rect3.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/rectHor.png"))));
        rect3.setBounds(0, 0, 150, 30);
        frame.getContentPane().add(rect3);

        logic.putWall(board,dim,new Point(555,372),rect3,false);

        Assertions.assertFalse(rect3.isVisible());

        //POSITION 5,3 VERT
        JLabel rect4=new JLabel();
        rect4.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/rectHor.png"))));
        rect4.setBounds(0, 0, 30, 150);
        frame.getContentPane().add(rect4);

        logic.putWall(board,dim,new Point(1120,478),rect4,true);

        Assertions.assertEquals(1100,rect4.getX());
        Assertions.assertEquals(434,rect4.getY());
        Assertions.assertTrue(rect4.isVisible());

        //POSITION 5,3 (SAME) VERT
        JLabel rect5=new JLabel();
        rect5.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/rectHor.png"))));
        rect5.setBounds(0, 0, 30, 150);
        frame.getContentPane().add(rect5);

        logic.putWall(board,dim,new Point(1120,478),rect5,true);

        Assertions.assertFalse(rect5.isVisible());

        //POSITION 5,2 VERT
        JLabel rect6=new JLabel();
        rect6.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/rectHor.png"))));
        rect6.setBounds(0, 0, 30, 150);
        frame.getContentPane().add(rect6);

        logic.putWall(board,dim,new Point(1120,372),rect6,true);

        Assertions.assertFalse(rect6.isVisible());
    }

    @Test
    void removeWallLink() {
        Board board=new Board();

        logic.removeWallLink(board,1,5,false);
        List<Space> spaces=board.getLinksOfSpace(47);
        Assertions.assertEquals(3,spaces.size());
        spaces=board.getLinksOfSpace(48);
        Assertions.assertEquals(3,spaces.size());

        logic.removeWallLink(board,3,4,true);
        spaces=board.getLinksOfSpace(40);
        Assertions.assertEquals(3,spaces.size());
        spaces=board.getLinksOfSpace(49);
        Assertions.assertEquals(3,spaces.size());

    }
}