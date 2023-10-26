package com.ohgiraffers.section07.subquery;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubqeuryTests {

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
    public void 서브쿼리를_이용한_메뉴_조회_테스트() {
        // select,from절에서는 서브쿼리 사용 불가능(인라인뷰 불가능) / where, having 절에서만 사용 가능
        //given
        String categoryNameParameter = "한식"; // join을 하거나 subquery를 이용해서 정보 조회
        //when
        String jpql = "SELECT m FROM menu_section07 m WHERE m.categoryCode" +
                " = (SELECT c.categoryCode FROM category_section07 c WHERE c.categoryName = :categoryName)";
        // => 4번 카테고리를 가지고 있는 menu 목록을 조회하겠다.
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                .setParameter("categoryName", categoryNameParameter)
                .getResultList();
        //then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }

}
