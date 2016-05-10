package co.com.sc.nexura.superfinanciera.action.user;

import static org.jboss.seam.ScopeType.APPLICATION;

import java.io.Serializable;
import java.util.Date;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.async.QuartzTriggerHandle;
 
@Name("controller")
@Scope(APPLICATION)
@AutoCreate
@Startup
public class ScheduleController implements Serializable 
{
    /**
	 * Default serial version
	 */
	private static final long serialVersionUID = 3755382207633136297L;

	@In
    ScheduleProcessor processor;
 
    private QuartzTriggerHandle quartzResponseTriggerHandle;
    
    private static Long CRON_INTERVAL_RESPONSE = 30000L;
 
    @Create
    public void scheduleTimer() 
    {
    	Date ProcessDate = new Date(System.currentTimeMillis()+CRON_INTERVAL_RESPONSE);
        quartzResponseTriggerHandle = processor.createQuartzResponseTimer(ProcessDate, CRON_INTERVAL_RESPONSE);
    }

	/**
	 * @return the quartzResponseTriggerHandle
	 */
	public QuartzTriggerHandle getQuartzResponseTriggerHandle()
	{
		return quartzResponseTriggerHandle;
	}

	/**
	 * @param quartzResponseTriggerHandle the quartzResponseTriggerHandle to set
	 */
	public void setQuartzResponseTriggerHandle(QuartzTriggerHandle quartzResponseTriggerHandle)
	{
		this.quartzResponseTriggerHandle = quartzResponseTriggerHandle;
	}
}
