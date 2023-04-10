package com.board;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.board.dto.BoardCommentDTO;
import com.board.dto.BoardDTO;
import com.board.dto.LogDTO;
import com.board.dto.MemberDTO;
import com.board.mapper.BoardLogMapper;

@Component
@Aspect
public class BoardAOP {
		//MemberController에서 실행되는 메서드의 작업자와 실행시간 등을 txt파일로 만들기
		private PrintWriter pw; //메소드 밖에 prinWriter 변수를 만들어두기
		private BoardLogMapper mapper;
		
		
		public BoardAOP(BoardLogMapper mapper) {
			this.mapper = mapper;
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String fileName = sdf.format(Calendar.getInstance().getTime())+"_log.csv"; //filename에 그냥 date를 넣기
			try {
				FileOutputStream fos = new FileOutputStream(fileName);
				pw = new PrintWriter(fos);
				System.out.println("로그 파일 연결 완료");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}			
			
		}
	
		@Before("execution(* com.board.service.MemberService.*(..))") //컨트롤러로 경로를 연결해도 되지만, session을 꺼내기 위해 모든 컨트롤러에 세션이 들어가야함.
		public void memberLog(JoinPoint joinpoint) throws Throwable{
			Object[] arr = joinpoint.getArgs();//getArgs(), 메소드의 매개변수값을 받아서 arr배열에 넣는것. 인자값을 배열에 넣어서 출력한다.
			System.out.println(Arrays.toString(arr));
			//날짜시간,작업자,실행한메서드,작업데이터
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			StringBuffer str = new StringBuffer(); //문자열을 계속 붙여넣어야할떄는 stringbuffer가 낫다
			str.append(sdf.format(Calendar.getInstance().getTime())+",");
			str.append(joinpoint.getSignature().getName() + ","); //실행한 메서드

			for (Object obj : joinpoint.getArgs()) { //작업데이터
				if (obj instanceof MemberDTO) { //instanceof 연산자를 사용하면 객체가 특정 클래스에 속하는지 아닌지를 확인할수있다. 따라서 ogj가 MemberDTO에 속하면, 이라는 조건문
					MemberDTO dto = (MemberDTO) obj;
					str.append(dto.getId() + " / ");
					str.append(dto.getName() + " / ");
					str.append(dto.getGradeNo() + " / ");
					str.append(dto.getNick() + " / ");
					str.append(dto.getPasswd() + " / ");
				}else if(obj instanceof HashMap) {
					Map map = (Map) obj;
					Iterator it = map.keySet().iterator(); //iterator는 ArrayList, HashSet과 같은 컬렉션을 반복하는 데 사용할 수 있는 객체
					while(it.hasNext()) {
						str.append(it.next() + " / ");
					}
				}else if(obj instanceof HttpSession){ //컨트롤러에 session추가해서 작업자 추가함
					HttpSession session = (HttpSession) obj;
					MemberDTO dto = (MemberDTO) session.getAttribute("dto");
					str.append("," + dto.getId() + " / "); 
				}
				else {
					str.append(obj + " / ");
				}
			}
			
			str.delete(str.length()-3, str.length()-1); //인덱스번호
			pw.println(str);
			pw.flush();
		}
		
		//게시판에 글을 쓸 경우에 해당 글정보를 board_log에 저장
		//mapper를 새로 생성 -> Mapper : BoardLogMapper, board_log_mapper.xml
		@Around("execution(* com.board.service.BoardService.insertBoard(..))")
		public Object insertBoardLog(ProceedingJoinPoint joinpoint) throws Throwable{
			Object val = joinpoint.proceed();
			System.out.println("게시판 글쓰기 기능 수행"); //test
			
			BoardDTO dto = (BoardDTO)joinpoint.getArgs() [0]; 
			LogDTO log = new LogDTO();
			log.setRunMethod(joinpoint.getSignature().getName());
			log.setUpdateData(dto.toString());
			log.setWriter(dto.getWriter());
			mapper.insertLog(log);
				
			return val;
		}
		
		//게시판에 댓글 달았을때 로그 추가
		@Around("execution(* com.board.service.BoardService.insertBoardComment(..))")
		public Object insertCommentLog(ProceedingJoinPoint joinpoint) throws Throwable{
			Object val = joinpoint.proceed();
			System.out.println("댓글 쓰기 기능 수행"); //test
			
			BoardCommentDTO dto = (BoardCommentDTO)joinpoint.getArgs() [0]; 
			LogDTO log = new LogDTO();
			log.setRunMethod(joinpoint.getSignature().getName());
			log.setUpdateData(dto.toString());
			log.setWriter(dto.getWriter());
			mapper.insertLog(log);
				
			return val;
		}
		
		//좋아요 싫어요 했을때도 로그 추가
		@Around("execution(* com.board.service.BoardService.insertBoard*(int,String))") //좋아요싫어요 둘다 할거라 insertBoard*(int,String) 매개변수 타입을 지정해서 likeHate만 나오도록 함
		public Object likeBoardLog(ProceedingJoinPoint joinpoint) throws Throwable{
			Object val = joinpoint.proceed();
			System.out.println(joinpoint.getSignature().getName()); //test
			
			LogDTO log = new LogDTO();
			log.setRunMethod(joinpoint.getSignature().getName());
			log.setUpdateData(Arrays.toString(joinpoint.getArgs())+((int)val == 0 ? "/ delete" : " / insert")); //좋아요를 했는지 취소했는지 알수있는 삼항연산자
			log.setWriter(joinpoint.getArgs()[1].toString());
			mapper.insertLog(log);
				
			return val;
		}
	
		//댓글 좋아요싫어요 로그 추가
		@Around("execution(* com.board.service.BoardService.insertBoardCommentLikeHate(String, int, String))")//(String mode, int cno, String id)
		public Object likeBoardCommentLog(ProceedingJoinPoint joinPoint) throws Throwable {
			Object val = joinPoint.proceed();
			System.out.println(joinPoint.getSignature().getName());
			LogDTO log = new LogDTO();
			log.setRunMethod(joinPoint.getSignature().getName());
			log.setUpdateData(Arrays.toString(joinPoint.getArgs())+ 
					((int)val == 0 ? " / delete" : " / insert"));
			log.setWriter(joinPoint.getArgs()[2].toString());
			mapper.insertLog(log);
			return val;
		}

		
		
		
		@Around("execution(* com.board.service.MemberService.login(..))")
		public Object loginLog(ProceedingJoinPoint joinPoint) throws Throwable{
			Object val = joinPoint.proceed();//메서드 실행하는 부분
			String str = "login : %s / %s ,접속주소 : %s, 요청시간 : %s, 결과 : %s";//문자열로 받기
			Object[] args = joinPoint.getArgs();//getArgs() 인자값을 배열에 넣어서 출력한다.
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sdf.format(Calendar.getInstance().getTime());
			str = String.format(str, args[0],args[1],args[2], date, (val == null ? "로그인실패" : val.toString()));//str에 값들 모두 넣기
			System.out.println(str);
			return val;
		}
		
		@Around("execution(* com.board.service.MemberService.logout(..))")
		public Object logoutLog(ProceedingJoinPoint joinPoint) throws Throwable{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sdf.format(Calendar.getInstance().getTime());
			HttpSession session = (HttpSession)joinPoint.getArgs()[0];
			MemberDTO dto = (MemberDTO) session.getAttribute("dto");
			System.out.println(dto.getId() + " 로그아웃, " + date);
			return joinPoint.proceed();
		}
		
		
		

	
}
