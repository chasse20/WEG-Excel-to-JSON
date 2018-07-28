

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

/** File writer for individual {@link Asset}s */
public class Writer
{
	/** Absolute file path for writing */
	protected String path;
	
	/**
	* Constructor
	* @param tPath Absolute file path for writing
	*/
	public Writer( String tPath )
	{
		path = tPath;
	}
	
	/**
	* Attempts to convert an {@link Asset} into a JSON file at the given {@link Writer#path}
	* @param tAsset Asset to write
	* @throws Exception If the file stream or JSON writer streams could not be opened
	*/
	public void write( Asset tAsset ) throws Exception
	{
		// Open file stream
		FileWriter tempWriter;
		try
		{
			tempWriter = new FileWriter( path );
		}
		catch ( IOException tError )
		{
			throw tError;
		}
		
		// Setup JSON writer
		HashMap<String,Object> tempSettings = new HashMap<>();
		tempSettings.put( JsonGenerator.PRETTY_PRINTING, true );
		JsonWriterFactory tempFactory = Json.createWriterFactory( tempSettings );
		
		// Write JSON to string stream
		StringWriter tempStringWriter = new StringWriter();
		JsonWriter tempJSONWriter;
		try
		{
			tempJSONWriter = tempFactory.createWriter( tempStringWriter );
		}
		catch ( Exception tError )
		{
			throw tError;
		}
		
		tempJSONWriter.writeObject( writeAsset( tAsset ).build() );
		tempJSONWriter.close();
		tempWriter.write( tempStringWriter.toString() );
		
		// Close
		tempWriter.close();
	}
	
	/**
	* Builds a JSON object from a given {@link Asset}
	* @param tAsset Asset to build a JSON
	* @return JSON representation of the asset
	*/
	protected JsonObjectBuilder writeAsset( Asset tAsset )
	{
		JsonObjectBuilder tempBuilder = Json.createObjectBuilder();
		
		// Notes
		if ( tAsset.notes != null && !tAsset.notes.isEmpty() )
		{
			tempBuilder.add( "notes", tAsset.notes );
		}
		
		// Sections
		ArrayList<Section> tempSections = tAsset.getSections();
		if ( tempSections != null )
		{
			JsonArrayBuilder tempSectionArray = Json.createArrayBuilder();
		
			int tempListLength = tempSections.size();
			for ( int i = 0; i < tempListLength; ++i )
			{
				tempSectionArray.add( writeSection( tempSections.get( i ) ) );
			}
			
			tempBuilder.add( "sections", tempSectionArray );
		}
			
		// Variants
		ArrayList<Variant> tempVariants = tAsset.getVariants();
		if ( tempVariants != null )
		{
			JsonArrayBuilder tempVariantArray = Json.createArrayBuilder();
		
			int tempListLength = tempVariants.size();
			for ( int i = 0; i < tempListLength; ++i )
			{
				tempVariantArray.add( writeVariant( tempVariants.get( i ) ) );
			}
			
			tempBuilder.add( "variants", tempVariantArray );
		}
		
		return tempBuilder;
	}
	
	/**
	* Builds a JSON object from a given {@link Section}
	* @param tSection Section to build a JSON
	* @return JSON representation of the section
	*/
	protected JsonObjectBuilder writeSection( Section tSection )
	{
		JsonObjectBuilder tempBuilder = Json.createObjectBuilder();
		
		// Name
		tempBuilder.add( "name", tSection.getName() );
		
		// Properties
		ArrayList<Property> tempProperties = tSection.getProperties();
		if ( tempProperties != null )
		{
			JsonArrayBuilder tempPropertyArray = Json.createArrayBuilder();
		
			int tempListLength = tempProperties.size();
			for ( int i = 0; i < tempListLength; ++i )
			{
				tempPropertyArray.add( writeProperty( tempProperties.get( i ) ) );
			}
			
			tempBuilder.add( "properties", tempPropertyArray );
		}
		
		// Sub-sections
		ArrayList<Section> tempSubSections = tSection.getSubSections();
		if ( tempSubSections != null )
		{
			JsonArrayBuilder tempSubSectionArray = Json.createArrayBuilder();
		
			int tempListLength = tempSubSections.size();
			for ( int i = 0; i < tempListLength; ++i )
			{
				tempSubSectionArray.add( writeSection( tempSubSections.get( i ) ) );
			}
			
			tempBuilder.add( "sections", tempSubSectionArray );
		}
		
		return tempBuilder;
	}
	
	/**
	* Builds a JSON object from a given {@link Property}
	* @param tProperty Property to build a JSON
	* @return JSON representation of the property
	*/
	protected JsonObjectBuilder writeProperty( Property tProperty )
	{
		JsonObjectBuilder tempBuilder = Json.createObjectBuilder();
		
		tempBuilder.add( "name", tProperty.getName() );
		
		String tempUnits = tProperty.getUnits();
		if ( tempUnits != null && !tempUnits.isEmpty() )
		{
			tempBuilder.add( "units", tProperty.getUnits() );
		}
		tempBuilder.add( "value", tProperty.getValue() );
		
		return tempBuilder;
	}
	
	/**
	* Builds a JSON object from a given {@link Variant}
	* @param tVariant Variant to build a JSON
	* @return JSON representation of the variant
	*/
	protected JsonObjectBuilder writeVariant( Variant tVariant )
	{
		JsonObjectBuilder tempBuilder = Json.createObjectBuilder();
		
		tempBuilder.add( "name", tVariant.getName() );
		tempBuilder.add( "notes", tVariant.getNotes() );
		
		return tempBuilder;
	}
}