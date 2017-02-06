package sim;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Graph extends JPanel
{
    private ArrayList<Forme> formeSet[];
    private int hp;
    private ArrayList<Task> ts;
    private String type;
    public Graph(){
	hp=0;
	type="";
	ts=new ArrayList<>();
	formeSet=new ArrayList[0];
    }
    public ArrayList<Forme>[] getForme() {
	return formeSet;
    }
    public void setForme(ArrayList<Forme>[] forme,int h,ArrayList<Task> tasks,String t) {
	this.formeSet = forme;
	hp=h;
	ts=tasks;
        type=t;
	setPreferredSize(new Dimension( (hp+1)*4+hp*26+50,150+60*ts.size()-50));
        setSize(new Dimension( (hp+1)*4+hp*26+50,150+60*ts.size()-50));
        getImage();
    }
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, (hp+1)*4+hp*26+50, 150+60*ts.size()-50);
        //Dessiner la Legende
        g.setFont(new java.awt.Font("Calibri", 1, 14));
	g.drawString(type, 55, 13);
	g.setColor(Color.GREEN);
	g.fillRect(58, 30, 4, 16);
	g.setColor(Color.BLACK);
	g.drawString("Date d'activation", 67, 44);
        g.drawString("Deadline", 218, 44);
	g.drawString("Pèriode", 308, 44);
	g.fillRect(390, 37, 26, 6);
	g.drawString("Unité de calcul", 418, 44);
	g.drawRect(56, 16, 480, 32);
        try {
            g.drawImage(ImageIO.read(Forme.DEADLINE), 206, 19, this);
            g.drawImage(ImageIO.read(Forme.PERIODE), 296, 19, this);
        } catch (IOException ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Dessiner l'axe de temps
	int x=58;
	int y=70;
        if(Math.ceil(hp/5.0)!=0)
            for(int i=0;i<=Math.ceil(hp/5.0);i++)
            {
                g.drawString(i*5+"", x, y);
                x+=5*29;
            }
        x=60;
	y=80;
	for(int i=0;i<hp;i++)
	{
            g.fillRect(x,y,3,16);
            x+=3;
            g.fillRect(x,y+8-1,26,2);
            x+=26;
	}
        if(hp!=0)
        g.fillRect(x,y,3,16);
        //Dessiner les info de chaque processus+processeur
        x=60;
	for(int i=0;i<ts.size();i++)
	{
              g.drawString(ts.get(i).toString(), x, i*60+150);
        }
        //Dessiner le Diagramme de chaque Processus
	int i=1;
	for(ArrayList<Forme> f:formeSet)
	{
            x=60;
            y=i*60+60;
            for(Forme forme:f)
            {
		g.setColor(forme.getCoul());
		g.fillRect(x, y+8-forme.getHaut()/2, forme.getLarg(), forme.getHaut());
                if(forme.isPicture())
                    g.drawImage(forme.getPicture(), x-4, y+8-27, this);
		x+=forme.getLarg();
            }
            i++;
	}
        }
    void getImage()
    {
        ScreenImage.layoutComponent(this);
        BufferedImage bi=ScreenImage.createImage( this );
        try {
            ScreenImage.writeImage(bi, "result.jpg");
        } catch (IOException ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
