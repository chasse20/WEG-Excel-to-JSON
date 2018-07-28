

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/** File reader for a formatted WEG Excel file */
public class Reader
{
	/** Expected index of the asset name column */
	public static int COLUMN_ASSET = 2;
	/** Expected index of the section name column */
	public static int COLUMN_SECTION = 3;
	/** Expected index of the sub-section name column */
	public static int COLUMN_SUB_SECTION = 4;
	/** Expected index of the property name column */
	public static int COLUMN_PROPERTY = 6;
	/** Expected index of the property units column */
	public static int COLUMN_UNITS = 7;
	/** Expected index of the property value or variant name column */
	public static int COLUMN_VALUE = 8;
	/** Expected index of the variant notes column */
	public static int COLUMN_VARIANT = 9;
	/** Expected index of the marked-as-reviewed column */
	public static int COLUMN_REVIEWED = 10;
	/** Cached worksheet of the Excel document */
	protected XSSFSheet sheet;
	
	/**
	* Constructor
	* @param tFile File stream of the Excel workbook
	* @throws Exception If the file is not a valid WEG Excel document
	*/
	public Reader( File tFile ) throws Exception
	{
		// Initialize
		XSSFWorkbook tempWorkbook;
		try
		{
			tempWorkbook = new XSSFWorkbook( tFile );
		}
		catch ( IOException tError )
		{
			throw tError;
		}
		
		sheet = tempWorkbook.getSheetAt( 0 );
		
		// Validate
		Iterator<Row> tempIterator = sheet.iterator();
		if ( !tempIterator.hasNext() )
		{
			throw new Exception( "Invalid format: no rows found." );
		}
		else if ( ( tempIterator.next().getLastCellNum() - 1 ) < COLUMN_REVIEWED )
		{
			throw new Exception( "Invalid format: incorrect number of columns." );
		}
	}
	
	/**
	* Processes the cached Excel {@link Reader#sheet} into individual {@link Asset} models 
	* @return Array of individual {@link Asset} models
	*/
	public Asset[] read()
	{
		// Read Assets
		Iterator<Row> tempIterator = sheet.iterator();
		HashMap<String,CachedAsset> tempCache = null;
		Row tempRow;
		
		while ( tempIterator.hasNext() )
		{
			tempRow = tempIterator.next();
			if ( !ReadCell( tempRow.getCell( COLUMN_REVIEWED ) ).isEmpty() ) // only process if approved
			{
				String tempAssetName = ReadCell( tempRow.getCell( COLUMN_ASSET ) );
				if ( tempCache == null )
				{
					tempCache = new HashMap<>();
					CachedAsset tempCachedAsset = new CachedAsset();
					tempCachedAsset.asset = new Asset( tempAssetName );
					tempCache.put( tempAssetName, tempCachedAsset );
					readRow( tempRow, tempCachedAsset );
				}
				else if ( tempCache.containsKey( tempAssetName ) )
				{
					readRow( tempRow, tempCache.get( tempAssetName ) );
				}
				else
				{
					CachedAsset tempCachedAsset = new CachedAsset();
					tempCachedAsset.asset = new Asset( tempAssetName );
					tempCache.put( tempAssetName, tempCachedAsset );
					readRow( tempRow, tempCachedAsset );
				}
			}
		}
		
		if ( tempCache != null )
		{
			Asset[] tempAssets = new Asset[ tempCache.size() ];
			
			int i = 0;
			for ( CachedAsset tempAsset : tempCache.values() )
			{
				tempAssets[i] = tempAsset.asset;
				++i;
			}
			
			return tempAssets;
		}
		
		return null;
	}
	
	/**
	* Processes and decomposes an individual row into either notes, variants or sections
	* @param tRow Excel row to process
	* @param tCache {@link CachedAsset} for data linking
	*/
	protected void readRow( Row tRow, CachedAsset tCache )
	{
		String tempSectionName = ReadCell( tRow.getCell( COLUMN_SECTION ) );
		switch ( tempSectionName )
		{
			case "Notes":
				tCache.asset.notes = ReadCell( tRow.getCell( COLUMN_VALUE ) );
				break;
			case "Variants":
				tCache.asset.addVariant( new Variant( ReadCell( tRow.getCell( COLUMN_VALUE ) ), ReadCell( tRow.getCell( COLUMN_VARIANT ) ) ) );
				break;
			default:
				// Section
				if ( tCache.sections == null )
				{
					tCache.sections = new HashMap<>();
					CachedSection tempCachedSection = new CachedSection();
					tempCachedSection.section = new Section( tempSectionName );
					tCache.asset.addSection( tempCachedSection.section );
					tCache.sections.put( tempSectionName, tempCachedSection );
					readSection( tRow, tempCachedSection );
				}
				else if ( tCache.sections.containsKey( tempSectionName ) )
				{
					readSection( tRow, tCache.sections.get( tempSectionName ) );
				}
				else
				{
					CachedSection tempCachedSection = new CachedSection();
					tempCachedSection.section = new Section( tempSectionName );
					tCache.asset.addSection( tempCachedSection.section );
					tCache.sections.put( tempSectionName, tempCachedSection );
					readSection( tRow, tempCachedSection );
				}
				break;
		}
	}
	
	/**
	* Processes and decomposes an individual {@link Section}
	* @param tRow Excel row to process
	* @param tCache {@link CachedSection} for data linking
	*/
	protected void readSection( Row tRow, CachedSection tCache )
	{
		// Property
		String tempSubSectionName = ReadCell( tRow.getCell( COLUMN_SUB_SECTION ) );
		if ( tempSubSectionName.isEmpty() )
		{
			readProperty( tRow, tCache.section );
		}
		// Sub-section
		else
		{
			if ( tCache.subSections == null )
			{
				tCache.subSections = new HashMap<>();
				Section tempSubSection = new Section( tempSubSectionName );
				tCache.section.addSubSection( tempSubSection );
				tCache.subSections.put( tempSubSectionName, tempSubSection );
				readProperty( tRow, tempSubSection );
			}
			else if ( tCache.subSections.containsKey( tempSubSectionName ) )
			{
				readProperty( tRow, tCache.subSections.get( tempSubSectionName ) );
			}
			else
			{
				Section tempSubSection = new Section( tempSubSectionName );
				tCache.section.addSubSection( tempSubSection );
				tCache.subSections.put( tempSubSectionName, tempSubSection );
				readProperty( tRow, tempSubSection );
			}
		}
	}
	
	/**
	* Processes and decomposes an individual {@link Property}
	* @param tRow Excel row to process
	* @param tSection {@link Section} that owns the property
	*/
	protected void readProperty( Row tRow, Section tSection )
	{
		tSection.addProperty( new Property( ReadCell( tRow.getCell( COLUMN_PROPERTY ) ), ReadCell( tRow.getCell( COLUMN_UNITS ) ), ReadCell( tRow.getCell( COLUMN_VALUE ) ) ) );
	}
	
	/**
	* Helper function for parsing a {@link Cell} into a string value
	* @param tCell Excel cell to read
	* @return String value of the given cell
	*/
	protected static String ReadCell( Cell tCell )
	{
		if ( tCell != null )
		{
			CellType tempType = tCell.getCellTypeEnum();
			if ( tempType == CellType.STRING )
			{
				return tCell.getStringCellValue();
			}
			else if ( tempType == CellType.NUMERIC )
			{
				return String.valueOf( tCell.getNumericCellValue() );
			}
		}
		
		return "";
	}
}