package game_logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import other_gui.TeamGUIComponents;

public class FinalistsAndEliminatedTeams {

	//a list of the teams that qualified for final jeoapardy
	private List<TeamGUIComponents> finalists;
	//a list of the teams who have been eliminated from the game (will not be in final jeopardy)
	private List<TeamGUIComponents> eliminatedTeams;
	
	public FinalistsAndEliminatedTeams(List<TeamGUIComponents> teams) {
		this.finalists = new ArrayList<>();
		this.eliminatedTeams = new ArrayList<>();
		
		for (int i = 0; i<teams.size(); i++){
			
			TeamGUIComponents team = teams.get(i);
			
			//sort the team into the appropriate list: finalist, or loser
			if (team.getPoints() > 0){
				finalists.add(team);
			}
			
			else{
				eliminatedTeams.add(team);
			}
		}
	}
	
	public List<TeamGUIComponents> getFinalists(){
		return finalists;
	}
	
	public List<TeamGUIComponents> getEliminatedTeams(){
		return eliminatedTeams;
	}

	//returns a list of the winning teams
	//note that the return type is and ArrayList<TeamData> NOT ArrayList<TeamGUIComponents>
	//this is because the WinnersGUI only needs to access the team name, it doesn't need the GUI information
	// yay abstraction!!
	public ArrayList<TeamData> getWinners(){
	
		ArrayList<TeamData> winners = new ArrayList<>();
		
		if (finalists.size() != 0){
			//sorts the finalists in order of their total score
			Collections.sort(finalists, TeamData.getComparator());
			//the team at the end of the list must have the highest score and is definitely a winner
			TeamData definiteWinner = finalists.get(finalists.size() - 1);
			long max = definiteWinner.getPoints();
			//if the max score is 0, we know that no one won the game
			if (max == 0) return winners;
			
			winners.add(definiteWinner);
			
			//check to see if there are other winners
			if (finalists.size() > 1){
				
				for (int i = finalists.size() -2; i > -1; i--){
			
					//if this team has the same score as the max, it must also be a winner
					if (finalists.get(i).getPoints() == max) winners.add(finalists.get(i));
				}	
			}
		}
		
		return winners;
	}

}
