package game_GUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import game_logic.GameData;
import game_logic.GamePlay;
import game_logic.QuestionAnswer;

public class QuestionCards extends JButton {

	int categoryIndex;
	int valueIndex;

	public QuestionCards(Container pane, String buttonLabel, int c, int v) {
		setText(buttonLabel);
		setBackground(InitializeComponents.darkGray);
		setOpaque(true);
		setBorderPainted(false);
		setForeground(Color.white);
		setHorizontalAlignment(SwingConstants.CENTER);
		setFont(new Font("Serif", Font.BOLD, 16));
		setBorder(new EmptyBorder(50, 78, 50, 79));
		pane.add(this);
		this.categoryIndex = c;
		this.valueIndex = v;

		addActionListener(selected);
	}

	public void setcategoryIndex(int index) {
		categoryIndex = index;
	}

	public void setvalueIndex(int index) {
		valueIndex = index;
	}

	public int getcategoryIndex() {
		return categoryIndex;
	}

	public int getvalueIndex() {
		return valueIndex;
	}

	private ActionListener selected = new ActionListener() {

		public void actionPerformed(ActionEvent e) {

			System.out.println("Cat " + getcategoryIndex());
			System.out.println("Val" + getvalueIndex());

			QuestionAnswer thisQuestion = GameData.getQA(getcategoryIndex(), getvalueIndex());

			GameBoard.setCurrentQuestion(getcategoryIndex(), getvalueIndex());

			thisQuestion.setHasBeenAsked();

			setEnabled(false);
			GameBoard.updateGameProgressInfo(GamePlay.getTeamData(GamePlay.getWhoseTurn()).getTeamName() + " chose "
					+ GameBoard.getCategory(getcategoryIndex()) + " for " + GameData.getPointValue(getvalueIndex()));
			GameBoard.setTheQuestion(thisQuestion.getQuestion());
			GameBoard.setDisplay(2);
		}
	};

}
