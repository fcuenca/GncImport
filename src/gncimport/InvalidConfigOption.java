package gncimport;

public class InvalidConfigOption extends RuntimeException
{
	private static final long serialVersionUID = -4796586744681635739L;

	public InvalidConfigOption(String message)
	{
		super(message);
	}
}
