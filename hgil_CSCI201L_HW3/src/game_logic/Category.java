package game_logic;

import java.awt.Color;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import other_gui.AppearanceConstants;
import other_gui.AppearanceSettings;

public class Category
{

	//The category name
	private String category;
	//The category's index in ordering the categories
	private int index;
	//The label displayed on the MainGUI above the game board
	private JLabel categoryLabel;
	private Image categoryBackgroundImage;

	public Category(String category, int index, Image categoryBackgroundImage)
	{
		this.category = category;
		this.index = index;
		this.categoryBackgroundImage = categoryBackgroundImage;

		ImageIcon icon = new ImageIcon(categoryBackgroundImage);
		categoryLabel = new JLabel(icon);
		categoryLabel.setText(category);
		categoryLabel.setHorizontalTextPosition(JLabel.CENTER);
		categoryLabel.setVerticalTextPosition(JLabel.CENTER);
		categoryLabel.setFont(AppearanceConstants.fontMedium);
		categoryLabel.setForeground(Color.lightGray);
		categoryLabel.setOpaque(true);
		AppearanceSettings.setTextAlignment(categoryLabel);
		categoryLabel.setBorder(BorderFactory.createLineBorder(AppearanceConstants.darkBlue));
	}

	public int getIndex()
	{
		return index;
	}

	public JLabel getCategoryLabel()
	{
		return categoryLabel;
	}
}
