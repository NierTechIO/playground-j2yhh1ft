package com.example.training.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Quiz01ServiceTest {
    @Autowired
    private Quiz01Service quiz01Service;

    @Test
    public void quiz01() {
        List<String> result = quiz01Service.quiz01();

        assertEquals(2, result.size());
        assertEquals("Nier", result.get(0));
        assertEquals("WL", result.get(1));
    }
}