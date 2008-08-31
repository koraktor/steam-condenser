package steamcondenser;

public class NothingReceivedException extends SteamCondenserException
{
	private static final long serialVersionUID = 4147705099294124283L;

	public NothingReceivedException()
	{
		super("No data received.");
	}
}
