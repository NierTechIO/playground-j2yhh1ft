package com.example.training.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class Ans01Service {
    final private EntityManager em;

    @Autowired
    public Ans01Service(EntityManager em) {
        this.em = em;
    }

    @SuppressWarnings("unchecked")
    public List<String> quiz01() {
        return (List<String>) em.createNativeQuery("SELECT FIRST_NAME FROM USERS WHERE AGE > 18 ORDER BY ID ASC").getResultList();
    }
}
