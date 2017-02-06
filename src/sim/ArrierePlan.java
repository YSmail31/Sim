package sim;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ArrierePlan extends JPanel{
    private BufferedImage image;
    public ArrierePlan() throws IOException {
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        image =ImageIO.read(new File("9.jpg"));
        
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
    
}
