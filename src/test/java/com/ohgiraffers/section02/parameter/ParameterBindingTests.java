package com.ohgiraffers.section02.parameter;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParameterBindingTests {

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
    public void 이름_기준_파라미터_바인딩_메뉴_목록_조회_테스트() {
        //given
        String menuNameParameter = "한우딸기국밥"; // 바인딩
        //when
        String jpql = "SELECT m FROM menu_section02 m WHERE m.menuName = :menuName"; // 바인딩
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                .setParameter("menuName", menuNameParameter)    // 네임 파라미터 바인딩
                .getResultList();
        //then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }


    @Test
    public void 위치_기준_파라미터_바인딩_메뉴_목록_조회_테스트() {
        //given
        String menuNameParameter = "한우딸기국밥"; // 바인딩
        //when
        String jpql = "SELECT m FROM menu_section02 m WHERE m.menuName = ?1"; // 바인딩
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                .setParameter(1, menuNameParameter)    // 포지션 파라미터 바인딩
                .getResultList();
        //then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }
}
