package src;

import javax.swing.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Point;
import java.awt.event.*;
import java.util.*;

public class Game{

    private boolean isPawnClicked;
    private final Logic l=new Logic();
    private Point offset;
    private final MouseMotionListener m=new MouseMotionListener(){
                public void mouseMoved(MouseEvent e){}
                public void mouseDragged(MouseEvent e){
                    if(!isPawnClicked){
                        int x = e.getPoint().x - offset.x;
                        int y = e.getPoint().y - offset.y;
                        Component component = e.getComponent();
                        Point location = component.getLocation();
                        location.x += x;
                        location.y += y;
                        component.setLocation(location);
                    }
                }
    };

    public Game(){
        JFrame frame = new JFrame("Quoridor");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        //Dimension dim=new Dimension(1351,780);
        if(dim.width!=1366 && dim.height!=768) dim.setSize(1351,780);

        //System.out.println(dim.width+" "+dim.height);
        frame.setSize(dim.width, dim.height);
        frame.getContentPane().setBackground(Color.decode("#AF4933")); //Color.red.darker().darker().darker()
        //frame.setSize(1920,1080);
        frame.setLayout(null);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //fecha o programa ao clicar no X

        menu(frame,dim);
        frame.setVisible(true);
    }

    public void menu(JFrame frame, Dimension dim){
        ImageIcon icon=new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/icon.png")));
        frame.setIconImage(icon.getImage());
        
        JLabel label=new JLabel();
        label.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/icon.png"))));
        label.setBounds((dim.width-312)/2, 20, 312, 312);
        frame.getContentPane().add(label);

        JButton button1 = new JButton("Start Game");
        button1.setBounds((dim.width-312)/2+50, 352, 212, 50);
        frame.getContentPane().add(button1);
        button1.addActionListener(e -> startGame(frame,dim));

        JButton button2 = new JButton("Game Rules");
        button2.setBounds((dim.width-312)/2+50, 430, 212, 50);
        frame.getContentPane().add(button2);
        button2.addActionListener(e -> gameRules(frame,dim));

        JButton button3 = new JButton("Quit");
        button3.setBounds((dim.width-312)/2+50, 508, 212, 50);
        frame.getContentPane().add(button3);
        button3.addActionListener(e -> System.exit(0));
    }

    public void cleanFrame(JFrame frame){       
        frame.getContentPane().removeAll();
        frame.getContentPane().repaint();        
        frame.getContentPane().revalidate();
    }

    public void addBackArrow(JFrame frame, Dimension dim){
        JLabel arrow=new JLabel();
        arrow.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/arrow.png"))));
        arrow.setBounds(10, 10, 50, 50); 

        arrow.addMouseListener(new MouseListener(){
            public void mousePressed(MouseEvent e){}
            public void mouseReleased(MouseEvent e){}

            public void mouseExited(MouseEvent e){
                frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                frame.repaint();
            }
            public void mouseEntered(MouseEvent e){
                frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
                frame.repaint();

            }
            public void mouseClicked(MouseEvent e) {
                cleanFrame(frame);
                frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                menu(frame,dim);
            }
        });
        frame.getContentPane().add(arrow);
    }

    public void addMoves(Player p, Board board, JLabel b, JLabel walls, JLabel playerColor, JLabel name, JFrame frame, Dimension dim){
        List<Space> moves=l.possibleMoves(p, board);
        
        for(Space s : moves){
            //System.out.println(s.getSpaceNumber());
            int[] coords;
            coords=calculatePawnCoords(s.getSpaceNumber());

            JLabel mov=new JLabel();
            if(dim.height<1000){
                mov.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/square.png"))));
                mov.setBounds(24+coords[0]*66, 25+coords[1]*66, 35, 35);
            }
            else{
                mov.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/square.png"))));
                mov.setBounds(35+coords[0]*98, 37+coords[1]*98, 55, 55);
            }

            mov.addMouseListener(new MouseListener(){
                public void mousePressed(MouseEvent e){}
                public void mouseReleased(MouseEvent e){}
    
                public void mouseExited(MouseEvent e){
                    frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    frame.repaint();
                }
                public void mouseEntered(MouseEvent e){
                    frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    frame.repaint();
    
                }
                public void mouseClicked(MouseEvent e) {
                    movePawn(p, board, coords[0], coords[1]);
                    if(l.isGameOver(board)) gameOver(frame,board,dim);

                    board.nextTurn();
                    walls.setText(String.valueOf(board.getCurrentPlayer().getWallsLeft()));
                    name.setText(board.getCurrentPlayer().getPlayerName());
                    switch (board.getCurrentPlayer().getPawnColor()) {
                        case "blue" ->
                                playerColor.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/BluePawnStart.png"))));
                        case "red" ->
                                playerColor.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/RedPawnStart.png"))));
                        case "green" ->
                                playerColor.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/GreenPawnStart.png"))));
                        case "yellow" ->
                                playerColor.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/YellowPawnStart.png"))));
                    }
                    isPawnClicked=false;
                    frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    b.removeAll();
                    printAllPawns(b, walls, playerColor,name, frame, board, dim);
                    frame.repaint();
                }
            });

            b.add(mov);
        }
    }

    public void movePawn(Player p, Board board, int x, int y){
        int k=x+1+y*9;
        p.setPos(k);
        board.setOnePawnLocation(k, p.getPawnDirection());
    }

    public void printAllPawns(JLabel b, JLabel walls, JLabel playerColor, JLabel name, JFrame frame, Board board, Dimension dim){
        for(int i=0;i<board.getPlayers().size();i++){
            printPawn(board.getPlayers().get(i), b, walls, playerColor, name, frame, board, dim);
        }
    }

