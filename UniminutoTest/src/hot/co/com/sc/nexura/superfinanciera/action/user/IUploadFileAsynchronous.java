package co.com.sc.nexura.superfinanciera.action.user;

import co.com.sc.nexura.superfinanciera.model.BusinessProcess;
import co.com.sc.nexura.superfinanciera.model.FinancialInstitution;
import co.com.sc.nexura.superfinanciera.model.ReportType;
import co.com.sc.nexura.superfinanciera.model.SendTypeEnum;
import org.jboss.seam.annotations.async.Asynchronous;

import javax.ejb.Local;
import java.io.File;
import java.util.Date;
import java.util.List;

@Local
public interface IUploadFileAsynchronous
{

	@Asynchronous
	void processFile ( List<File> dataDefinitiveFiles, File dataNotesFile, File dataNotesFile2,
					   BusinessProcess businessProcess, ReportType reportType,
					   SendTypeEnum sendTypeSelected, Date cutOffDate,
					   FinancialInstitution financialInstitution, String tmpReportStore,
					   String tmpResponseStore, Integer idbusinessCode );

}
