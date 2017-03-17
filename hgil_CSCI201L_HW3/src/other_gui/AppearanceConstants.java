package other_gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;

public class AppearanceConstants
{

	//colors, fonts, ect that can be statically referenced by other classes
	private static final ImageIcon exitIconLarge = new ImageIcon("images/question_mark.jpg");
	private static final Image exitImage = exitIconLarge.getImage();
	private static final Image exitImageScaled = exitImage.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);

	public static final ImageIcon exitIcon = new ImageIcon(exitImageScaled);

	public static final Color darkBlue = new Color(0, 0, 139);
	public static final Color lightBlue = new Color(135, 206, 250);
	public static final Color mediumGray = new Color(100, 100, 100);

	public static final Font fontSmallest = new Font("Palatino", Font.BOLD, 16);
	public static final Font fontSmall = new Font("Palatino", Font.BOLD, 20);
	public static final Font fontMedium = new Font("Palatino", Font.BOLD, 25);
	public static final Font fontLarge = new Font("Palatino", Font.BOLD, 40);
}
