package sim;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Panneau extends JPanel
{

    public Panneau() 
    {
        setSize(800,600);
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            g.drawImage(ImageIO.read(new File("C:\\Users\\Houssem\\Desktop\\deadline.png")), 20, 30, this);
        } catch (IOException ex) {
            Logger.getLogger(Panneau.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
