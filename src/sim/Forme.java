/*c'est une classe qui se charge de formes utiliser dans la simulation
 *  Forme(26,6,Color.BLACK) une tache en execution
 *  Forme(26,1,Color.BLACK) une tache au repos 
 *  Forme(3,1,Color.BLACK,Forme.DEADLINE) temps de l'echence
 *  Forme(3,1,Color.BLACK,Forme.PERIODE) periode
 *  Forme(3,6,Color.BLACK) séparateur entre unité de temps
 */
package sim;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Forme 
{
	private int larg,haut;
	private Color coul;
        private boolean isPicture;
        private Image picture;
        public static final File DEADLINE=new File("deadline.png"),PERIODE=new File("periode.png");
	public Forme(int larg, int haut, Color coul) {
		this.larg = larg;
		this.haut = haut;
		this.coul = coul;
                isPicture=false;
	}
        public Forme(int larg, int haut, Color coul,File f) throws IOException {
            picture=ImageIO.read(f);
            this.larg = larg;
            this.haut = haut;
            this.coul = coul;
            isPicture=true;
        }

    public Image getPicture() {
        return picture;
    }
	public int getLarg() {
		return larg;
	}
	public void setLarg(int larg) {
		this.larg = larg;
	}
	public int getHaut() {
		return haut;
	}
	public void setHaut(int haut) {
		this.haut = haut;
	}
	public Color getCoul() {
		return coul;
	}
	public void setCoul(Color coul) {
		this.coul = coul;
	}
        public boolean isPicture()
        {
            return isPicture;
        }
                
}
