package steamcondenser;

import java.nio.ByteBuffer;

public class PacketBuffer
{
	private ByteBuffer byteBuffer;
	
	public PacketBuffer(byte[] data)
	{
		this.byteBuffer = ByteBuffer.wrap(data);
	}
	
	public byte[] array()
	{
		return this.byteBuffer.array();
	}
	
	public byte getByte()
	{
		return this.byteBuffer.get();
	}
	
	public int getInt()
	{
		return this.byteBuffer.getInt();
	}
	
	public int getLength()
	{
		return this.byteBuffer.capacity();
	}
	
	public short getShort()
	{
		return this.byteBuffer.getShort();
	}
	
	public String getString()
	{
		byte[] remainingBytes = new byte[byteBuffer.remaining()];
		this.byteBuffer.get(remainingBytes);
		String dataString = new String(remainingBytes);
		
		return dataString.substring(0, dataString.indexOf("\0"));
	}
	
	public boolean hasRemaining()
	{
		return this.byteBuffer.hasRemaining();
	}
}
