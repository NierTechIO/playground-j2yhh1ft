# JPQL with Entity Manager

在上一章的範例中，我們已經學會如何創建 Entity。

Entity 可以讓我們把 DB 的 row 轉變成 POJO 來處理，我們可以執行 JPQL 來回傳 ```List<Entity>``` 而不是 ```List<Object[]>```，這樣可以減少很多手動轉換型別的時間。

## 使用 Entity Manager 來執行 JPQL

最直覺的做法就是直接使用 EntityManager 的 ```createQuery``` 來產生 JPQL query。

```java
List<UsersEntity> usersList =
        em.createQuery("SELECT u FROM UsersEntity u WHERE u.age >= :age", UsersEntity.class)
                .setParameter("age", 20L)
                .getResultList();
```

當然裡面也跟之前的 Native SQL 一樣可以使用 named parameters，也跟之前一樣使用 ```getResultList()``` 和 ```executeUpdate()``` 來取得結果。

## 使用 Entity Manager 的 methods 來做 CRUD

除了執行 JPQL，Entity Manager 也有許多 method 來對 Entity 做 CRUD。

### Create: persist



```java
UsersEntity usersEntity = new UsersEntity();
usersEntity.setFirstName("Hello");
usersEntity.setLastName("World");
usersEntity.setAge(35L);
em.persist(usersEntity);
```

## Managed Entity & Detached Entity



## 練習

試著將之前的 Table 寫成 Entity 吧

Table Name: USERS

Primary Key: ID, AUTO_INCREMENT

| ID<br>NUMBER | FIRST_NAME<br>VARCHAR(20) | LAST_NAME<br>VARCHAR(20) | AGE<br>NUMBER |
|:------------:|:-------------------------:|:------------------------:|:-------------:|
|       1      |            Nier           |           Wang           |       29      |
|       2      |             WL            |           Chang          |       20      |
|       3      |             SJ            |            Pig           |       18      |

@[Complete the Entity class]({"stubs": ["src/main/java/com/example/training/entity/Users.java"], "command": "com.example.training.entity.UsersTest#checkEntity"})
