
public class NaverSearchStart {

	public static void main(String[] args) {
		String cron = "0 0 8 1/1 * ? *";
//		String cron = "0/30 * * * 1/1 ? *";
		TestCronTrigger cronTrigger = new TestCronTrigger(cron, NaverSearchJob.class);
		cronTrigger.triggerJob();
	}

}

