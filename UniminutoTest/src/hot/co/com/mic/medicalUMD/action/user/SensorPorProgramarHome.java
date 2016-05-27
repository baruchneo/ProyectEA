package co.com.mic.medicalUMD.action.user;

import co.com.mic.medicalUMD.modelo.Estado;
import co.com.mic.medicalUMD.modelo.SensorPorProgramar;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sensorPorProgramarHome")
public class SensorPorProgramarHome extends EntityHome<SensorPorProgramar>
{

	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id
	 */
	private static final long serialVersionUID = 1L;
	
	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//

	
	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//
	
	@Override
	protected SensorPorProgramar createInstance()
	{
		return new SensorPorProgramar();
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//

	public void setSensorPorProgramarId(Long id)
	{
		setId(id);
	}

	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//

	public Long getSensorPorProgramarId()
	{
		return (Long) getId();
	}

	//---------------------------------------------------------------//
	// Class business methods
	//---------------------------------------------------------------//

	public void load()
	{
		if (isIdDefined())
		{
			wire();
		}
	}

	public void wire()
	{
		getInstance();
	}

	public boolean isWired()
	{
		return true;
	}
	

	@Override
	public SensorPorProgramar getInstance()
	{
		return super.getInstance();
	}
	
	public SensorPorProgramar getDefinedInstance()
	{
		return isIdDefined() ? getInstance() : null;
	}
}