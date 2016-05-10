package co.com.sc.nexura.superfinanciera.model;

/**
 * NIIF associated business process definition
 * 
 * @author Alex Vicente Chacon Jimenez (alex.chacon@software-colombia.com)
 *
 */
public enum BusinessProcessNIIFEnum
{	
	
	XBRL("xbrl", "Xbrl"), 
	EXCEL_ESTADO("excel estados", "Excel"),
	EXCEL_NOTAS("excel notas", "Excel notas"),
	PDF("pdf", "Pdf");
	
	/**
	 * Report type name
	 */
	private final String name;
	
	/**
	 * Report type label
	 */
	private final String label;
	
	/**
	 * Default constructor
	 * @param name report type name
	 * @param label report type name
	 */
	BusinessProcessNIIFEnum ( String name, String label )
	{
		this.name = name;
		this.label = label;
	}

	/**
	 * @return report type name
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * @return report type label
	 */
	public String getLabel()
	{
		return this.label;
	}
}