    public void printPawn(Player p, JLabel b, JLabel walls, JLabel playerColor, JLabel name, JFrame frame, Board board, Dimension dim){
        int[] coords=calculatePawnCoords(p.getPos());
        int x=coords[0],y=coords[1];
        //System.out.println(x+" "+y);

        switch (p.getPawnColor()) {
            case "blue" -> {
                JLabel bluePawn = new JLabel();
                if(dim.height<1000){
                    bluePawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/BluePawn.png"))));
                    bluePawn.setBounds(23 + x * 66, 22 + y * 66, 40, 40);
                }
                else{
                    bluePawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/BluePawn.png"))));
                    bluePawn.setBounds(34 + x * 98, 38 + y * 98, 50, 50);
                }
                bluePawn.addMouseListener(new MouseListener() {
                    public void mousePressed(MouseEvent e) {
                    }

                    public void mouseReleased(MouseEvent e) {
                    }

                    public void mouseExited(MouseEvent e) {
                        if(p.getPlayerOrder()==board.getTurnOrder()){
                            frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            frame.repaint();
                        }
                    }

                    public void mouseEntered(MouseEvent e) {
                        if(p.getPlayerOrder()==board.getTurnOrder()){
                            frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            frame.repaint();
                        }
                    }

                    public void mouseClicked(MouseEvent e) {
                        frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        if(p.getPlayerOrder()==board.getTurnOrder()){
                            isPawnClicked=true;
                            addMoves(p, board, b, walls, playerColor,name,frame,dim);
                        }
                    }
                });
                b.add(bluePawn);
            }
            case "red" -> {
                JLabel redPawn = new JLabel();
                if(dim.height<1000){
                    redPawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/RedPawn.png"))));
                    redPawn.setBounds(23 + x * 66, 22 + y * 66, 40, 40);
                }
                else{
                    redPawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/RedPawn.png"))));
                    redPawn.setBounds(34 + x * 98, 38 + y * 98, 50, 50);
                }
                redPawn.addMouseListener(new MouseListener() {
                    public void mousePressed(MouseEvent e) {
                    }

                    public void mouseReleased(MouseEvent e) {
                    }

                    public void mouseExited(MouseEvent e) {
                        if(p.getPlayerOrder()==board.getTurnOrder()){
                            frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            frame.repaint();
                        }
                    }

                    public void mouseEntered(MouseEvent e) {
                        if(p.getPlayerOrder()==board.getTurnOrder()){
                            frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            frame.repaint();
                        }
                    }

                    public void mouseClicked(MouseEvent e) {
                        frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        if(p.getPlayerOrder()==board.getTurnOrder()){
                            isPawnClicked=true;
                            addMoves(p, board, b, walls, playerColor, name, frame,dim);
                        }
                    }
                });
                b.add(redPawn);
            }
            case "green" -> {
                JLabel greenPawn = new JLabel();
                if(dim.height<1000){
                    greenPawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/GreenPawn.png"))));
                    greenPawn.setBounds(23 + x * 66, 22 + y * 66, 40, 40);
                }
                else{
                    greenPawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/GreenPawn.png"))));
                    greenPawn.setBounds(34 + x * 98, 38 + y * 98, 50, 50);
                }
                greenPawn.addMouseListener(new MouseListener() {
                    public void mousePressed(MouseEvent e) {
                    }

                    public void mouseReleased(MouseEvent e) {
                    }

                    public void mouseExited(MouseEvent e) {
                        if(p.getPlayerOrder()==board.getTurnOrder()){
                            frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            frame.repaint();
                        }
                    }

                    public void mouseEntered(MouseEvent e) {
                        if(p.getPlayerOrder()==board.getTurnOrder()){
                            frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            frame.repaint();
                        }
                    }

                    public void mouseClicked(MouseEvent e) {
                        frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        if(p.getPlayerOrder()==board.getTurnOrder()){
                            isPawnClicked=true;
                            addMoves(p, board, b, walls, playerColor, name, frame,dim);
                        }
                    }
                });
                b.add(greenPawn);
            }
            case "yellow" -> {
                JLabel yellowPawn = new JLabel();
                if(dim.height<1000){
                    yellowPawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/YellowPawn.png"))));
                    yellowPawn.setBounds(23 + x * 66, 22 + y * 66, 40, 40);
                }
                else{
                    yellowPawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/YellowPawn.png"))));
                    yellowPawn.setBounds(34 + x * 98, 38 + y * 98, 50, 50);
                }
                yellowPawn.addMouseListener(new MouseListener() {
                    public void mousePressed(MouseEvent e) {
                    }

                    public void mouseReleased(MouseEvent e) {
                    }

                    public void mouseExited(MouseEvent e) {
                        if(p.getPlayerOrder()==board.getTurnOrder()){
                            frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            frame.repaint();
                        }
                    }

                    public void mouseEntered(MouseEvent e) {
                        if(p.getPlayerOrder()==board.getTurnOrder()){
                            frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            frame.repaint();
                        }
                    }

                    public void mouseClicked(MouseEvent e) {
                        frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        if(p.getPlayerOrder()==board.getTurnOrder()){
                            isPawnClicked=true;
                            addMoves(p, board, b, walls, playerColor, name, frame,dim);
                        }
                    }
                });
                b.add(yellowPawn);
            }
        }
    }

    public int[] calculatePawnCoords(int pos){
        int[] coords=new int[2];
        int j=pos,i;

        for(i=0;j>=10;i++){
            j-=9;
        }
        coords[0]=j-1;
        coords[1]=i;

        return coords;
    }

    public void addInitialMovableWalls(JFrame frame, Dimension dim, Board board, JLabel walls, JLabel playerColor, JLabel name){

        for(int i=1;i<=4;i++){
            JLabel rect=new JLabel();
            if(i==3 || i==4){
                if(dim.height<1000){
                    rect.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/rectVert.png"))));
                    rect.setBounds(dim.width-250, 120, 20, 110);
                }
                else{
                    rect.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/rectVert.png"))));
                    rect.setBounds(dim.width-400, 170, 30, 150);
                }
            }
            else{
                if(dim.height<1000){
                    rect.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/rectHor.png"))));
                    rect.setBounds(dim.width-200, 160, 110, 20);
                }
                else{
                    rect.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/rectHor.png"))));
                    rect.setBounds(dim.width-400, 20, 150, 30);
                }
            }
            rect.setDoubleBuffered(true);
            
            if(i%2==0){
                rect.addMouseListener(new MouseListener(){
                    public void mouseClicked(MouseEvent e){}
                    public void mouseEntered(MouseEvent e){
                        if(board.getCurrentPlayer().getWallsLeft()==0) return;
                        if(!isPawnClicked){
                            frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            frame.repaint();
                        }
                    }
                    public void mouseExited(MouseEvent e){
                        if(board.getCurrentPlayer().getWallsLeft()==0) return;
                        if(!isPawnClicked){
                            frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            frame.repaint();
                        }
                    }
                    public void mousePressed(MouseEvent e){
                        if(board.getCurrentPlayer().getWallsLeft()==0) return;
                        if(!isPawnClicked){
                            JLabel label = (JLabel) e.getComponent();
                            frame.setComponentZOrder(label, 0);
                            offset = e.getPoint();
                        }
                    }
                    public void mouseReleased(MouseEvent e){
                        int wall;
                        if(board.getCurrentPlayer().getWallsLeft()==0) return;

                        if(!isPawnClicked){
                            frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            rect.removeMouseMotionListener(m);
                            rect.removeMouseListener(this);
                            //rect.setBounds(550,200,150,30); usar para dar lock na peça
                            Component component = e.getComponent();
                            //System.out.println(component.getLocation()); //isto da a posiçao da barreira completa (sempre igual nao importa a posiçao do rato)
                            if(rect.getWidth()==150 || rect.getWidth()==110){
                                addNewMovableWall(frame, dim, board, walls,playerColor, name, false);
                                wall=l.putWall(board,dim, component.getLocation(),rect,false);
                            }
                            else{
                                addNewMovableWall(frame, dim , board, walls,playerColor, name, true);
                                wall=l.putWall(board,dim,component.getLocation(),rect,true);
                            }
                            if(wall==1){
                                board.nextTurn();
                                walls.setText(String.valueOf(board.getCurrentPlayer().getWallsLeft()));
                                name.setText(board.getCurrentPlayer().getPlayerName());
                                switch (board.getCurrentPlayer().getPawnColor()) {
                                    case "blue" ->
                                            playerColor.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/BluePawnStart.png"))));
                                    case "red" ->
                                            playerColor.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/RedPawnStart.png"))));
                                    case "green" ->
                                            playerColor.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/GreenPawnStart.png"))));
                                    case "yellow" ->
                                            playerColor.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/YellowPawnStart.png"))));
                                }
                            }
                            frame.repaint();
                        }

                    }
                });
                
                rect.addMouseMotionListener(m);
            }
            frame.getContentPane().add(rect);
        }
    }

