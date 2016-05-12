package co.com.mic.medicalUMD.modelo;

/**
 * NIIF reports state definition
 * 
 * @author Alex Vicente Chacon Jimenez (alex.chacon@software-colombia.com)
 *
 */
public enum StateDisplayNIIFEnum
{
	
	INFORMACION_NO_ENVIADA("Informacion no enviada", "Informaci\u00f3n no enviada", "/img/state_void.png"), 
	INFORMACION_EN_PROCESO("Informacion enviada en procesamiento", "Informaci\u00f3n enviada en procesamiento", "/img/state_wait.png"),
	INFORMACION_PROCESADA_EXITO("Informacion procesada con exito", "Informaci\u00f3n procesada con \u00e9xito", "/img/state_success.png"),
	INFORMACION_PROCESADA_ERRORES("Informacion procesada con errores", "Informaci\u00f3n procesada con errores", "/img/state_fail.png"),
	INFORMACION_NO_PROCESADA("Informacion no procesada", "Informaci\u00f3n no procesada", "/img/state_error.png");
	
	/**
	 * State name
	 */
	private final String name;
	
	/**
	 * State label
	 */
	private final String label;
	
	/**
	 * State icon
	 */
	private final String icon;
	
	/**
	 * Default constructor
	 * @param label state label
	 * @param icon state icon URL
	 * @param name state name
	 */
	StateDisplayNIIFEnum ( String name, String label, String icon )
	{
		this.label = label;
		this.icon = icon;
		this.name = name;
	}

	/**
	 * @return current state label
	 */
	public String getLabel()
	{
		return this.label;
	}

	/**
	 * @return current state icon URL
	 */
	public String getIcon()
	{
		return this.icon;
	}
	
	/**
	 * @return current state name
	 */
	public String getName()
	{
		return this.name;
	}
}
