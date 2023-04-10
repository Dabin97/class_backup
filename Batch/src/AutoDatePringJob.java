import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AutoDatePringJob implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// 설정한 주기에 실행할 기능
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(Calendar.getInstance().getTime()));
		
	}


}
