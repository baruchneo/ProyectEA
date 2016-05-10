package co.com.sc.nexura.superfinanciera.action.user;

import java.util.Date;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.async.Expiration;
import org.jboss.seam.annotations.async.IntervalDuration;
import org.jboss.seam.async.QuartzTriggerHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Name("processor")
@AutoCreate
@Scope(ScopeType.APPLICATION)
public class ScheduleProcessor 
{
	
	/**
	 * Default class logger
	 */
	private static final Logger _LOGGER = LoggerFactory.getLogger(ScheduleProcessor.class);
   
   @In(create = true)
   ReaderResponse readerResponse;
   
   @Asynchronous
   @Transactional
   public QuartzTriggerHandle createQuartzResponseTimer( @Expiration Date when, @IntervalDuration Long interval) 
   {
       _LOGGER.debug("Quartz Response: " + new Date());
       readerResponse.iteratorSendingInProcess();
       return null;
   }
   
}
