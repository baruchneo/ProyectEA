package co.com.sc.nexura.superfinanciera.action.user;

import co.com.sc.nexura.superfinanciera.model.BusinessProcess;
import co.com.sc.nexura.superfinanciera.model.FinancialInstitution;
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
					   BusinessProcess businessProcess, Date cutOffDate,
					   FinancialInstitution financialInstitution, String tmpReportStore,
					   String tmpResponseStore, Integer idbusinessCode );

}
