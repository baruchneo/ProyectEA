package co.com.sc.nexura.superfinanciera.action.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.richfaces.model.CalendarDataModel;

import co.com.sc.calendar.CalendarModelBiMonthly;
import co.com.sc.calendar.CalendarModelBiWeekly;
import co.com.sc.calendar.CalendarModelDaily;
import co.com.sc.calendar.CalendarModelDailyNationalHolidays;
import co.com.sc.calendar.CalendarModelMonthly;
import co.com.sc.calendar.CalendarModelQuarter;
import co.com.sc.calendar.CalendarModelSemester;
import co.com.sc.calendar.CalendarModelWeekly;
import co.com.sc.calendar.CalendarModelYearly;
import co.com.sc.nexura.superfinanciera.action.admin.ReportTypeList;
import co.com.sc.nexura.superfinanciera.model.BusinessProcess;
import co.com.sc.nexura.superfinanciera.model.ReportType;
import co.com.sc.nexura.superfinanciera.model.ReportTypePeriodicityEnum;
import co.com.sc.nexura.superfinanciera.model.SendTypeEnum;

@Name("reportTypePeriodicityBean")
@Scope(ScopeType.SESSION)
public class ReportTypePeriodicityBean implements Serializable
{
	
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//
	@In(create=true) 
	ReportTypeList reportTypeList;
	
	CalendarDataModel currentCalendarDataModel;
	
	private List<SelectItem> sendingTypes = new ArrayList<SelectItem>();
	
	private Long reportTypeId = null;
	
	private String reportTypeName = "";
	
	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//
	
	/**
	 * @return the currentCalendarDataModel
	 */
	public CalendarDataModel getCurrentCalendarDataModel()
	{
		return currentCalendarDataModel;
	}

	/**
	 * @return the reportTypeId
	 */
	public Long getReportTypeId()
	{
		return reportTypeId;
	}
	
