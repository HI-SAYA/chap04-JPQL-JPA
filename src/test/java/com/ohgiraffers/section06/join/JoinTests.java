package com.ohgiraffers.section06.join;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JoinTests {

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
    public void 내부조인을_이용한_조회_테스트() {
        //when
        String jpql = "SELECT m FROM menu_section06 m JOIN m.category c";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();
        //then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
        // 메뉴에 대한 조회가 먼저, 카테고리에 대한 조회가 나중에 필요할 때 일어난다.
        // 필요할 때 카테고리 대한 조회가 이뤄지는데 굉장히 많이 발생한다.
        // => inner join이 된다. 패치를 통해 해결할 수 있다.
    }


    @Test
    public void 외부조인을_이용한_조회_테스트() {
        //when
        String jpql = "SELECT m.menuName, c.categoryName FROM menu_section06 m RIGHT JOIN m.category c" + // 카테고리 엔티티 기준으로 조인을 하겠다.(RIGHT JOIN)
                     " ORDER BY m.category.categoryCode"; // 메뉴 엔티티에서 카테고리를 참조하고 거기에서 카테고리 코드로 order by 정렬 하겠다.
        List<Object[]> menuList = entityManager.createQuery(jpql, Object[].class).getResultList(); // Object[] 사용하는 이유 : m.menuName, c.categoryName 두가지(이상)을 조회하기 때문에
        //then
        assertNotNull(menuList);
        menuList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " ")); // 한우딸기국밥 한식 개행 앙버터김치찜 한식 개행
            System.out.println(); // 단순 개행구문
            // 실제 메뉴가 없는 경우(null 디저트 등)에도 조회가 된다 : outer join 했기 때문에
        });
    }

    @Test
    public void 컬렉션조인을_이용한_조회_테스트() {
        //when
        String jpql = "SELECT c.categoryName, m.menuName FROM category_section06 c LEFT JOIN c.menuList m";
        List<Object[]> categoryList = entityManager.createQuery(jpql, Object[].class).getResultList();
        // Object[] 사용하는 이유 : m.menuName, c.categoryName 두가지(이상)을 조회하기 때문에
        //then
        assertNotNull(categoryList);
        categoryList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println(); // 단순 개행구문
        });
    }


    @Test // (모든 경우의 수를 반환한다 = cross join)
    public void 세타조인을_이용한_조회_테스트() {
        String jpql = "SELECT c.categoryName, m.menuName FROM category_section06 c, menu_section06 m"; // FROM 절에 엔티티를 그냥 나열(JOIN X)
        List<Object[]> categoryList = entityManager.createQuery(jpql, Object[].class).getResultList();
        //then
        assertNotNull(categoryList);
        categoryList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println(); // 단순 개행구문
        });
        // from
        //        tbl_category category0_ cross          <- cross join
    }


    @Test
    public void 페치조인을_이용한_조회_테스트() { //fetch
        //when
        String jpql = "SELECT m FROM menu_section06 m JOIN FETCH m.category c"; // JOIN 뒤에 FETCH
        // FETCH가 없을 경우 MENU 먼저 조회하고 필요할 때 CATEGORY를 조회했는데(지연로딩) FETCH를 사용할 경우 한번에 JOIN되어 MENU와 CATEGORY가 같이 조회되어 나온다.
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();
        //then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }
}
