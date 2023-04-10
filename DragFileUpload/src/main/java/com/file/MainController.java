package com.file;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MainController {
	@RequestMapping("/")
	public String main() {
		return "index";
	}
	
	@RequestMapping("/fileUpload.do")
	public String fileUpload(@RequestParam(value="file") MultipartFile[] fileload, HttpServletRequest request) {
		//파일 업로드할 경로 설정
		String root = "c:\\fileupload\\";
		//현재 날짜 시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
		String date = sdf.format(Calendar.getInstance().getTime());
		//저장한 파일경로
		String fileResult = "";
		
		for(int i=0;i<fileload.length;i++) {
			if(fileload[i].getSize() == 0) continue; //파일크기를 체크할수있다. 만약 파일크기가 0이면 로드하게끔, 그러나 직접 드래그해서 올리는것이기 때문에 필요없다. 
			//서버에 파일을 저장할때 파일명을 날짜시간으로 변경
			//DB에 저장할 때는 원본파일명과 변경된 파일명 모두 저장
			//원본 파일명 뽑음
			String originFileName = fileload[i].getOriginalFilename();
			//저장할 파일명
			String fileName = date + "_" + i  + originFileName.substring(originFileName.lastIndexOf('.'));
			System.out.println("저장할 파일명 : " + fileName);
			
			try {
				//실제 파일이 업로드 되는 부분
				File file = new File(root + fileName); 
				fileload[i].transferTo(file);
				fileResult += file.getAbsolutePath()+"<br>"; //.getAbsolutePath() 경로를 리턴
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		request.setAttribute("file_path", fileResult); //file_path에 절대경로를 넣음, result.html에서 꺼낼것임
		return "result";
	}
	
	
	@RequestMapping("/fileAjaxUpload.do")
	public ResponseEntity<String> fileAjaxUpload(@RequestParam(value = "file") MultipartFile[] fileload) {
		ArrayList<String> list = new ArrayList<String>();
		// 파일 업로드할 경로 설정
		String root = "c:\\fileupload\\";
		// 현재 날짜 시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
		String date = sdf.format(Calendar.getInstance().getTime());
		// 저장한 파일경로
		String fileResult = "";
		for (int i = 0; i < fileload.length; i++) {
			System.out.println(fileload[i].getOriginalFilename());
			if (fileload[i].getSize() == 0)
				continue;
			// 서버에 파일을 저장할때 파일명을 날짜시간으로 변경
			// DB에 저장할 때는 원본파일명과 변경된 파일명 모두 저장
			// 원본 파일명 뽑음
			String originFileName = fileload[i].getOriginalFilename();
			// 저장할 파일명
			String fileName = date + "_" + i + originFileName.substring(originFileName.lastIndexOf('.'));
			System.out.println("저장할 파일명 : " + fileName);

			try {
				// 실제 파일이 업로드 되는 부분
				File file = new File(root + fileName);
				fileload[i].transferTo(file);
				//DB에 저장 후, 파일 다운로드 시키는 경로를 완성해서 list에 추가
				list.add(file.getAbsolutePath());
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return new ResponseEntity(list, HttpStatus.OK);
	}


	
	
}