	/**
	 * @return the reportTypeName
	 */
	public String getReportTypeName() 
	{
		return reportTypeName;
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//
	
	/**
	 * @param reportTypeName the reportTypeName to set
	 */
	public void setReportTypeName(String reportTypeName) 
	{
		this.reportTypeName = reportTypeName;
	}

	/**
	 * @param reportTypeId the reportTypeId to set
	 */
	public void setReportTypeId(Long reportTypeId)
	{
		this.reportTypeId = reportTypeId;
	}

	/**
	 * @param sendingTypes the sendingTypes to set
	 */
	public void setSendingTypes(List<SelectItem> sendingTypes)
	{
		this.sendingTypes = sendingTypes;
	}

	/**
	 * @param currentCalendarDataModel the currentCalendarDataModel to set
	 */
	public void setCurrentCalendarDataModel(CalendarDataModel currentCalendarDataModel)
	{
		this.currentCalendarDataModel = currentCalendarDataModel;
	}
	
	//---------------------------------------------------------------//
	// Class business methods
	//---------------------------------------------------------------//
	
	/**
	 * Get Event from Integer ID value
	 * @param event
	 */
	public void loadNewCalendarDataModel(ValueChangeEvent event)
	{
		changeLogic ((Long) event.getNewValue());
	}
	
	/**
	 * Get Event from String param valua
	 * @param event
	 */
	public void loadNewCalendarDataModelString(ValueChangeEvent event)
	{
		changeLogicString ((String) event.getNewValue());
	}
	
	/**
	 * Change the frequency of schedule by type report specified for id
	 * @param reportTypeId
	 */
	public void changeLogic (Long reportTypeId)
	{
		this.reportTypeId = reportTypeId;
		
		if (this.reportTypeId != null)
		{
			reportTypeList.getReportType().setId(reportTypeId);
			List<ReportType> currentReportTypeList = reportTypeList.getResultList();
			
			this.sendingTypes = new ArrayList<SelectItem>();
			
			if (currentReportTypeList.size() > 0)
			{
				ReportType selectedReportType = currentReportTypeList.get(0);
				BusinessProcess currentBusinessProcess = selectedReportType.getBusinessProcess();
				
				if (selectedReportType.getPeriodicity().equals(ReportTypePeriodicityEnum.DIARIA.getLabel()))
				{
					this.currentCalendarDataModel= new CalendarModelDaily();
				}
				else if (selectedReportType.getPeriodicity().equals(ReportTypePeriodicityEnum.SEMANAL.getLabel()))
				{
					this.currentCalendarDataModel= new CalendarModelWeekly();
				}
				else if (selectedReportType.getPeriodicity().equals(ReportTypePeriodicityEnum.BISEMANAL.getLabel()))
				{
					this.currentCalendarDataModel= new CalendarModelBiWeekly();
				}
				else if (selectedReportType.getPeriodicity().equals(ReportTypePeriodicityEnum.MENSUAL.getLabel()))
				{
					this.currentCalendarDataModel= new CalendarModelMonthly();
				}
				else if (selectedReportType.getPeriodicity().equals(ReportTypePeriodicityEnum.BIMENSUAL.getLabel()))
				{
					this.currentCalendarDataModel= new CalendarModelBiMonthly();
				}
				else if (selectedReportType.getPeriodicity().equals(ReportTypePeriodicityEnum.TRIMESTRAL.getLabel()))
				{
					this.currentCalendarDataModel= new CalendarModelQuarter();
				}
				else if (selectedReportType.getPeriodicity().equals(ReportTypePeriodicityEnum.SEMESTRAL.getLabel()))
				{
					this.currentCalendarDataModel= new CalendarModelSemester();
				}
				else if (selectedReportType.getPeriodicity().equals(ReportTypePeriodicityEnum.ANUAL.getLabel()))
				{
					this.currentCalendarDataModel= new CalendarModelYearly();
				}
				else if (selectedReportType.getPeriodicity().equals(ReportTypePeriodicityEnum.DIARIA_HABILES.getLabel()))
				{
					this.currentCalendarDataModel= new CalendarModelDailyNationalHolidays();
				}
				else
				{
					this.currentCalendarDataModel= new CalendarModelDaily();
				}
				
				if (currentBusinessProcess.getValidationAvailable())
				{
					this.sendingTypes.add(new SelectItem(new Boolean(true), SendTypeEnum.ENVIO_VALIDACION.getLabel()));
					this.sendingTypes.add(new SelectItem(new Boolean(false), SendTypeEnum.ENVIO_DEFINITIVO.getLabel()));
				}
				else
				{
					this.sendingTypes.add(new SelectItem(new Boolean(false), SendTypeEnum.ENVIO_DEFINITIVO.getLabel()));
				}
			}
			else
			{
				this.currentCalendarDataModel= new CalendarModelDaily();
			}
		}
		else
		{
			this.currentCalendarDataModel= new CalendarModelDaily();
		}
	}
	
	/**
	 * Change the frequency of schedule by type report specified for string value
	 * @param reportTypeName
	 */
	public void changeLogicString(String reportTypeName)
	{
		this.reportTypeName = reportTypeName;
		
		if (this.reportTypeName != null)
		{
			reportTypeList.getReportType().setName(reportTypeName);
			List<ReportType> currentReportTypeList = reportTypeList.getResultList();
			
			this.sendingTypes = new ArrayList<SelectItem>();
			
			if (currentReportTypeList.size() > 0)
			{
				ReportType selectedReportType = currentReportTypeList.get(0);
				BusinessProcess currentBusinessProcess = selectedReportType.getBusinessProcess();
				
				if (selectedReportType.getPeriodicity().equals(ReportTypePeriodicityEnum.DIARIA.getLabel()))
				{
					this.currentCalendarDataModel= new CalendarModelDaily();
				}
				else if (selectedReportType.getPeriodicity().equals(ReportTypePeriodicityEnum.SEMANAL.getLabel()))
				{
					this.currentCalendarDataModel= new CalendarModelWeekly();
				}
				else if (selectedReportType.getPeriodicity().equals(ReportTypePeriodicityEnum.BISEMANAL.getLabel()))
				{
					this.currentCalendarDataModel= new CalendarModelBiWeekly();
				}
				else if (selectedReportType.getPeriodicity().equals(ReportTypePeriodicityEnum.MENSUAL.getLabel()))
				{
					this.currentCalendarDataModel= new CalendarModelMonthly();
				}
				else if (selectedReportType.getPeriodicity().equals(ReportTypePeriodicityEnum.BIMENSUAL.getLabel()))
				{
					this.currentCalendarDataModel= new CalendarModelBiMonthly();
				}
				else if (selectedReportType.getPeriodicity().equals(ReportTypePeriodicityEnum.TRIMESTRAL.getLabel()))
				{
					this.currentCalendarDataModel= new CalendarModelQuarter();
				}
				else if (selectedReportType.getPeriodicity().equals(ReportTypePeriodicityEnum.SEMESTRAL.getLabel()))
				{
					this.currentCalendarDataModel= new CalendarModelSemester();
				}
				else if (selectedReportType.getPeriodicity().equals(ReportTypePeriodicityEnum.ANUAL.getLabel()))
				{
					this.currentCalendarDataModel= new CalendarModelYearly();
				}
				else if (selectedReportType.getPeriodicity().equals(ReportTypePeriodicityEnum.DIARIA_HABILES.getLabel()))
				{
					this.currentCalendarDataModel= new CalendarModelDailyNationalHolidays();
				}
				else
				{
					this.currentCalendarDataModel= new CalendarModelDaily();
				}
				
				if (currentBusinessProcess.getValidationAvailable())
				{
					this.sendingTypes.add(new SelectItem(new Boolean(true), SendTypeEnum.ENVIO_VALIDACION.getLabel()));
					this.sendingTypes.add(new SelectItem(new Boolean(false), SendTypeEnum.ENVIO_DEFINITIVO.getLabel()));
				}
				else
				{
					this.sendingTypes.add(new SelectItem(new Boolean(false), SendTypeEnum.ENVIO_DEFINITIVO.getLabel()));
				}
			}
			else
			{
				this.currentCalendarDataModel= new CalendarModelDaily();
			}
		}
		else
		{
			this.currentCalendarDataModel= new CalendarModelDaily();
		}
	}
}
