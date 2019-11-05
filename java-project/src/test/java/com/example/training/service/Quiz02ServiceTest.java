package com.example.training.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Quiz02ServiceTest {
    @Autowired
    private EntityManager em;

    @Autowired
    private Quiz02Service quiz02Service;

    @Test
    public void quiz02() {
        quiz02_01();
        quiz02_02();
    }

    @SuppressWarnings("unchecked")
    private void quiz02_01() {
        // check insert
        boolean insertResult = quiz02Service.quiz02_01("BS", "He", 30L);
        Assert.assertTrue(insertResult);
        List<Object[]> queryResult = (List<Object[]>) em.createNativeQuery("SELECT ID, FIRST_NAME, LAST_NAME, AGE FROM USERS ORDER BY ID DESC").getResultList();
        Assert.assertEquals(4, queryResult.size());
        Object[] newRecord = queryResult.get(0);
        Assert.assertEquals(4, ((BigDecimal) newRecord[0]).longValue());
        Assert.assertEquals("BS", newRecord[1]);
        Assert.assertEquals("He", newRecord[2]);
        Assert.assertEquals(30, ((BigDecimal) newRecord[3]).longValue());
    }

    private void quiz02_02() {
        List<String> firstNames = quiz02Service.quiz02_02(Arrays.asList(2L, 3L));
        Assert.assertEquals(2, firstNames.size());
        Assert.assertEquals("WL", firstNames.get(0));
        Assert.assertEquals("SJ", firstNames.get(1));

        List<String> firstNames2 = quiz02Service.quiz02_02(Arrays.asList(2L, 3L, 9L, 1L));
        Assert.assertEquals(3, firstNames2.size());
        Assert.assertEquals("WL", firstNames2.get(0));
        Assert.assertEquals("SJ", firstNames2.get(1));
        Assert.assertEquals("Nier", firstNames2.get(2));

        List<String> firstNames3 = quiz02Service.quiz02_02(Collections.emptyList());
        Assert.assertEquals(0, firstNames3.size());

        List<String> firstNames4 = quiz02Service.quiz02_02(null);
        Assert.assertEquals(0, firstNames4.size());
    }
}
