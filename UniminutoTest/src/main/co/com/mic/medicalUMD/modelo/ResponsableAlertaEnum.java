package co.com.mic.medicalUMD.modelo;

public enum ResponsableAlertaEnum
{
    //INFORMACION_NO_PROCESADA("Informacion no procesada");
    ALERTA_POR_PACIENTE("Alerta por paciente"),
    ALERTA_POR_CENTRO_ATENCION("Alerta por centro de atencion"),
    ALERTA_POR_CENTRO_AMBULANCIA("Alerta por centro de ambulancias"),
    ALERTA_POR_AMBULANCIA("Alerta por ambulancia");


    private final String name;

    ResponsableAlertaEnum ( String name )
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }
}