    public void addNewMovableWall(JFrame frame, Dimension dim, Board board, JLabel walls, JLabel playerColor, JLabel name, boolean isVertical){
        JLabel rect=new JLabel();
        if(isVertical){
            if(dim.height<1000){
                rect.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/rectVert.png"))));
                rect.setBounds(dim.width-250, 120, 20, 110);
            }
            else{
                rect.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/rectVert.png"))));
                rect.setBounds(dim.width-400, 100, 30, 150);
            }
        }
        if(!isVertical){
            if(dim.height<1000){
                rect.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/rectHor.png"))));
                rect.setBounds(dim.width-200, 160, 110, 20);
            }
            else{
                rect.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/rectHor.png"))));
                rect.setBounds(dim.width-400, 20, 150, 30);
            }
        }
        rect.setDoubleBuffered(true);
        
        rect.addMouseListener(new MouseListener(){
            public void mouseClicked(MouseEvent e){}
            public void mouseEntered(MouseEvent e){
                if(board.getCurrentPlayer().getWallsLeft()==0) return;
                if(!isPawnClicked){
                    frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    frame.repaint();
                }
            }
            public void mouseExited(MouseEvent e){
                if(board.getCurrentPlayer().getWallsLeft()==0) return;
                if(!isPawnClicked){
                    frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    frame.repaint();
                }
            }
            public void mousePressed(MouseEvent e){
                if(board.getCurrentPlayer().getWallsLeft()==0) return;
                if(!isPawnClicked){
                    JLabel label = (JLabel) e.getComponent();
                    frame.setComponentZOrder(label, 0);
                    offset = e.getPoint();
                }
            }
            public void mouseReleased(MouseEvent e){
                int wall;
                if(board.getCurrentPlayer().getWallsLeft()==0) return;

                if(!isPawnClicked){
                    frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    rect.removeMouseMotionListener(m);
                    rect.removeMouseListener(this);
                    Component component = e.getComponent();
                    //System.out.println(component.getLocation()); //isto da a posiçao da barreira completa (sempre igual nao importa a posiçao do rato)
                    if(rect.getWidth()==150 || rect.getWidth()==110){
                        addNewMovableWall(frame, dim, board, walls,playerColor, name, false);
                        wall=l.putWall(board,dim,component.getLocation(),rect,false);
                    }
                    else{
                        addNewMovableWall(frame, dim , board, walls,playerColor, name, true);
                        wall=l.putWall(board,dim,component.getLocation(),rect,true);
                    }
                    if(wall==1){
                        board.nextTurn();
                        walls.setText(String.valueOf(board.getCurrentPlayer().getWallsLeft()));
                        name.setText(board.getCurrentPlayer().getPlayerName());
                        switch (board.getCurrentPlayer().getPawnColor()) {
                            case "blue" ->
                                    playerColor.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/BluePawnStart.png"))));
                            case "red" ->
                                    playerColor.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/RedPawnStart.png"))));
                            case "green" ->
                                    playerColor.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/GreenPawnStart.png"))));
                            case "yellow" ->
                                    playerColor.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/YellowPawnStart.png"))));
                        }
                    }
                    frame.repaint();
                }
            }
        });

        rect.addMouseMotionListener(m);
        
        frame.getContentPane().add(rect);
    }

