

import java.util.ArrayList;

/** Model representing a WEG asset */
public class Asset
{
	/**	Name of the asset */
	protected String _name;
	/**	Notes */
	public String notes;
	/**	Section list */
	protected ArrayList<Section> _sections;
	/**	Variant list */
	protected ArrayList<Variant> _variants;
	
	/**
	* Constructor
	* @param tName Name of the asset
	*/
	public Asset( String tName )
	{
		_name = tName;
	}
	
	/**
	* Gets the {@link Asset#_name} of the asset
	* @return {@link Asset#_name}
	*/
	public String getName()
	{
		return _name;
	}
	
	/**
	* Gets the {@link Asset#_sections} of the asset
	* @return {@link Asset#_sections}
	*/
	public ArrayList<Section> getSections()
	{
		return _sections;
	}
	
	/**
	* Adds a section to the asset
	* @param tSection Section model
	*/
	public void addSection( Section tSection )
	{
		if ( _sections == null )
		{
			_sections = new ArrayList<>();
		}
		
		_sections.add( tSection );
	}
	
	/**
	* Gets the {@link Asset#_variants} of the asset
	* @return {@link Asset#_variants}
	*/
	public ArrayList<Variant> getVariants()
	{
		return _variants;
	}
	
	/**
	* Adds a variant to the asset
	* @param tVariant Variant model
	*/
	public void addVariant( Variant tVariant )
	{
		if ( _variants == null )
		{
			_variants = new ArrayList<>();
		}
		
		_variants.add( tVariant );
	}
}