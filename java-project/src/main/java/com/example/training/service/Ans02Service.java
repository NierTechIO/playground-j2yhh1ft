package com.example.training.service;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class Ans02Service {
    private EntityManager em;

    public Ans02Service(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public boolean quiz02_01(String firstName, String lastName, Long age) {
        try {
            Long newId = ((BigDecimal) em.createNativeQuery("SELECT MAX(ID) + 1 FROM USERS").getSingleResult()).longValue();
            int rowCount =
                    em.createNativeQuery("INSERT INTO USERS (ID, FIRST_NAME, LAST_NAME, AGE) VALUES (:id, :firstName, :lastName, :age)")
                            .setParameter("id", newId)
                            .setParameter("firstName", firstName)
                            .setParameter("lastName", lastName)
                            .setParameter("age", age)
                            .executeUpdate();

            return rowCount == 1;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> quiz02_02(List<Long> idList) {
        if (idList == null) {
            return Collections.emptyList();
        }

        Map<Long, Integer> idToIndexMap = IntStream.range(0, idList.size())
                .boxed()
                .collect(Collectors.toMap(idList::get, Function.identity()));

        List<Object[]> queryResult = em.createNativeQuery("SELECT ID, FIRST_NAME FROM USERS WHERE ID IN :idList")
                .setParameter("idList", idList)
                .getResultList();

        return queryResult.stream()
                .sorted(Comparator.comparingInt(row -> idToIndexMap.get(((BigDecimal) row[0]).longValue())))
                .map(row -> (String) row[1])
                .collect(Collectors.toList());
    }
}
