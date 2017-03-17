package game_GUI;

import java.awt.Dimension;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BetSlider extends JSlider {
	int bet;
	int row;

	public BetSlider(int sliderValue, int row, long max) {
		setMinimum(0);
		setMaximum((int) max);
		setMajorTickSpacing((int) max / 10);
		setMinorTickSpacing((int) max / 100);
		setOpaque(false);
		setPreferredSize(new Dimension(600, 40));
		setMaximumSize(new Dimension(600, 40));
		setPaintTicks(true);
		setPaintLabels(true);
		bet = sliderValue;
		this.row = row;
	}

	private ChangeListener sliderChange = new ChangeListener() {

		@Override
		public void stateChanged(ChangeEvent e) {
			GameBoard.updateBetLabels(Integer.toString(getValue()), row);
		}

	};
}
