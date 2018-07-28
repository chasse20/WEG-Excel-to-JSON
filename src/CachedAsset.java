

import java.util.HashMap;

/** Bare helper class used by a {@link Reader} for caching an {@link Asset} */
public class CachedAsset
{
	/** Actual asset reference */
	public Asset asset;
	/** Cached dictionary of sections used for linking data */
	public HashMap<String,CachedSection> sections;
}