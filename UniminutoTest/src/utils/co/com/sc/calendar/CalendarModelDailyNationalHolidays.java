package co.com.sc.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.faces.bean.ApplicationScoped;

import org.jboss.seam.annotations.Name;
import org.richfaces.model.CalendarDataModelItem;

//import co.com.sc.nexura.superfinanciera.model.ColombianNationalHolidaysEnum;
//import co.com.sc.nexura.superfinanciera.model.ColombianVariableNationalHolidaysEnum;

@Name("calendarModelDailyNationalHolidays")
@ApplicationScoped
public class CalendarModelDailyNationalHolidays extends CalendarModelBase
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
			
			if (today.after(current) || today.compareTo(current) == 0)
			{
				if (current.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || current.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
				{
					modelItem.setEnabled(false);
					modelItem.setStyleClass(WEEKEND_DAY_CLASS);
					
					if (today.compareTo(current) == 0)
					{
						 today.add(Calendar.DAY_OF_MONTH, 1);
					}
				}
				else
				{
					if (checkColombianNationalHoliday(current))
					{
						modelItem.setEnabled(false);
						modelItem.setStyleClass(WEEKEND_DAY_CLASS);
						
						if (today.compareTo(current) == 0)
						{
							 today.add(Calendar.DAY_OF_MONTH, 1);
						}
					}
					else
					{
						modelItem.setEnabled(true);
						modelItem.setStyleClass(ENABLED_DAY_CLASS);
					}
				}
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
	
	/**
	 * Checks if the given day is a national holiday
	 * @param current given day
	 * @return true if the given day is a national holiday. False otherwise
	 */
	private boolean checkColombianNationalHoliday(Calendar current)
	{
		/*for (ColombianNationalHolidaysEnum currentDate : ColombianNationalHolidaysEnum.values())
		{
			Calendar currentHoliday = currentDate.getDate();
			if (currentHoliday.get(Calendar.DAY_OF_MONTH) == current.get(Calendar.DAY_OF_MONTH) && currentHoliday.get(Calendar.MONTH) == current.get(Calendar.MONTH))
			{
				return true;
			}
		}
		
		if (current.get(Calendar.YEAR) >= 2000 && current.get(Calendar.YEAR) <= 2030)
		{
			for (ColombianVariableNationalHolidaysEnum currentDate : ColombianVariableNationalHolidaysEnum.values()) 
			{
				Calendar currentHoliday = currentDate.getDate();
				if (currentHoliday.get(Calendar.DAY_OF_MONTH) == current.get(Calendar.DAY_OF_MONTH) && currentHoliday.get(Calendar.MONTH) == current.get(Calendar.MONTH) && currentHoliday.get(Calendar.YEAR) == current.get(Calendar.YEAR))
				{
					return true;
				}
			}
		}*/
		
		return false;
	}
}