    public void selectPlayers(JFrame frame, Dimension dim, Board board){

        JLabel label=new JLabel("Number of Players");
        if(dim.height<1000){
            label.setFont(new Font(Font.SANS_SERIF,Font.BOLD,60));
            label.setBounds(dim.width/4+20,30,1000,250);
        }
        else{
            label.setFont(new Font(Font.SANS_SERIF,Font.BOLD,100));
            label.setBounds(dim.width/4+50,50,1000,250);
        }
        label.setForeground(Color.black);
        frame.getContentPane().add(label);

        JLabel bluePawn=new JLabel();
        if(dim.height<1000){
            bluePawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/BluePawnStart.png"))));
            bluePawn.setBounds(dim.width/4+100, dim.height/2, 150, 150);
        }
        else{
            bluePawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/BluePawnStart.png"))));
            bluePawn.setBounds(dim.width/4+200, dim.height/2, 200, 200);
        }
        frame.getContentPane().add(bluePawn);

        JLabel bluePawn2=new JLabel();
        if(dim.height<1000){
            bluePawn2.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/BluePawnStart.png"))));
            bluePawn2.setBounds(dim.width/4+400, dim.height/2, 150, 150);
        }
        else{
            bluePawn2.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/BluePawnStart.png"))));
            bluePawn2.setBounds(dim.width/4+800, dim.height/2, 200, 200);
        }
        frame.getContentPane().add(bluePawn2);

        JLabel redPawn=new JLabel();
        if(dim.height<1000){
            redPawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/RedPawnStart.png"))));
            redPawn.setBounds(dim.width/4, dim.height/2-150, 150, 150);
        }
        else{
            redPawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/RedPawnStart.png"))));
            redPawn.setBounds(dim.width/4, dim.height/2-200, 200, 200);
        }
        frame.getContentPane().add(redPawn);

        JLabel redPawn2=new JLabel();
        if(dim.height<1000){
            redPawn2.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/RedPawnStart.png"))));
            redPawn2.setBounds(dim.width/4+525, dim.height/2-150, 150, 150);
        }
        else{
            redPawn2.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/RedPawnStart.png"))));
            redPawn2.setBounds(dim.width/4+600, dim.height/2-200, 200, 200);
        }
        frame.getContentPane().add(redPawn2);

        JLabel greenPawn=new JLabel();
        if(dim.height<1000){
            greenPawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/GreenPawnStart.png"))));
            greenPawn.setBounds(dim.width/4+525, dim.height/2, 150, 150);
        }
        else{
            greenPawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/GreenPawnStart.png"))));
            greenPawn.setBounds(dim.width/4+600, dim.height/2, 200, 200);
        }
        frame.getContentPane().add(greenPawn);

        JLabel yellowPawn=new JLabel();
        if(dim.height<1000){
            yellowPawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/YellowPawnStart.png"))));
            yellowPawn.setBounds(dim.width/4+400, dim.height/2-150, 150, 150);
        }
        else{
            yellowPawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/YellowPawnStart.png"))));
            yellowPawn.setBounds(dim.width/4+800, dim.height/2-200, 200, 200);
        }
        frame.getContentPane().add(yellowPawn);

        JLabel p2=new JLabel("2 Players");
        if(dim.height<1000){
            p2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,35));
            p2.setBounds(dim.width/4+35,dim.height/2+160,185,40);
        }
        else{
            p2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,50));
            p2.setBounds(dim.width/4+100,dim.height/2+200,230,60);
        }
        p2.setForeground(Color.black);
        p2.addMouseListener(new MouseListener(){
            public void mousePressed(MouseEvent e){}
            public void mouseReleased(MouseEvent e){}

            public void mouseExited(MouseEvent e){
                frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                frame.repaint();
            }
            public void mouseEntered(MouseEvent e){
                frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
                frame.repaint();
            }
            public void mouseClicked(MouseEvent e) {
                frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                selected2Players(frame, dim, board);
            }
        });
        frame.getContentPane().add(p2);

        JLabel p4=new JLabel("4 Players");
        if(dim.height<1000){
            p4.setFont(new Font(Font.SANS_SERIF,Font.BOLD,35));
            p4.setBounds(dim.width/4+450,dim.height/2+160,185,40);
        }
        else{
            p4.setFont(new Font(Font.SANS_SERIF,Font.BOLD,50));
            p4.setBounds(dim.width/4+700,dim.height/2+200,230,60);
        }
        p4.setForeground(Color.black);
        p4.addMouseListener(new MouseListener(){
            public void mousePressed(MouseEvent e){}
            public void mouseReleased(MouseEvent e){}

            public void mouseExited(MouseEvent e){
                frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                frame.repaint();
            }
            public void mouseEntered(MouseEvent e){
                frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
                frame.repaint();
            }
            public void mouseClicked(MouseEvent e) {
                frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                selected4Players(frame,dim,board);
            }
        });
        frame.getContentPane().add(p4);
    }

    public void selected2Players(JFrame frame, Dimension dim, Board board){
        cleanFrame(frame);
        addBackArrow(frame,dim);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        int[] pawns={77,5,0,0};
        board.setPawnsLocations(pawns);
        Player p1=new Player(10,"up",pawns[0]);
        Player p2=new Player(10,"down",pawns[1]);

        board.getPlayers().add(p1);
        board.getPlayers().add(p2);

        chooseColorAndName(frame, dim, board, 2);
    }

    public void selected4Players(JFrame frame, Dimension dim, Board board){
        cleanFrame(frame);
        addBackArrow(frame,dim);

        int[] pawns={77,5,37,45};
        board.setPawnsLocations(pawns);
        Player p1=new Player(5,"up",pawns[0]);
        Player p2=new Player(5,"down",pawns[1]);
        Player p3=new Player(5,"right",pawns[2]);
        Player p4=new Player(5,"left",pawns[3]);

        board.getPlayers().add(p1);
        board.getPlayers().add(p2);
        board.getPlayers().add(p3);
        board.getPlayers().add(p4);

        chooseColorAndName(frame, dim, board, 4);
    }

    public void chooseColorAndName(JFrame frame, Dimension dim, Board board, int playersCount){
        cleanFrame(frame);

        JLabel player1=new JLabel("Player 1: ");
        if(dim.height<1000){
            player1.setFont(new Font(Font.SANS_SERIF,Font.BOLD,40));
            player1.setBounds(dim.width/4, 50, 230, 60);
        }
        else{
            player1.setFont(new Font(Font.SANS_SERIF,Font.BOLD,50));
            player1.setBounds(dim.width/4, 200, 230, 60);
        }
        player1.setForeground(Color.black);
        frame.getContentPane().add(player1);

        JTextField name1=new JTextField("PlayerName");
        if(dim.height<1000){
            name1.setBounds(dim.width/4+210, 55, 150, 50);
            name1.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
        }
        else{
            name1.setBounds(dim.width/4+230, 205, 150, 50);
            name1.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
        }
        frame.getContentPane().add(name1);

        JLabel player2=new JLabel("Player 2: ");
        if(dim.height<1000){
            player2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,40));
            player2.setBounds(dim.width/4, 150, 230, 60);
        }
        else{
            player2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,50));
            player2.setBounds(dim.width/4, 300, 230, 60);
        }
        player2.setForeground(Color.black);
        frame.getContentPane().add(player2);

        JTextField name2=new JTextField("PlayerName");
        if(dim.height<1000){
            name2.setBounds(dim.width/4+210, 155, 150, 50);
            name2.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
        }
        else{
            name2.setBounds(dim.width/4+230, 305, 150, 50);
            name2.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
        }
        frame.getContentPane().add(name2);
        
        JLabel player3=new JLabel("Player 3: ");
        if(dim.height<1000){
            player3.setFont(new Font(Font.SANS_SERIF,Font.BOLD,40));
            player3.setBounds(dim.width/4, 250, 230, 60);
        }
        else{
            player3.setFont(new Font(Font.SANS_SERIF,Font.BOLD,50));
            player3.setBounds(dim.width/4, 400, 230, 60);
        }
        player3.setForeground(Color.black);
        player3.setVisible(false);
        frame.getContentPane().add(player3);

        JTextField name3=new JTextField("PlayerName");
        if(dim.height<1000){
            name3.setBounds(dim.width/4+210, 255, 150, 50);
            name3.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
        }
        else{
            name3.setBounds(dim.width/4+230, 405, 150, 50);
            name3.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
        }
        name3.setVisible(false);
        frame.getContentPane().add(name3);

        JLabel player4=new JLabel("Player 4: ");
        if(dim.height<1000){
            player4.setFont(new Font(Font.SANS_SERIF,Font.BOLD,40));
            player4.setBounds(dim.width/4, 350, 230, 60);
        }
        else{
            player4.setFont(new Font(Font.SANS_SERIF,Font.BOLD,50));
            player4.setBounds(dim.width/4, 500, 230, 60);
        }
        player4.setForeground(Color.black);
        player4.setVisible(false);
        frame.getContentPane().add(player4);

        JTextField name4=new JTextField("PlayerName");
        if(dim.height<1000){
            name4.setBounds(dim.width/4+210, 355, 150, 50);
            name4.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
        }
        else{
            name4.setBounds(dim.width/4+230, 505, 150, 50);
            name4.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
        }
        name4.setVisible(false);
        frame.getContentPane().add(name4);
        
        if(playersCount==4){
            player3.setVisible(true);
            name3.setVisible(true);
            player4.setVisible(true);
            name4.setVisible(true);
        }

        JButton red1=new JButton("Red",new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/RedPawn.png"))));
        red1.setBounds(dim.width/4+400, 40, 100, 80);
        red1.setVerticalTextPosition(AbstractButton.BOTTOM);
        red1.setHorizontalTextPosition(AbstractButton.CENTER);
        red1.setActionCommand("red1");
        frame.getContentPane().add(red1);
        JLabel p1Red=new JLabel();
        p1Red.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/RedPawn.png"))));
        p1Red.setBounds(dim.width/4-60, 45, 60, 60);
        p1Red.setVisible(false);
        frame.getContentPane().add(p1Red);

        JButton red2=new JButton("Red",new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/RedPawn.png"))));
        red2.setBounds(dim.width/4+400, 140, 100, 80);
        red2.setVerticalTextPosition(AbstractButton.BOTTOM);
        red2.setHorizontalTextPosition(AbstractButton.CENTER);
        red2.setActionCommand("red2");
        frame.getContentPane().add(red2);
        JLabel p2Red=new JLabel();
        p2Red.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/RedPawn.png"))));
        p2Red.setBounds(dim.width/4-60, 145, 60, 60);
        p2Red.setVisible(false);
        frame.getContentPane().add(p2Red);

        JButton red3=new JButton("Red",new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/RedPawn.png"))));
        red3.setBounds(dim.width/4+400, 240, 100, 80);
        red3.setVerticalTextPosition(AbstractButton.BOTTOM);
        red3.setHorizontalTextPosition(AbstractButton.CENTER);
        red3.setActionCommand("red3");
        frame.getContentPane().add(red3);
        JLabel p3Red=new JLabel();
        p3Red.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/RedPawn.png"))));
        p3Red.setBounds(dim.width/4-60, 245, 60, 60);
        p3Red.setVisible(false);
        frame.getContentPane().add(p3Red);

        JButton red4=new JButton("Red",new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/RedPawn.png"))));
        red4.setBounds(dim.width/4+400, 340, 100, 80);
        red4.setVerticalTextPosition(AbstractButton.BOTTOM);
        red4.setHorizontalTextPosition(AbstractButton.CENTER);
        red4.setActionCommand("red4");
        frame.getContentPane().add(red4);
        JLabel p4Red=new JLabel();
        p4Red.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/RedPawn.png"))));
        p4Red.setBounds(dim.width/4-60, 345, 60, 60);
        p4Red.setVisible(false);
        frame.getContentPane().add(p4Red);

        JButton blue1=new JButton("Blue",new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/BluePawn.png"))));
        blue1.setBounds(dim.width/4+500, 40, 100, 80);
        blue1.setVerticalTextPosition(AbstractButton.BOTTOM);
        blue1.setHorizontalTextPosition(AbstractButton.CENTER);
        blue1.setActionCommand("blue1");
        frame.getContentPane().add(blue1);
        JLabel p1Blue=new JLabel();
        p1Blue.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/BluePawn.png"))));
        p1Blue.setBounds(dim.width/4-60, 45, 60, 60);
        p1Blue.setVisible(false);
        frame.getContentPane().add(p1Blue);

        JButton blue2=new JButton("Blue",new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/BluePawn.png"))));
        blue2.setBounds(dim.width/4+500, 140, 100, 80);
        blue2.setVerticalTextPosition(AbstractButton.BOTTOM);
        blue2.setHorizontalTextPosition(AbstractButton.CENTER);
        blue2.setActionCommand("blue2");
        frame.getContentPane().add(blue2);
        JLabel p2Blue=new JLabel();
        p2Blue.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/BluePawn.png"))));
        p2Blue.setBounds(dim.width/4-60, 145, 60, 60);
        p2Blue.setVisible(false);
        frame.getContentPane().add(p2Blue);

        JButton blue3=new JButton("Blue",new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/BluePawn.png"))));
        blue3.setBounds(dim.width/4+500, 240, 100, 80);
        blue3.setVerticalTextPosition(AbstractButton.BOTTOM);
        blue3.setHorizontalTextPosition(AbstractButton.CENTER);
        blue3.setActionCommand("blue3");
        frame.getContentPane().add(blue3);
        JLabel p3Blue=new JLabel();
        p3Blue.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/BluePawn.png"))));
        p3Blue.setBounds(dim.width/4-60, 245, 60, 60);
        p3Blue.setVisible(false);
        frame.getContentPane().add(p3Blue);

        JButton blue4=new JButton("Blue",new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/BluePawn.png"))));
        blue4.setBounds(dim.width/4+500, 340, 100, 80);
        blue4.setVerticalTextPosition(AbstractButton.BOTTOM);
        blue4.setHorizontalTextPosition(AbstractButton.CENTER);
        blue4.setActionCommand("blue4");
        frame.getContentPane().add(blue4);
        JLabel p4Blue=new JLabel();
        p4Blue.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/BluePawn.png"))));
        p4Blue.setBounds(dim.width/4-60, 345, 60, 60);
        p4Blue.setVisible(false);
        frame.getContentPane().add(p4Blue);

        JButton green1=new JButton("Green",new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/GreenPawn.png"))));
        green1.setBounds(dim.width/4+600, 40, 100, 80);
        green1.setVerticalTextPosition(AbstractButton.BOTTOM);
        green1.setHorizontalTextPosition(AbstractButton.CENTER);
        green1.setActionCommand("green1");
        frame.getContentPane().add(green1);
        JLabel p1Green=new JLabel();
        p1Green.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/GreenPawn.png"))));
        p1Green.setBounds(dim.width/4-60, 45, 60, 60);
        p1Green.setVisible(false);
        frame.getContentPane().add(p1Green);

        JButton green2=new JButton("Green",new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/GreenPawn.png"))));
        green2.setBounds(dim.width/4+600, 140, 100, 80);
        green2.setVerticalTextPosition(AbstractButton.BOTTOM);
        green2.setHorizontalTextPosition(AbstractButton.CENTER);
        green2.setActionCommand("green2");
        frame.getContentPane().add(green2);
        JLabel p2Green=new JLabel();
        p2Green.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/GreenPawn.png"))));
        p2Green.setBounds(dim.width/4-60, 145, 60, 60);
        p2Green.setVisible(false);
        frame.getContentPane().add(p2Green);

        JButton green3=new JButton("Green",new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/GreenPawn.png"))));
        green3.setBounds(dim.width/4+600, 240, 100, 80);
        green3.setVerticalTextPosition(AbstractButton.BOTTOM);
        green3.setHorizontalTextPosition(AbstractButton.CENTER);
        green3.setActionCommand("green3");
        frame.getContentPane().add(green3);
        JLabel p3Green=new JLabel();
        p3Green.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/GreenPawn.png"))));
        p3Green.setBounds(dim.width/4-60, 245, 60, 60);
        p3Green.setVisible(false);
        frame.getContentPane().add(p3Green);

        JButton green4=new JButton("Green",new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/GreenPawn.png"))));
        green4.setBounds(dim.width/4+600, 340, 100, 80);
        green4.setVerticalTextPosition(AbstractButton.BOTTOM);
        green4.setHorizontalTextPosition(AbstractButton.CENTER);
        green4.setActionCommand("green4");
        frame.getContentPane().add(green4);
        JLabel p4Green=new JLabel();
        p4Green.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/GreenPawn.png"))));
        p4Green.setBounds(dim.width/4-60, 345, 60, 60);
        p4Green.setVisible(false);
        frame.getContentPane().add(p4Green);

        JButton yellow1=new JButton("Yellow",new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/YellowPawn.png"))));
        yellow1.setBounds(dim.width/4+700, 40, 100, 80);
        yellow1.setVerticalTextPosition(AbstractButton.BOTTOM);
        yellow1.setHorizontalTextPosition(AbstractButton.CENTER);
        yellow1.setActionCommand("yellow1");
        frame.getContentPane().add(yellow1);
        JLabel p1Yellow=new JLabel();
        p1Yellow.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/YellowPawn.png"))));
        p1Yellow.setBounds(dim.width/4-60, 45, 60, 60);
        p1Yellow.setVisible(false);
        frame.getContentPane().add(p1Yellow);

        JButton yellow2=new JButton("Yellow",new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/YellowPawn.png"))));
        yellow2.setBounds(dim.width/4+700, 140, 100, 80);
        yellow2.setVerticalTextPosition(AbstractButton.BOTTOM);
        yellow2.setHorizontalTextPosition(AbstractButton.CENTER);
        yellow2.setActionCommand("yellow2");
        frame.getContentPane().add(yellow2);
        JLabel p2Yellow=new JLabel();
        p2Yellow.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/YellowPawn.png"))));
        p2Yellow.setBounds(dim.width/4-60, 145, 60, 60);
        p2Yellow.setVisible(false);
        frame.getContentPane().add(p2Yellow);

        JButton yellow3=new JButton("Yellow",new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/YellowPawn.png"))));
        yellow3.setBounds(dim.width/4+700, 240, 100, 80);
        yellow3.setVerticalTextPosition(AbstractButton.BOTTOM);
        yellow3.setHorizontalTextPosition(AbstractButton.CENTER);
        yellow3.setActionCommand("yellow3");
        frame.getContentPane().add(yellow3);
        JLabel p3Yellow=new JLabel();
        p3Yellow.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/YellowPawn.png"))));
        p3Yellow.setBounds(dim.width/4-60, 245, 60, 60);
        p3Yellow.setVisible(false);
        frame.getContentPane().add(p3Yellow);

        JButton yellow4=new JButton("Yellow",new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/YellowPawn.png"))));
        yellow4.setBounds(dim.width/4+700, 340, 100, 80);
        yellow4.setVerticalTextPosition(AbstractButton.BOTTOM);
        yellow4.setHorizontalTextPosition(AbstractButton.CENTER);
        yellow4.setActionCommand("yellow4");
        frame.getContentPane().add(yellow4);
        JLabel p4Yellow=new JLabel();
        p4Yellow.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/YellowPawn.png"))));
        p4Yellow.setBounds(dim.width/4-60, 345, 60, 60);
        p4Yellow.setVisible(false);
        frame.getContentPane().add(p4Yellow);

        ActionListener action= e -> {
            if("red1".equals(e.getActionCommand())){
                red1.setEnabled(false); red3.setEnabled(false);
                red2.setEnabled(false); red4.setEnabled(false);

                if(p1Blue.isVisible()){
                    blue1.setEnabled(true); blue3.setEnabled(true);
                    blue2.setEnabled(true); blue4.setEnabled(true);
                }
                else if(p1Green.isVisible()){
                    green1.setEnabled(true); green3.setEnabled(true);
                    green2.setEnabled(true); green4.setEnabled(true);
                }
                else if(p1Yellow.isVisible()){
                    yellow1.setEnabled(true); yellow3.setEnabled(true);
                    yellow2.setEnabled(true); yellow4.setEnabled(true);
                }

                p1Red.setVisible(true); p1Blue.setVisible(false);
                p1Green.setVisible(false); p1Yellow.setVisible(false);
            }
            else if("red2".equals(e.getActionCommand())){
                red1.setEnabled(false); red3.setEnabled(false);
                red2.setEnabled(false); red4.setEnabled(false);

                if(p2Blue.isVisible()){
                    blue1.setEnabled(true); blue3.setEnabled(true);
                    blue2.setEnabled(true); blue4.setEnabled(true);
                }
                else if(p2Green.isVisible()){
                    green1.setEnabled(true); green3.setEnabled(true);
                    green2.setEnabled(true); green4.setEnabled(true);
                }
                else if(p2Yellow.isVisible()){
                    yellow1.setEnabled(true); yellow3.setEnabled(true);
                    yellow2.setEnabled(true); yellow4.setEnabled(true);
                }

                p2Red.setVisible(true); p2Blue.setVisible(false);
                p2Green.setVisible(false); p2Yellow.setVisible(false);
            }
            else if("red3".equals(e.getActionCommand())){
                red1.setEnabled(false); red3.setEnabled(false);
                red2.setEnabled(false); red4.setEnabled(false);

                if(p3Blue.isVisible()){
                    blue1.setEnabled(true); blue3.setEnabled(true);
                    blue2.setEnabled(true); blue4.setEnabled(true);
                }
                else if(p3Green.isVisible()){
                    green1.setEnabled(true); green3.setEnabled(true);
                    green2.setEnabled(true); green4.setEnabled(true);
                }
                else if(p3Yellow.isVisible()){
                    yellow1.setEnabled(true); yellow3.setEnabled(true);
                    yellow2.setEnabled(true); yellow4.setEnabled(true);
                }

                p3Red.setVisible(true); p3Blue.setVisible(false);
                p3Green.setVisible(false); p3Yellow.setVisible(false);
            }
            else if("red4".equals(e.getActionCommand())){
                red1.setEnabled(false); red3.setEnabled(false);
                red2.setEnabled(false); red4.setEnabled(false);

                if(p4Blue.isVisible()){
                    blue1.setEnabled(true); blue3.setEnabled(true);
                    blue2.setEnabled(true); blue4.setEnabled(true);
                }
                else if(p4Green.isVisible()){
                    green1.setEnabled(true); green3.setEnabled(true);
                    green2.setEnabled(true); green4.setEnabled(true);
                }
                else if(p4Yellow.isVisible()){
                    yellow1.setEnabled(true); yellow3.setEnabled(true);
                    yellow2.setEnabled(true); yellow4.setEnabled(true);
                }

                p4Red.setVisible(true); p4Blue.setVisible(false);
                p4Green.setVisible(false); p4Yellow.setVisible(false);
            }
            else if("blue1".equals(e.getActionCommand())){
                blue1.setEnabled(false); blue3.setEnabled(false);
                blue2.setEnabled(false); blue4.setEnabled(false);

                if(p1Red.isVisible()){
                    red1.setEnabled(true); red3.setEnabled(true);
                    red2.setEnabled(true); red4.setEnabled(true);
                }
                else if(p1Green.isVisible()){
                    green1.setEnabled(true); green3.setEnabled(true);
                    green2.setEnabled(true); green4.setEnabled(true);
                }
                else if(p1Yellow.isVisible()){
                    yellow1.setEnabled(true); yellow3.setEnabled(true);
                    yellow2.setEnabled(true); yellow4.setEnabled(true);
                }

                p1Red.setVisible(false); p1Blue.setVisible(true);
                p1Green.setVisible(false); p1Yellow.setVisible(false);
            }
            else if("blue2".equals(e.getActionCommand())){
                blue1.setEnabled(false); blue3.setEnabled(false);
                blue2.setEnabled(false); blue4.setEnabled(false);

                if(p2Red.isVisible()){
                    red1.setEnabled(true); red3.setEnabled(true);
                    red2.setEnabled(true); red4.setEnabled(true);
                }
                else if(p2Green.isVisible()){
                    green1.setEnabled(true); green3.setEnabled(true);
                    green2.setEnabled(true); green4.setEnabled(true);
                }
                else if(p2Yellow.isVisible()){
                    yellow1.setEnabled(true); yellow3.setEnabled(true);
                    yellow2.setEnabled(true); yellow4.setEnabled(true);
                }

                p2Red.setVisible(false); p2Blue.setVisible(true);
                p2Green.setVisible(false); p2Yellow.setVisible(false);
            }
            else if("blue3".equals(e.getActionCommand())){
                blue1.setEnabled(false); blue3.setEnabled(false);
                blue2.setEnabled(false); blue4.setEnabled(false);

                if(p3Red.isVisible()){
                    red1.setEnabled(true); red3.setEnabled(true);
                    red2.setEnabled(true); red4.setEnabled(true);
                }
                else if(p3Green.isVisible()){
                    green1.setEnabled(true); green3.setEnabled(true);
                    green2.setEnabled(true); green4.setEnabled(true);
                }
                else if(p3Yellow.isVisible()){
                    yellow1.setEnabled(true); yellow3.setEnabled(true);
                    yellow2.setEnabled(true); yellow4.setEnabled(true);
                }

                p3Red.setVisible(false); p3Blue.setVisible(true);
                p3Green.setVisible(false); p3Yellow.setVisible(false);
            }
            else if("blue4".equals(e.getActionCommand())){
                blue1.setEnabled(false); blue3.setEnabled(false);
                blue2.setEnabled(false); blue4.setEnabled(false);

                if(p4Red.isVisible()){
                    red1.setEnabled(true); red3.setEnabled(true);
                    red2.setEnabled(true); red4.setEnabled(true);
                }
                else if(p4Green.isVisible()){
                    green1.setEnabled(true); green3.setEnabled(true);
                    green2.setEnabled(true); green4.setEnabled(true);
                }
                else if(p4Yellow.isVisible()){
                    yellow1.setEnabled(true); yellow3.setEnabled(true);
                    yellow2.setEnabled(true); yellow4.setEnabled(true);
                }

                p4Red.setVisible(false); p4Blue.setVisible(true);
                p4Green.setVisible(false); p4Yellow.setVisible(false);
            }
            else if("green1".equals(e.getActionCommand())){
                green1.setEnabled(false); green3.setEnabled(false);
                green2.setEnabled(false); green4.setEnabled(false);

                if(p1Red.isVisible()){
                    red1.setEnabled(true); red3.setEnabled(true);
                    red2.setEnabled(true); red4.setEnabled(true);
                }
                else if(p1Blue.isVisible()){
                    blue1.setEnabled(true); blue3.setEnabled(true);
                    blue2.setEnabled(true); blue4.setEnabled(true);
                }
                else if(p1Yellow.isVisible()){
                    yellow1.setEnabled(true); yellow3.setEnabled(true);
                    yellow2.setEnabled(true); yellow4.setEnabled(true);
                }

                p1Red.setVisible(false); p1Blue.setVisible(false);
                p1Green.setVisible(true); p1Yellow.setVisible(false);
            }
            else if("green2".equals(e.getActionCommand())){
                green1.setEnabled(false); green3.setEnabled(false);
                green2.setEnabled(false); green4.setEnabled(false);

                if(p2Red.isVisible()){
                    red1.setEnabled(true); red3.setEnabled(true);
                    red2.setEnabled(true); red4.setEnabled(true);
                }
                else if(p2Blue.isVisible()){
                    blue1.setEnabled(true); blue3.setEnabled(true);
                    blue2.setEnabled(true); blue4.setEnabled(true);
                }
                else if(p2Yellow.isVisible()){
                    yellow1.setEnabled(true); yellow3.setEnabled(true);
                    yellow2.setEnabled(true); yellow4.setEnabled(true);
                }

                p2Red.setVisible(false); p2Blue.setVisible(false);
                p2Green.setVisible(true); p2Yellow.setVisible(false);
            }
            else if("green3".equals(e.getActionCommand())){
                green1.setEnabled(false); green3.setEnabled(false);
                green2.setEnabled(false); green4.setEnabled(false);

                if(p3Red.isVisible()){
                    red1.setEnabled(true); red3.setEnabled(true);
                    red2.setEnabled(true); red4.setEnabled(true);
                }
                else if(p3Blue.isVisible()){
                    blue1.setEnabled(true); blue3.setEnabled(true);
                    blue2.setEnabled(true); blue4.setEnabled(true);
                }
                else if(p3Yellow.isVisible()){
                    yellow1.setEnabled(true); yellow3.setEnabled(true);
                    yellow2.setEnabled(true); yellow4.setEnabled(true);
                }

                p3Red.setVisible(false); p3Blue.setVisible(false);
                p3Green.setVisible(true); p3Yellow.setVisible(false);
            }
            else if("green4".equals(e.getActionCommand())){
                green1.setEnabled(false); green3.setEnabled(false);
                green2.setEnabled(false); green4.setEnabled(false);

                if(p4Red.isVisible()){
                    red1.setEnabled(true); red3.setEnabled(true);
                    red2.setEnabled(true); red4.setEnabled(true);
                }
                else if(p4Blue.isVisible()){
                    blue1.setEnabled(true); blue3.setEnabled(true);
                    blue2.setEnabled(true); blue4.setEnabled(true);
                }
                else if(p4Yellow.isVisible()){
                    yellow1.setEnabled(true); yellow3.setEnabled(true);
                    yellow2.setEnabled(true); yellow4.setEnabled(true);
                }

                p4Red.setVisible(false); p4Blue.setVisible(false);
                p4Green.setVisible(true); p4Yellow.setVisible(false);
            }
            else if("yellow1".equals(e.getActionCommand())){
                yellow1.setEnabled(false); yellow3.setEnabled(false);
                yellow2.setEnabled(false); yellow4.setEnabled(false);

                if(p1Red.isVisible()){
                    red1.setEnabled(true); red3.setEnabled(true);
                    red2.setEnabled(true); red4.setEnabled(true);
                }
                else if(p1Blue.isVisible()){
                    blue1.setEnabled(true); blue3.setEnabled(true);
                    blue2.setEnabled(true); blue4.setEnabled(true);
                }
                else if(p1Green.isVisible()){
                    green1.setEnabled(true); green3.setEnabled(true);
                    green2.setEnabled(true); green4.setEnabled(true);
                }

                p1Red.setVisible(false); p1Blue.setVisible(false);
                p1Green.setVisible(false); p1Yellow.setVisible(true);
            }
            else if("yellow2".equals(e.getActionCommand())){
                yellow1.setEnabled(false); yellow3.setEnabled(false);
                yellow2.setEnabled(false); yellow4.setEnabled(false);

                if(p2Red.isVisible()){
                    red1.setEnabled(true); red3.setEnabled(true);
                    red2.setEnabled(true); red4.setEnabled(true);
                }
                else if(p2Blue.isVisible()){
                    blue1.setEnabled(true); blue3.setEnabled(true);
                    blue2.setEnabled(true); blue4.setEnabled(true);
                }
                else if(p2Green.isVisible()){
                    green1.setEnabled(true); green3.setEnabled(true);
                    green2.setEnabled(true); green4.setEnabled(true);
                }

                p2Red.setVisible(false); p2Blue.setVisible(false);
                p2Green.setVisible(false); p2Yellow.setVisible(true);
            }
            else if("yellow3".equals(e.getActionCommand())){
                yellow1.setEnabled(false); yellow3.setEnabled(false);
                yellow2.setEnabled(false); yellow4.setEnabled(false);

                if(p3Red.isVisible()){
                    red1.setEnabled(true); red3.setEnabled(true);
                    red2.setEnabled(true); red4.setEnabled(true);
                }
                else if(p3Blue.isVisible()){
                    blue1.setEnabled(true); blue3.setEnabled(true);
                    blue2.setEnabled(true); blue4.setEnabled(true);
                }
                else if(p3Green.isVisible()){
                    green1.setEnabled(true); green3.setEnabled(true);
                    green2.setEnabled(true); green4.setEnabled(true);
                }

                p3Red.setVisible(false); p3Blue.setVisible(false);
                p3Green.setVisible(false); p3Yellow.setVisible(true);
            }
            else if("yellow4".equals(e.getActionCommand())){
                yellow1.setEnabled(false); yellow3.setEnabled(false);
                yellow2.setEnabled(false); yellow4.setEnabled(false);

                if(p4Red.isVisible()){
                    red1.setEnabled(true); red3.setEnabled(true);
                    red2.setEnabled(true); red4.setEnabled(true);
                }
                else if(p4Blue.isVisible()){
                    blue1.setEnabled(true); blue3.setEnabled(true);
                    blue2.setEnabled(true); blue4.setEnabled(true);
                }
                else if(p4Green.isVisible()){
                    green1.setEnabled(true); green3.setEnabled(true);
                    green2.setEnabled(true); green4.setEnabled(true);
                }

                p4Red.setVisible(false); p4Blue.setVisible(false);
                p4Green.setVisible(false); p4Yellow.setVisible(true);
            }
        };

        red1.addActionListener(action); blue1.addActionListener(action); green1.addActionListener(action); yellow1.addActionListener(action);
        red2.addActionListener(action); blue2.addActionListener(action); green2.addActionListener(action); yellow2.addActionListener(action);
        red3.addActionListener(action); blue3.addActionListener(action); green3.addActionListener(action); yellow3.addActionListener(action);
        red4.addActionListener(action); blue4.addActionListener(action); green4.addActionListener(action); yellow4.addActionListener(action);

        if(playersCount==2){
            red3.setVisible(false); blue3.setVisible(false); green3.setVisible(false); yellow3.setVisible(false);
            red4.setVisible(false); blue4.setVisible(false); green4.setVisible(false); yellow4.setVisible(false);
        }

        JButton button = new JButton("Done");
        if(dim.height<1000){
            button.setBounds(dim.width/2-106, dim.height-200, 212, 50);
        }
        else{
            button.setBounds(dim.width/2-106, dim.height-200, 212, 50);
        }
        button.addActionListener(e -> {
            int done=1,i=0;

            if(playersCount==4){
                if(name1.getText().equals(name2.getText()) || name1.getText().equals(name3.getText()) ||
                   name1.getText().equals(name4.getText()) || name2.getText().equals(name3.getText()) ||
                   name2.getText().equals(name4.getText()) || name3.getText().equals(name4.getText())) done=0;

                if(red1.isEnabled() || blue1.isEnabled() || green1.isEnabled() || yellow1.isEnabled()) done=0;
            }
            else{
                if(name1.getText().equals(name2.getText())) done=0;

                if(red1.isEnabled()) i++;
                if(blue1.isEnabled()) i++;
                if(green1.isEnabled()) i++;
                if(yellow1.isEnabled()) i++;

                if(i>2) done=0;
            }

            if(done==0){
                JFrame f=new JFrame("ERROR");
                f.setSize(700,300);
                f.setLayout(null);
                f.setBounds(dim.width/4,dim.height/4,700,300);
                f.getContentPane().setBackground(Color.decode("#AF4933"));
                f.setAlwaysOnTop(true);
                f.setVisible(true);

                JLabel label1=new JLabel("All players must choose a color");
                label1.setFont(new Font(Font.SANS_SERIF,Font.BOLD,30));
                label1.setBounds(100,10,700,50);
                label1.setForeground(Color.black);
                f.getContentPane().add(label1);

                JLabel label2=new JLabel("and have different names!");
                label2.setFont(new Font(Font.SANS_SERIF,Font.BOLD,30));
                label2.setBounds(150,55,500,50);
                label2.setForeground(Color.black);
                f.getContentPane().add(label2);

                JButton b=new JButton("OK!");
                b.setBounds(300,150,100,50);
                b.addActionListener(a -> f.setVisible(false));
                f.getContentPane().add(b);
            }

            if(done==1){
                List<Player> players=board.getPlayers();
                
                if(playersCount==4){
                    players.get(0).setPlayerName(name1.getText());
                    players.get(0).setPlayerOrder(1);
                    if(p1Red.isVisible()) players.get(0).setPawnColor("red");
                    else if(p1Blue.isVisible()) players.get(0).setPawnColor("blue");
                    else if(p1Green.isVisible()) players.get(0).setPawnColor("green");
                    else if(p1Yellow.isVisible()) players.get(0).setPawnColor("yellow");

                    players.get(1).setPlayerName(name2.getText());
                    players.get(1).setPlayerOrder(2);
                    if(p2Red.isVisible()) players.get(1).setPawnColor("red");
                    else if(p2Blue.isVisible()) players.get(1).setPawnColor("blue");
                    else if(p2Green.isVisible()) players.get(1).setPawnColor("green");
                    else if(p2Yellow.isVisible()) players.get(1).setPawnColor("yellow");

                    players.get(2).setPlayerName(name3.getText());
                    players.get(2).setPlayerOrder(3);
                    if(p3Red.isVisible()) players.get(2).setPawnColor("red");
                    else if(p3Blue.isVisible()) players.get(2).setPawnColor("blue");
                    else if(p3Green.isVisible()) players.get(2).setPawnColor("green");
                    else if(p3Yellow.isVisible()) players.get(2).setPawnColor("yellow");

                    players.get(3).setPlayerName(name4.getText());
                    players.get(3).setPlayerOrder(4);
                    if(p4Red.isVisible()) players.get(3).setPawnColor("red");
                    else if(p4Blue.isVisible()) players.get(3).setPawnColor("blue");
                    else if(p4Green.isVisible()) players.get(3).setPawnColor("green");
                    else if(p4Yellow.isVisible()) players.get(3).setPawnColor("yellow");
                }
                else{
                    players.get(0).setPlayerName(name1.getText());
                    players.get(0).setPlayerOrder(1);
                    if(p1Red.isVisible()) players.get(0).setPawnColor("red");
                    else if(p1Blue.isVisible()) players.get(0).setPawnColor("blue");
                    else if(p1Green.isVisible()) players.get(0).setPawnColor("green");
                    else if(p1Yellow.isVisible()) players.get(0).setPawnColor("yellow");

                    players.get(1).setPlayerName(name2.getText());
                    players.get(1).setPlayerOrder(2);
                    if(p2Red.isVisible()) players.get(1).setPawnColor("red");
                    else if(p2Blue.isVisible()) players.get(1).setPawnColor("blue");
                    else if(p2Green.isVisible()) players.get(1).setPawnColor("green");
                    else if(p2Yellow.isVisible()) players.get(1).setPawnColor("yellow");
                }

                createBoard(frame, dim, board, name1.getText());
            }
        });
        frame.getContentPane().add(button);
    }

    public void createBoard(JFrame frame, Dimension dim, Board board, String name){
        cleanFrame(frame);

        JLabel b=new JLabel();
        if(dim.height<1000){
            b.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/tabuleiro.png"))));
            b.setBounds((dim.width-612)/2, (dim.height-612)/2-30, 612, 612);
        }
        else{
            b.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/tabuleiro.png"))));
            b.setBounds(504, 70, 912, 912);   
        } 
        frame.getContentPane().add(b);

        JLabel wall=new JLabel("Walls");
        wall.setFont(new Font(Font.SANS_SERIF,Font.BOLD,40));
        wall.setBounds(1100,20,300,100);
        wall.setForeground(Color.black);
        frame.getContentPane().add(wall);

        JLabel wallLeft=new JLabel("Walls Left:");
        wallLeft.setFont(new Font(Font.SANS_SERIF,Font.BOLD,30));
        wallLeft.setBounds(1080,250,400,100);
        wallLeft.setForeground(Color.black);
        frame.getContentPane().add(wallLeft);

        JLabel wallCount=new JLabel();
        if(board.getPlayers().size()==4) wallCount.setText("5");
        else wallCount.setText("10");
        wallCount.setFont(new Font(Font.SANS_SERIF,Font.BOLD,30));
        wallCount.setBounds(1260,277,50,50);
        wallCount.setForeground(Color.black);
        frame.getContentPane().add(wallCount);

        JLabel playerTurn=new JLabel("Player Turn");
        playerTurn.setFont(new Font(Font.SANS_SERIF,Font.BOLD,40));
        playerTurn.setBounds(70,20,400,100);
        playerTurn.setForeground(Color.black);
        frame.getContentPane().add(playerTurn);

        JLabel playerColor=new JLabel();
        switch (board.getCurrentPlayer().getPawnColor()) {
            case "blue" ->
                    playerColor.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/BluePawnStart.png"))));
            case "red" ->
                    playerColor.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/RedPawnStart.png"))));
            case "green" ->
                    playerColor.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/GreenPawnStart.png"))));
            case "yellow" ->
                    playerColor.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/YellowPawnStart.png"))));
        }

        playerColor.setBounds(100,120,150,150);
        frame.getContentPane().add(playerColor);

        JLabel playerName=new JLabel(name);
        playerName.setFont(new Font(Font.SANS_SERIF,Font.BOLD,40));
        playerName.setBounds(100,250,600,100);
        playerName.setForeground(Color.black);
        frame.getContentPane().add(playerName);

        addInitialMovableWalls(frame, dim, board,wallCount,playerColor,playerName);

        printAllPawns(b, wallCount, playerColor, playerName, frame, board, dim);
    }

    public void startGame(JFrame frame, Dimension dim){
        cleanFrame(frame);
        addBackArrow(frame,dim);

        Board board=new Board();
        selectPlayers(frame, dim, board);
    }

    public void gameRules(JFrame frame, Dimension dim) {
        cleanFrame(frame);
        addBackArrow(frame,dim);

        JLabel rules=new JLabel();
        rules.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Laptop/rules.png"))));
        rules.setBounds(10,10,1300,750);
        frame.getContentPane().add(rules);
    }

    public void gameOver(JFrame frame, Board board, Dimension dim){
        frame.setVisible(false);

        JFrame f = new JFrame("Quoridor");
        if((dim.width!=1920 && dim.height!=1080) && (dim.width!=1366 && dim.height!=768)) dim.setSize(1351,780);
        f.setSize(dim.width, dim.height);
        f.getContentPane().setBackground(Color.decode("#AF4933"));
        f.setLayout(null);
        f.setAlwaysOnTop(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);


        JLabel congrats=new JLabel("CONGRATULATIONS!");
        congrats.setFont(new Font(Font.SANS_SERIF,Font.BOLD,100));
        congrats.setBounds(100,50,1300,100);
        congrats.setForeground(Color.black);
        f.getContentPane().add(congrats);

        JLabel pawn=new JLabel();
        pawn.setBounds(dim.width/2-100,200,200,200);
        f.getContentPane().add(pawn);

        JLabel label=new JLabel();
        label.setFont(new Font(Font.SANS_SERIF,Font.BOLD,100));
        label.setForeground(Color.black);
        f.getContentPane().add(label);

        switch (board.getCurrentPlayer().getPawnColor()) {
            case "red" -> {
                pawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/RedPawnStart.png"))));
                label.setBounds(dim.width/2-270,450,1000,100);
                label.setText("RED WINS");
            }
            case "blue" -> {
                pawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/BluePawnStart.png"))));
                label.setBounds(dim.width/2-300,450,1000,100);
                label.setText("BLUE WINS");
            }
            case "green" -> {
                pawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/GreenPawnStart.png"))));
                label.setBounds(dim.width/2-350,450,1000,100);
                label.setText("GREEN WINS");
            }
            case "yellow" -> {
                pawn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Imagens/Desktop/YellowPawnStart.png"))));
                label.setBounds(dim.width/2-400,450,1000,100);
                label.setText("YELLOW WINS");
            }
        }

        JButton button=new JButton("MENU");
        button.setBounds(dim.width/2-100,dim.height-150,200,50);
        button.addActionListener(e ->{
            cleanFrame(f);
            menu(f,dim);
        });
        f.getContentPane().add(button);
    }
}
