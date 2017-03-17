package game_GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class InitializeComponents {

	public static Color lightBlue = new Color(131, 205, 252);
	public static Color darkBlue = new Color(1, 0, 144);
	public static Color lightGray = new Color(128, 128, 128);
	public static Color darkGray = new Color(64, 64, 64);

	protected static JTextField addATextField(Container container) {
		JTextField textField = new JTextField();
		container.add(textField);
		return textField;
	}

	protected static JTextField addATextField(Container container, Color bgColor) {
		JTextField textField = new JTextField();
		textField.setOpaque(true);
		textField.setBackground(bgColor);
		textField.setAlignmentX(Component.CENTER_ALIGNMENT);
		container.add(textField);
		return textField;
	}

	protected static JButton addAButton(String text, Container container) {
		JButton button = new JButton(text);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setBackground(darkGray);
		button.setOpaque(true);
		button.setBorderPainted(false);
		button.setForeground(Color.white);
		container.add(button);
		return button;
	}

	protected static JLabel addALabel(String text, Container container) {
		JLabel label = new JLabel(text);
		label.setOpaque(false);
		label.setForeground(Color.white);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		container.add(label);
		return label;
	}

	protected static JLabel addALabel(String text, Container container, Color bgColor) {
		JLabel label = new JLabel(text);
		label.setOpaque(true);
		label.setBackground(bgColor);
		if (bgColor.equals(lightBlue)) {
			label.setForeground(Color.black);
		} else {
			label.setForeground(Color.white);
		}
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		container.add(label);
		return label;
	}

	protected static JPanel addAPanel(Container container) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		container.add(panel);

		return panel;
	}

	protected static JPanel addAPanel(Container container, Color bgColor) {
		JPanel panel = new JPanel();
		panel.setOpaque(true);
		panel.setBackground(bgColor);
		container.add(panel);

		return panel;
	}

	protected static JSlider addSlider(int numTeams, Container container) {

		JSlider slider = new JSlider(1, numTeams, 1);
		slider.setOpaque(true);
		slider.setBackground(darkGray);
		slider.setForeground(Color.white);

		slider.setMajorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setSnapToTicks(true);
		container.add(slider);
		return slider;
	}
}