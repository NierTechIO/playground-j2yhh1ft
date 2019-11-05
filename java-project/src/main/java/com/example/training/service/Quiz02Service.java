package com.example.training.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class Quiz02Service {
    public boolean quiz02_01(String firstName, String lastName, Long age) {
        /* TODO:
            insert new user to USERS
            remember to increase ID
         */
        return false;
    }

    public List<String> quiz02_02(List<Long> idList) {
        /* TODO:
            return firstName of USERS in idList, preserve the order of idList
            (if idList is [2, 1], then the order of returned List<String> should be [firstNameOfId2, firstNameOfId1])
            you can assume there will not be duplicated ids in idList
            remember to consider every possible input of idList
            always returns a List instead of other type of values
         */
        return Collections.emptyList();
    }
}
