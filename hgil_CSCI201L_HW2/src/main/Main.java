package main;

import game_logic.GamePlay;

public class Main {

	public static void main(String gameFile, String[] teamNames, int numTeams) {

		new GamePlay(gameFile, teamNames, numTeams);
	}
}
