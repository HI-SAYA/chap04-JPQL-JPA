package com.ohgiraffers.section04.paging;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PagingTests {

    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @BeforeAll // 모든 테스트 수행하기 전에 딱 한번
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach //  테스트가 수행 되기 전마다 한번씩
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll // 모든 테스트 수행하기 전에 딱 한번
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @AfterEach //  테스트가 수행 되기 전마다 한번씩
    public void closeManager() {
        entityManager.close();
    }


    @Test
    public void 페이징_API를_이용한_조회_테스트() {
        //given
        int offset = 10; // 뛰어 넘고 싶은 행의 갯수      11 ~ 15행을 불러오겠다. 라는 의미
        int limit = 5;   // 불러오고 싶은 컨텐츠 갯수
        //when
        String jpql = "SELECT m FROM menu_section04 m ORDER BY m.menuCode DESC"; // 메뉴 코드 역순으로 조회
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                .setFirstResult(offset)     // 첫번째 결과가 뭐였으면?
                .setMaxResults(limit)       // 얼만큼 최대한 가져올거?
        // 페이징과 관련된 메소드 FirstResult, MaxResults
                .getResultList();
        //then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
        // order by
        //        menu0_.menu_code DE                  SC offset ? rows fetch next ? rows only
        // offset 10개 행을 뛰어 넘는다 rows fetch next 5개 가져오겠다 rows only
    }
}
