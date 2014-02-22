package gncimport.utils;

public class ProgrammerError extends RuntimeException
{
	private static final long serialVersionUID = 5564604532752130574L;

	public ProgrammerError(String message)
	{
		super(message);
	}
}
