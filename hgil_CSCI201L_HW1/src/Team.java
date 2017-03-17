//holds team name and points
public class Team
{
	
	//team teamName
	public String teamName;
	
	//team points
	public int points;
	
	//Team default constructor
	public Team()
	{
		teamName = "";
		points = 0;
	}
	
	//Team constructor
	public Team(String enteredName, int enteredPoints) //change name, points are never entered
	{
		this.teamName = enteredName;
		this.points = enteredPoints;
	}
	
	//set team name
	public void setTeamName(String enteredName)
	{
		teamName = enteredName;
	}
	
	//set team points
	public void setPoints(int newPoints)
	{
		points = newPoints;
	}
	
	//get team name
	public String getTeamName()
	{
		return teamName;
	}
	
	//get team points
	public int getPoints()
	{
		return points;
	}
	
	//add points to team's points
	public void addPoints(int pointsToAdd)
	{
		setPoints(points + pointsToAdd);
	}
	
	//subtract points from team's points
	public void subtractPoints(int pointsToSubtract)
	{
		setPoints(points - pointsToSubtract);
	}
		 
}