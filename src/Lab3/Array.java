package Lab3;

public class Array
{
	// ��"array(2,array(2,integer))"Ϊ��
	private int length;  // ���ȣ�2
	private Array type;  // �������ͣ�array(2,integer)
	private String baseType;  // �������ͣ�integer
	public int getLength()
	{
		return length;
	}
	
	public Array getType()
	{
		return type;
	}
	
	public String getBaseType()
	{
		return baseType;
	}
	
	public void setLength(int length)
	{
		this.length=length;
	}
	
	public void setType(Array type)
	{
		this.type=type;
	}
	
	public void setBaseType(String baseType)
	{
		this.baseType=baseType;
	}

}
