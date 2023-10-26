package com.ohgiraffers.section08.namedquery;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NamedQueryTests {

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


    /* 네임드 쿼리 (정적인 쿼리) => StringBuilder jpql = new StringBuilder(" ")
    String jpql = "..."; => 동적 쿼리 - 프로그램이 실행되면서 동작하기 때문에 동적 쿼리 */

    @Test
    public void 동적쿼리를_이용한_조회_테스트() {
        //given
        String searchName = "한우"; // *** "" 도 가능 -> ""일 경우 where절에 붙는 조건이 categoryCode만 붙는다.
        int searchCategoryCode = 4; // *** 0 도 가능 -> 0일 경우는 where절에 붙는 조건이 like만 붙는다.
        // *** "" + 0인 경우에는 where 조건이 없기 때문에 전체 조회 되는 것을 확인할 수 있다.
        //when
        StringBuilder jpql = new StringBuilder("SELECT m FROM menu_section08 m "); // 기본이 되는 쿼리를 StringBuilder로 만들었다.
        if (searchName != null && !searchName.isEmpty() && searchCategoryCode > 0) { // 모든 파라미터가 넘어온 경우
            jpql.append("WHERE ");  // append 새로운 요소 추가한다는 의미
            jpql.append("m.menuName LIKE '%' || :menuName || '%' ");
            jpql.append("AND ");
            jpql.append("m.categoryCode = :categoryCode ");
        } else {
            if (searchName != null && !searchName.isEmpty()) { // 두개 넘어온 경우
                jpql.append("WHERE ");
                jpql.append("m.menuName LIKE '%' || :menuName || '%' ");
            } else if (searchCategoryCode > 0) { // 하나만 넘어온 경우
                jpql.append("WHERE ");
                jpql.append("m.categoryCode = :categoryCode ");
            } // 아무것도 안넘어온 경우
            /* ======================================================== */
        } // *** 구문 완성하기
        TypedQuery<Menu> query = entityManager.createQuery(jpql.toString(), Menu.class);
        if (searchName != null && !searchName.isEmpty() && searchCategoryCode > 0) {
            query.setParameter("menuName", searchName);
            query.setParameter("categoryCode", searchCategoryCode);
        } else {
            if (searchName != null && !searchName.isEmpty()) {
                query.setParameter("menuName", searchName);
            } else if (searchCategoryCode > 0) {
                query.setParameter("categoryCode", searchCategoryCode);
            } // *** 완성한 구문 수행되도록 코드 작성하기
        }
        List<Menu> menuList = query.getResultList();
        //then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }
    /* 동적 jpql은 너무 번거롭다. 조건이 두개 뿐인데도 매우 길어진다.
     * 순수하게 jpql을 쓰면 번거롭고 별도의 라이브러리를 사용하는 것이 효율적이다. */



    @Test
    public void 네임드쿼리를_이용한_조회_테스트() {
        //when
        List<Menu> menuList = entityManager.createNamedQuery("menu_section08.selectMenuList", Menu.class) // Menu 엔티티에 기재한 name과 동일
                .getResultList();
        //then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }
}


