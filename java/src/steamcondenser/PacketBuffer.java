package steamcondenser;

import java.nio.ByteBuffer;

public class PacketBuffer
{
	private ByteBuffer byteBuffer;
	
	public PacketBuffer(byte[] data)
	{
		this.byteBuffer = ByteBuffer.wrap(data);
	}
	
	public byte getByte()
	{
		return (byte) this.byteBuffer.getChar();
	}
	
	public short getShort()
	{
		return this.byteBuffer.getShort();
	}
	
	public String getString()
	{
		byte[] remainingBytes = new byte[byteBuffer.capacity() - byteBuffer.arrayOffset()];
		this.byteBuffer.get(remainingBytes);
		int stringEnd = remainingBytes.toString().indexOf("\0");
		
		return remainingBytes.toString().substring(0, stringEnd);
	}
	
	public boolean hasRemaining()
	{
		return this.byteBuffer.hasRemaining();
	}
}
