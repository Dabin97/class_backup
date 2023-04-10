import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;

public class APICallTest {

	public static void main(String[] args) {
		try {
			//1. api 호출할 url 설정
			String apiUrl = "http://localhost:8888/member/admin/search";
			//2. 파라미터 셋팅
			String kind = "id";
			String search = URLEncoder.encode("0","utf-8");
			String param = String.format("kind=%s&search=%s", kind,search);
			
			//3. 접속
			URL url = new URL(apiUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			//서버 쪽에서 POST로 받는 경우에만 셋팅
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			
			//4. 전송할 스트림 생성
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			//4-1. 데이터 전송
			dos.write(param.getBytes());
			dos.flush();
			
			//5. 서버쪽에서 보낸 데이터를 받을 스트림을 생성
			BufferedReader br = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			StringBuilder builder = new StringBuilder();
			//5-1. 데이터 읽는 부분
			while(true) {
				String str = br.readLine();
				if(str == null) break;
				builder.append(str);
			}
			System.out.println(builder.toString());
			//전송받은 텍스트를 json으로 파싱해서 출력
			JSONArray arr = new JSONArray(builder.toString());
			for(int i=0;i<arr.length();i++) {
				System.out.println(arr.getJSONObject(i).getString("id"));
				System.out.println(arr.getJSONObject(i).getString("passwd"));
				System.out.println(arr.getJSONObject(i).getString("name"));
				System.out.println(arr.getJSONObject(i).getString("nick"));
				System.out.println(arr.getJSONObject(i).getInt("gradeNo"));
				System.out.println("------------------------");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}




