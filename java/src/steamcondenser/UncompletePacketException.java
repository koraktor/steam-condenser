package steamcondenser;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class UncompletePacketException extends SteamCondenserException
{
	private static final long serialVersionUID = 5524570166078828173L;
	
	public UncompletePacketException()
	{
		super("Packet is missing a part of data.");
	}
}
