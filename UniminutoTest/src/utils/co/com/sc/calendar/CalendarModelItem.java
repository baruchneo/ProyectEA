package co.com.sc.calendar;

import org.richfaces.model.CalendarDataModelItem;

public class CalendarModelItem implements CalendarDataModelItem 
{
	private boolean enabled;
	private String styleClass;
	private Object toolTip;
	
	public void setEnabled(boolean enabled) 
	{
		this.enabled=enabled;
	}
	
	public void setStyleClass(String styleClass) 
	{
		this.styleClass = styleClass;
	}
	
	public void setToolTip (Object toolTip)
	{
		this.toolTip = toolTip;
	}
	
	public boolean isEnabled() 
	{
		return this.enabled;
	}
	
	public String getStyleClass() 
	{
		return this.styleClass;
	}
	
	public Object getData() 
	{
		return null;
	}
	
	public boolean hasToolTip() 
	{
		return true;
	}
	
	public Object getToolTip() 
	{
		return this.toolTip;
	}
	
	public int getDay() 
	{
		return 0;
	}
}