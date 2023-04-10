package com.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.board.dto.MemberDTO;
import com.board.mapper.MemberMapper;

@SpringBootTest
class SpringBoardProject2ApplicationTests {
	
	@Autowired
	MemberMapper mapper;
	
	@DisplayName("회원정보 추가 테스트")
	@Test
	@Order(3)
	void updateMemberTest() {
		System.out.println("회원정보 추가 테스트");
		MemberDTO dto = new MemberDTO("Test11","123456","테스트","테스트용",1);
		int result = mapper.insertMember(dto);
		assertEquals(1, result,"회원정보 추가 실패");
	}
	
	@DisplayName("회원정보 삭제 테스트")
	@Test
	@Order(4)
	void deleteMemberTest() {
		//테스트 데이터 삭제
		System.out.println("회원정보 삭제 테스트");
		int result = mapper.deleteMember("Test11");
		assertEquals(1, result,"회원정보 추가 실패");
	}
	
	@DisplayName("회원정보검색 테스트")
	@Test
	@Order(1)
	void searchMemberTest() {
		System.out.println("회원정보 검색 테스트");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("kind", "id");
		map.put("search", "0");
		List<MemberDTO> list = mapper.searchMember(map);
		assertEquals(0, list.size(),"전체 조회 오류");
	}
	
	@DisplayName("전체 정보 조회 테스트")
	@Test
	@Order(2)
	void selectAllMember() {
		List<MemberDTO> list = mapper.selectAllMember();
		System.out.println("전체회원정보 검색 테스트");
		assertNotEquals(0, list.size());
		fail();
	}
	
	@BeforeEach
	void beforeEach() {
		System.out.println("테스트 메서드 수행전에 실행되는 메서드");
	}
	
	@AfterEach
	void afterEach() {
		System.out.println("테스트 메서드 수행후에 실행되는 메서드");
	}

	@BeforeAll
	static void beforeAll() {
		System.out.println("전체 테스트 수행 전 맨처음에 한번 수행되는 메서드");
	}
	@AfterAll
	static void afterAll() {
		System.out.println("전체 테스트 수행 후 맨마지막에 한번 수행되는 메서드");
	}
}













