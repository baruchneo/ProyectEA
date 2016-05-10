package co.com.sc.nexura.superfinanciera.model;

public enum StateEnum
{
	
	INFORMACION_NO_ENVIADA("Informacion no enviada"), 
	INFORMACION_EN_PROCESO("Informacion enviada en procesamiento"),
	INFORMACION_PROCESADA_EXITO("Informacion procesada con exito"),
	INFORMACION_PROCESADA_ERRORES("Informacion procesada con errores"),
	INFORMACION_NO_PROCESADA("Informacion no procesada");

	private final String name;

	StateEnum ( String name )
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}

}
