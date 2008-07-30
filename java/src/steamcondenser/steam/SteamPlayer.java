package steamcondenser.steam;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class SteamPlayer
{
	private float connectTime;
	
	private int id;
	
	private String name;
	
	private int score;
	
	public SteamPlayer(int id, String name, int score, float connectTime)
	{
		this.connectTime = connectTime;
		this.id = id;
		this.name = name;
		this.score = score;
	}
	
	public String toString()
	{
		return "#" + this.id + " \"" + this.name + "\" " + this.score + " " + this.connectTime;
	}
}
