package com.ohgiraffers.section01.simple;

import org.junit.jupiter.api.*;

import javax.persistence.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SimpleJPQLTests {

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
    public void TypedQuery를_이용한_단일메뉴_조회_테스트() {
        //when
        String jpql = "SELECT m.menuName FROM menu_section01 as m WHERE m.menuCode = 7";
        TypedQuery<String> query = entityManager.createQuery(jpql, String.class); // 실행할 구문, 반환 받을 데이터 타입
        // result 타입이 있다.
        String resultMenuName = query.getSingleResult();
        // String
        //then
        assertEquals("민트미역국", resultMenuName);
    }

    @Test
    public void Query를_이용한_단일메뉴_조회_테스트() {
        //when
        String jpql = "SELECT m.menuName FROM menu_section01 as m WHERE m.menuCode = 7";
        Query query = entityManager.createQuery(jpql);
        // result 타입이 없다.
        Object resultMenuName = query.getSingleResult();
        // Object
        //then
        assertEquals("민트미역국", resultMenuName);
    }


    @Test
    public void TypedQuery를_이용한_단일행_조회_테스트() {
        //when
        String jpql = "SELECT m FROM menu_section01 as m WHERE m.menuCode = 7"; // SELECT m = 엔티티 m 전체 컬럼을 조회한다.
        TypedQuery<Menu> query = entityManager.createQuery(jpql, Menu.class);
        Menu foundMenu = query.getSingleResult();
        //then
        assertEquals(7, foundMenu.getMenuCode());
        System.out.println(foundMenu);
    }


    @Test
    public void TypedQuery를_이용한_다중행_조회_테스트() {
        //when
        String jpql = "SELECT m FROM menu_section01 as m"; // SELECT m = 엔티티 m 전체 컬럼을 조회한다.
        TypedQuery<Menu> query = entityManager.createQuery(jpql, Menu.class);
        List<Menu> foundMenuList = query.getResultList(); // 여러 행 조회할 때
        //then
        assertNotNull(foundMenuList);
        foundMenuList.forEach(System.out::println);
    }


    @Test
    public void Query를_이용한_다중행_조회_테스트() {
        //when
        String jpql = "SELECT m FROM menu_section01 as m"; // SELECT m = 엔티티 m 전체 컬럼을 조회한다.
        Query query = entityManager.createQuery(jpql);
        List<Menu> foundMenuList = query.getResultList(); // 여러 행 조회할 때
        //then
        assertNotNull(foundMenuList);
        foundMenuList.forEach(System.out::println);
    }


    @Test
    public void distinct를_활용한_중복제거_여러_행_조회_테스트() {
        //when
        String jpql = "SELECT DISTINCT m.categoryCode FROM menu_section01 m";
        TypedQuery<Integer> query = entityManager.createQuery(jpql, Integer.class);
        List<Integer> categoryCodeList = query.getResultList();
        //then
        assertNotNull(categoryCodeList);
        categoryCodeList.forEach(System.out::println);
    }


    @Test
    public void in_연산자를_활용한_조회_테스트() {
        // 카테고리 코드가 6이거나 10인 menu
        //when
        String jpql = "SELECT m FROM menu_section01 as m WHERE categoryCode IN (6, 10)";
//        Query query = entityManager.createQuery(jpql);        // 내 답
//        List<Menu> menuList = query.getResultList();
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList(); // 쌤 답
        //then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }


    @Test
    public void like_연산자를_활용한_조회_테스트() {
        // "마늘"이 메뉴 이름으로 들어간 menu 조회
        //when
        String jpql = "SELECT m FROM menu_section01 as m WHERE menuName LIKE '%마늘%'";
//        TypedQuery<Menu> query = entityManager.createQuery(jpql, Menu.class);     // 내 답
//        List<Menu> menuList = query.getResultList();
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();  // 쌤 답
        //then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }
}
