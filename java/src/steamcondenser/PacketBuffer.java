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
	
	public float getFloat()
	{
		return this.byteBuffer.getFloat();
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
		byte[] remainingBytes = new byte[this.byteBuffer.remaining()];
		this.byteBuffer.slice().get(remainingBytes);
		String dataString = new String(remainingBytes);
		int stringEnd = dataString.indexOf(0);
		dataString = dataString.substring(0, stringEnd);
		
		// Setting new position by byte length of the string for compatibility with multi-byte characters
		this.byteBuffer.position(this.byteBuffer.position() + dataString.getBytes().length + 1);
		
		return dataString;
	}
	
	public int remaining()
	{
		return this.byteBuffer.remaining();
	}
	
	public boolean hasRemaining()
	{
		return this.byteBuffer.hasRemaining();
	}
}
