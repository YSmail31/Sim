package sim;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.List;
import java.util.Arrays;
import javax.imageio.*;
import javax.swing.*;

public class ScreenImage
{
	private static List<String> types = Arrays.asList( ImageIO.getWriterFileSuffixes() );
	public static BufferedImage createImage(JComponent component)
	{
		Dimension d = component.getSize();

		if (d.width == 0 || d.height == 0)
		{
			d = component.getPreferredSize();
			component.setSize( d );
		}
		Rectangle region = new Rectangle(0, 0, d.width, d.height);
		return ScreenImage.createImage(component, region);
	}
	public static BufferedImage createImage(JComponent component, Rectangle region)
	{
		if (! component.isDisplayable())
		{
			Dimension d = component.getSize();

			if (d.width == 0 || d.height == 0)
			{
				d = component.getPreferredSize();
				component.setSize( d );
			}
			layoutComponent( component );
		}

		BufferedImage image = new BufferedImage(region.width, region.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();

		//  Paint a background for non-opaque components,
		//  otherwise the background will be black

		if (! component.isOpaque())
		{
			g2d.setColor( component.getBackground() );
			g2d.fillRect(region.x, region.y, region.width, region.height);
		}

		g2d.translate(-region.x, -region.y);
		component.paint( g2d );
		g2d.dispose();
		return image;
	}
	public static BufferedImage createDesktopImage()
		throws AWTException, IOException
	{
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle region = new Rectangle(0, 0, d.width, d.height);
		return ScreenImage.createImage(region);
	}
	public static BufferedImage createImage(Component component)
		throws AWTException
	{
		Point p = new Point(0, 0);
		SwingUtilities.convertPointToScreen(p, component);
		Rectangle region = component.getBounds();
		region.x = p.x;
		region.y = p.y;
		return ScreenImage.createImage(region);
	}
	public static BufferedImage createImage(Rectangle region)
		throws AWTException
	{
		BufferedImage image = new Robot().createScreenCapture( region );
		return image;
	}
	public static void writeImage(BufferedImage image, String fileName)
		throws IOException
	{
		if (fileName == null) return;

		int offset = fileName.lastIndexOf( "." );

		if (offset == -1)
		{
			String message = "file suffix was not specified";
			throw new IOException( message );
		}

		String type = fileName.substring(offset + 1);

		if (types.contains(type))
		{
			ImageIO.write(image, type, new File( fileName ));
		}
		else
		{
			String message = "unknown writer file suffix (" + type + ")";
			throw new IOException( message );
		}
	}
	static void layoutComponent(Component component)
	{
		synchronized (component.getTreeLock())
		{
			component.doLayout();

    	    if (component instanceof Container)
        	{
            	for (Component child : ((Container)component).getComponents())
	            {
    	            layoutComponent(child);
        	    }
	        }
    	}
    }
}