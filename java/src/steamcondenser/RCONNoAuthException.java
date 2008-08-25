package steamcondenser;

public class RCONNoAuthException extends SteamCondenserException
{
	private static final long serialVersionUID = 6303597320774324157L;

	public RCONNoAuthException()
	{
		super("Not authenticated yet.");
	}
}
