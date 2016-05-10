package co.com.sc.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.faces.bean.ApplicationScoped;

import org.jboss.seam.annotations.Name;
import org.richfaces.model.CalendarDataModelItem;

@Name("CalendarModelDaily")
@ApplicationScoped
public class CalendarModelDaily extends CalendarModelBase
{
	
	public CalendarDataModelItem[] getData(Date[] dateArray)
	{
		CalendarDataModelItem[] modelItems = new CalendarModelItem[dateArray.length];
		Calendar current = GregorianCalendar.getInstance();
		Calendar currentOffset = GregorianCalendar.getInstance();
		
		Calendar today = GregorianCalendar.getInstance();
        today.setTime(new Date());
        today.add(Calendar.DAY_OF_MONTH, 1);
		
        Calendar todayReference = GregorianCalendar.getInstance();
        todayReference.setTime(new Date());
        
		for (int i = 0; i < dateArray.length; i++)
		{
			current.setTime(dateArray[i]);
			currentOffset.setTime(dateArray[i]);
			
			CalendarModelItem modelItem = new CalendarModelItem();
			
			if (today.after(current))
			{
				modelItem.setEnabled(true);
				modelItem.setStyleClass(ENABLED_DAY_CLASS);
				
			}
			else
			{
				modelItem.setEnabled(false);
				modelItem.setStyleClass(FUTURE_DAY_CLASS);
			}
			
			if (checkNow(current))
			{
				modelItem.setStyleClass(BUSYTODAY_DAY_CLASS);
			}
			
			modelItems[i] = modelItem;
		}

		return modelItems;
	}
}