

import java.util.ArrayList;

/** Model representing an {@link Asset} section or sub-section */
public class Section
{
	/**	Name of the section */
	protected String _name;
	/**	Sub-section list */
	protected ArrayList<Section> _subSections;
	/**	Property list */
	protected ArrayList<Property> _properties;
	
	/**
	* Constructor
	* @param tName Name of the section
	*/
	public Section( String tName )
	{
		_name = tName;
	}
	
	/**
	* Gets the {@link Section#_name} of the section
	* @return {@link Section#_name}
	*/
	public String getName()
	{
		return _name;
	}
	
	/**
	* Gets the {@link Section#_subSections} of the section
	* @return {@link Section#_subSections}
	*/
	public ArrayList<Section> getSubSections()
	{
		return _subSections;
	}
	
	/**
	* Adds a sub-section to the section
	* @param tSection Section model
	*/
	public void addSubSection( Section tSection )
	{
		if ( _subSections == null )
		{
			_subSections = new ArrayList<>();
		}
		
		_subSections.add( tSection );
	}
	
	/**
	* Gets the {@link Section#_properties} of the section
	* @return {@link Section#_properties}
	*/
	public ArrayList<Property> getProperties()
	{
		return _properties;
	}
	
	/**
	* Adds a property to the section
	* @param tProperty Property model
	*/
	public void addProperty( Property tProperty )
	{
		if ( _properties == null )
		{
			_properties = new ArrayList<>();
		}
		
		_properties.add( tProperty );
	}
}