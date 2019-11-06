package com.example.training.entity;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
public class UsersEntity {
    private Long id;
    private String firstName;
    private String lastName;
    private Long age;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "AGE")
    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }
}
