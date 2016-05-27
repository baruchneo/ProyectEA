package co.com.mic.medicalUMD.action.user;

import co.com.mic.medicalUMD.modelo.Estado;
import co.com.mic.medicalUMD.modelo.SensorPorProgramar;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;
import java.util.List;

@Name("sensorProProgramarList")
public class SensorPorProgramarList extends EntityQuery<SensorPorProgramar>
{

	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select sensorProProgramarList from SensorPorProgramarList sensorProProgramarList";

	private static final String[] RESTRICTIONS =
	{
		"sensorProProgramarList.id = #{sensorProProgramarList.sensorPorProgramar.id} ",
	};

	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//

	private SensorPorProgramar sensorPorProgramar = new SensorPorProgramar();

	//---------------------------------------------------------------//
	// Class constructors methods
	//---------------------------------------------------------------//

	public SensorPorProgramarList()
	{
		setOrderColumn("sensorPorProgramar.id");
        setOrderDirection("asc");
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		//setMaxResults(25);
	}
	
	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//
	
	public SensorPorProgramar getSensorPorProgramar()
	{
		return sensorPorProgramar;
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//
	
	/**
	 * @param sensorPorProgramar the sensorPorProgramar to set
	 */
	public void setSensorPorProgramar(SensorPorProgramar sensorPorProgramar)
	{
		this.sensorPorProgramar = sensorPorProgramar;
	}
	
	//---------------------------------------------------------------//
	// Class business methods
	//---------------------------------------------------------------//
		
	@Override
	public List<SensorPorProgramar> getResultList()
	{
		return super.getResultList();
	}
}
