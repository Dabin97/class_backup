
public class StartSchedulerMain {

	public static void main(String[] args) {
		TestCronTrigger trigger = new TestCronTrigger("0 0/1 * * 1/1 ? *", AutoDatePringJob.class); //클래스형태의 객체가 들어감(생성해주는건 X 그냥 정보)
		trigger.triggerJob();
		//0 0/1 * * 1/1 ? * --> 초 분 시 일 월 요일 연도 / 와일드카드(*) 문자는 '매 번'을 의미한다. /물음표(?) 는 '설정값 없음'을 나타낸다. 이는 일(DOM)과 요일(DOW)에만 사용할 수 있다.
		//https://www.leafcats.com/94 표현식 해석

		
	}

}
