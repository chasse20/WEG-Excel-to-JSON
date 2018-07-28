

/** Model representing a {@link Section} property */
public class Property
{
	/**	Name of the property */
	protected String _name;
	/**	Units of measurement */
	protected String _units;
	/**	Value of the property */
	protected String _value;
	
	/**
	* Constructor
	* @param tName Name of the property
	* @param tUnits Units of measurement
	* @param tValue Value of the property
	*/
	public Property( String tName, String tUnits, String tValue )
	{
		_name = tName;
		_units = tUnits;
		_value = tValue;
	}
	
	/**
	* Gets the {@link Property#_name} of the property
	* @return {@link Property#_name}
	*/
	public String getName()
	{
		return _name;
	}
	
	/**
	* Gets the {@link Property#_units} of the property
	* @return {@link Property#_units}
	*/
	public String getUnits()
	{
		return _units;
	}
	
	/**
	* Gets the {@link Property#_value} of the property
	* @return {@link Property#_value}
	*/
	public String getValue()
	{
		return _value;
	}
}