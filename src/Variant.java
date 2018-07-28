

/** Model representing an {@link Asset} variant */
public class Variant
{
	/**	Name of the variant */
	protected String _name;
	/**	Notes */
	protected String _notes;
	
	/**
	* Constructor
	* @param tName Name of the variant
	* @param tNotes Notes of the variant
	*/
	public Variant( String tName, String tNotes )
	{
		_name = tName;
		_notes = tNotes;
	}
	
	/**
	* Gets the {@link Variant#_name} of the variant
	* @return {@link Variant#_name}
	*/
	public String getName()
	{
		return _name;
	}
	
	/**
	* Gets the {@link Variant#_notes} of the variant
	* @return {@link Variant#_notes}
	*/
	public String getNotes()
	{
		return _notes;
	}
}