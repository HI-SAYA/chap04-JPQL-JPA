package com.ohgiraffers.section05.groupfunction;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GroupFunctionTests {

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
    public void 특정_카테고리의_등록된_메뉴_수_조회() {
        //given
        int categoryCodeParameter = 4;
        //when
        String jpql = "SELECT COUNT(m.menuPrice) FROM menu_section05 m WHERE m.categoryCode = :categoryCode";
        long countOfMenu = entityManager.createQuery(jpql, Long.class)
                .setParameter("categoryCode", categoryCodeParameter)
                .getSingleResult();
        //then
        assertTrue(countOfMenu >= 0);
        System.out.println(countOfMenu);    // 카테코리 코드가 4인 메뉴는 6개
    }


    @Test
    public void count를_제외한_다른_그룹함수의_조회결과가_없는_경우_테스트() {
        //given
        int categoryCodeParameter = 1;
        //when
        String jpql = "SELECT SUM(m.menuPrice) FROM menu_section05 m WHERE m.categoryCode = :categoryCode";
        //then
        assertThrows(NullPointerException.class, () -> { // 이 구문을 실행하면 Exception이 발생할 것이다.
            long sumOfPrice = entityManager.createQuery(jpql, Long.class)
                .setParameter("categoryCode", categoryCodeParameter)
                .getSingleResult();
        });
        // 발생할 예상 Exception 타입, {실행할 구문}
        // categoryCode = 1 인 0개 이기 때문에 SUM을 사용하면 Null이 나올 것이다. -> assertThrows를 사용하여 Exception 대비

        assertDoesNotThrow(() -> {
            Long sumOfPrice = entityManager.createQuery(jpql, Long.class) // 객체타입 Long으로 Exception 대비 ****
                    .setParameter("categoryCode", categoryCodeParameter)
                    .getSingleResult();
            System.out.println(sumOfPrice);
        });
        // Exception이 발생하지 않았다. null이라는 값이 출력된다.
    }


    @Test
    public void groupby절과_having절을_사용한_조회_테스트() {
        //given
        long minPrice = 50000L;
        //when
        String jpql = "SELECT m.categoryCode, SUM(m.menuPrice)" +
                     " FROM menu_section05 m" +
                     " GROUP BY m.categoryCode" +
                     " HAVING SUM(m.menuPrice) >= :minPrice";
        // 구문과 구문 사이의 개행 했을 때 띄어쓰기 반드시 필요 => 전체적으로 문자열 하나라는 것 기억하기
        // categoryCode 기준으로 menu_section05 안에 있는 것들을 조회하는데 menuPrice 기준으로 SUM을 구할건데 :minPrice(50000)인 이상인 것을 조회

        List<Object[]> sumPriceOfCategoryList = entityManager.createQuery(jpql, Object[].class)
                .setParameter("minPrice", minPrice)
                .getResultList();
        //then
        assertNotNull(sumPriceOfCategoryList);
        sumPriceOfCategoryList.forEach(row -> {
            Arrays.stream(row).forEach(System.out::println);
        });
    }
}
