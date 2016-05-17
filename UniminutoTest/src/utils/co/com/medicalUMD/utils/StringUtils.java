package co.com.medicalUMD.utils;

public class StringUtils
{
	public static String substring(String source, int start, int length)
	{
		if (source.length() > 0 && start >= 0 && length >= 0)
		{
			if (source.length() > length)
			{
				return source.substring(start, length) + " ...";
			}
		}

		return source;
	}

	public static String getInitials(String source)
	{
		StringBuilder sbInitials = new StringBuilder();
		String[] nameParts = source.split("\\s");
		for (String part : nameParts)
		{
			if (part != null && part.trim().length() > 2)
			{
				sbInitials.append(part.charAt(0));
			}
		}
		return sbInitials.toString();
	}
	
	public static String replaceString(String source, String target, String replacement)
	{
		String sourceFinal = source.replace(target, replacement);
		return sourceFinal;
	} 
}