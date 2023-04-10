package book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@TestMethodOrder(MethodOrderer.Alphanumeric.class) //이거 안넣음
@SpringBootTest
class BookWebProjectApplicationTests {

	@Autowired
	BookMapper mapper;

	@DisplayName("도서정보검색 테스트")
	@Test
	@Order(1)
	void selectBookTest() {
		System.out.println("도서정보 검색 테스트");
		String title = "자바";
		List<BookDTO> list = mapper.selectBook(title);
		System.out.println(list);
		assertEquals(0, list.size(),"조회 오류");
	}
	
	@DisplayName("전체 정보 조회 테스트") //이거 전체조회는 없어도됐음
	@Test
	@Order(2)
	void selectAllBookTest() {
		List<BookDTO> list = mapper.selectAllBook();
		System.out.println("전체도서정보 검색 테스트");
		System.out.println(list);
		assertNotEquals(0, list.size());
		fail();
	}
	
	@DisplayName("도서정보 추가 테스트")
	@Test
	@Order(3)
	void insertBookTest() {
		System.out.println("도서정보 추가 테스트");
		BookDTO dto = new BookDTO("891245671234","자바 프로그래밍","홍길동","J테스트","2020-02-19");
		int result = mapper.insertBook(dto);
		assertEquals(1, result,"도서정보 추가 실패");
	}
	
	@DisplayName("도서정보 삭제 테스트")
	@Test
	@Order(4)
	void deleteBookTest() {
		//테스트 데이터 삭제
		System.out.println("도서정보 삭제 테스트");
		int result = mapper.deleteBook("891245671234");
		assertEquals(1, result,"도서정보 삭제 실패");
	}

}
