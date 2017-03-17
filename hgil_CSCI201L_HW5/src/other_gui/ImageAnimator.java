package other_gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ImageAnimator extends JPanel
{
	private static final long serialVersionUID = 10;
	ImageIcon images[];
	int totalImages, animationDelay, currentImageIndex = 0;
	Image currentImage;
	String imageOf;
	Timer animationTimer;

	public ImageAnimator(int totalImages, int secondsToAnimate, String imageOf)
	{
		this.totalImages = totalImages;
		this.imageOf = imageOf;

		this.animationDelay = (secondsToAnimate * 1000) / totalImages;

		images = new ImageIcon[totalImages];
		for (int frame = 0; frame < images.length; ++frame)
		{
			if (imageOf.equals("Clock"))
			{
				images[frame] = new ImageIcon("images/clockAnimation/frame_" + frame + "_delay-0.06s.gif");
			}
			if (imageOf.equals("Fish"))
			{
				images[frame] = new ImageIcon("images/fishAnimation/frame_" + frame + "_delay-0.1s.gif");
			}
		}

		currentImage = images[currentImageIndex].getImage();
		prepareImage(currentImage, this);
		setDoubleBuffered(true); //not sure what this does
		setOpaque(false);

		animationTimer = new Timer(animationDelay, paintTimer);

		startAnimation();
	}

	public void startAnimation()
	{
		animationTimer.start();
	}

	public void stopAnimation()
	{
		animationTimer.stop();
	}

	public void restartAnimation()
	{
		stopAnimation();
		currentImageIndex = 0;
		startAnimation();
	}

	public void setSecondsToAnimate(int secondsToAnimate)
	{
		this.animationDelay = (secondsToAnimate * 1000) / totalImages;
		animationTimer.setDelay(animationDelay);
		restartAnimation();
	}

	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		//centers the image in the JPanel
		int xPos = (this.getWidth() - images[0].getIconWidth()) / 2;
		int yPos = (this.getHeight() - images[0].getIconHeight()) / 2;

		g2d.drawImage(currentImage, xPos, yPos, this);
		g.dispose();
	}

	Action paintTimer = new AbstractAction() {
		private static final long serialVersionUID = 11;

		public void actionPerformed(ActionEvent e)
		{
			currentImageIndex = (currentImageIndex + 1) % totalImages;
			currentImage = images[currentImageIndex].getImage();
			repaint();
		}
	};
}