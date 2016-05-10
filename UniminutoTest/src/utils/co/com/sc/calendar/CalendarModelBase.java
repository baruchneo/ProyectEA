package co.com.sc.calendar;

import java.util.Calendar;
import java.util.Date;

import org.richfaces.model.CalendarDataModel;

public abstract class CalendarModelBase implements CalendarDataModel
{
	protected static final String WEEKEND_DAY_CLASS = "wdc";
	protected static final String BUSY_DAY_CLASS = "bdc";
	protected static final String ENABLED_DAY_CLASS = "enableddc";
	protected static final String FUTURE_DAY_CLASS = "fdc";
	protected static final String BUSYTODAY_DAY_CLASS = "tdc";
	
	private static final String DEFAULT_TOOL_TIP = "Fecha de corte habilitada para llevar a cabo un envio";
	
	protected boolean checkWeekend(Calendar calendar)
	{
		return (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY);
	}
	
	protected boolean checkMonday(Calendar calendar)
	{
		return (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY );
	}
	
	protected boolean checkFirstDayOfMonth(Calendar calendar)
	{
		return (calendar.get(Calendar.DAY_OF_MONTH) == 1 );
	}
	
	protected boolean checkLastDayOfMonth(Calendar calendar)
	{
		return (calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH) );
	}
	
	protected boolean checkNow(Calendar calendar)
	{
		Calendar now = Calendar.getInstance();
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		int yearNow = now.get(Calendar.YEAR);
		int monthNow = now.get(Calendar.MONTH);
		int dayNow = now.get(Calendar.DAY_OF_MONTH);
		
		return (year == yearNow && month == monthNow && day == dayNow);
	}
	
	protected boolean checkEvenMonth(Calendar calendar)
	{
		int month = calendar.get(Calendar.MONTH);
		return (month%2 == 0);
	}
	
	protected boolean checkQuarter(Calendar calendar)
	{
		int month = calendar.get(Calendar.MONTH);
		return (month%3 == 0);
	}
	
	protected boolean checkSemester(Calendar calendar)
	{
		int month = calendar.get(Calendar.MONTH);
		return (month%6 == 0);
	}
	
	protected boolean checkJanuaryFirst(Calendar calendar)
	{
		return (calendar.get(Calendar.MONTH) == Calendar.JANUARY && calendar.get(Calendar.DAY_OF_MONTH) == 1);
	}
	
	protected int getFirstMondayDayOfMonthOfTheYear(Calendar calendar)
	{
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int i = 1;
		
		while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
		{
			i++;
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		return i;
	}
	
	public Object getToolTip(Date date)
	{
		return DEFAULT_TOOL_TIP;
	}
}