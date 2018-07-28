

import java.util.HashMap;

/** Bare helper class used by a {@link Reader} for caching a {@link Section} */
public class CachedSection
{
	/** Actual section reference */
	public Section section;
	/** Cached dictionary of sub-sections used for linking data */
	public HashMap<String,Section> subSections;
}