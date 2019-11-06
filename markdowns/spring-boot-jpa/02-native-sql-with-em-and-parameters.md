# Native SQL with EntityManager and parameters

有時候 SQL 並不是固定的，我們需要一些動態的參數。

## 使用 named parameters

在 Spring Boot 的 Native SQL 中使用 :parameterName 就代表著一個動態的參數。

> :parameterName 是 Hibernate 的 Named parameter 用法，每個 ORM library 可能有不同的實做方式，而 Spring Boot JPA 內建使用的就是 Hibernate，這邊也就如此舉例。

```java
String sql = "SELECT FIRST_NAME FROM USERS WHERE AGE > :age";
Query query = em.createNativeQuery(sql)
                .setParameter("age", 18);
List<String> queryResult = (List<String>) query.getResultList();
```

當然也適用在 update 系列的 SQL，一樣也是要用 executeUpdate() 來執行。

```java
String sql = "UPDATE USERS SET AGE = :newAge WHERE ID = :id";
Query query = em.createNativeQuery(sql)
                .setParameter("newAge", 29)
                .setParameter("id", 1);
query.executeUpdate();
```

## IN List 的簡潔用法

對於 IN clause Hibernate 有簡單的使用方法，不用自己兜 SQL。

我們可以直接使用 ```ID in :idList``` 並將 List<Long> idList 用前面的方法當作參數傳進去就可以了。

> 如果 idList 是 empty List，在最新版的 Hibernate 5.3 可以正常運行，但意思上就等同於 false。舊版的 Hibernate 有可能會壞掉，所以如果你確定你的 Hibernate 版本夠新就可以傳 empty List，否則還是檢查一下。
> 最好的方法就是在 Project 寫一個 test case 檢查這種類型的 SQL 能不能被接受，這樣以後升版出現不一致也會被 test case 發現。

```java
String sql = "SELECT FIRST_NAME FROM USERS WHERE ID IN :idList";
Query query = em.createNativeQuery(sql)
                .setParameter("idList", Arrays.asList(1, 2));
List<String> queryResult = (List<String>) query.getResultList();
```

## @Transactional

當你直接執行 executeUpdate() 的時候，可能會發現拋出了一個 Exception，告訴你 UPDATE 必須要有 transaction。

Transaction 後面會再詳細討論，這邊先說明如何解決這個問題。只需要在有 executeUpdate() 的 **public** method 上面標注 ```@Transactional``` 就可以讓這整個 method（除非特別再寫 Transactional annotation，否則連往下叫的子 method 都包含在內）都會被同一個 Transaction 包起來，這樣就可以正常執行 executeUpdate() 了。

```java
@Transactional
public void update() {
    em.createNativeQuery("UPDATE USERS SET AGE = 25 WHERE ID = 1").executeUpdate();
}
```

## 練習

我們有張 Table，初始內容如下:

Table Name: USERS

| ID<br>NUMBER | FIRST_NAME<br>VARCHAR(20) | LAST_NAME<br>VARCHAR(20) | AGE<br>NUMBER |
|:------------:|:-------------------------:|:------------------------:|:-------------:|
|       1      |            Nier           |           Wang           |       29      |
|       2      |             WL            |           Chang          |       20      |
|       3      |             SJ            |            Pig           |       18      |

請根據 TODO 完成 methods

@[Native SQL with EntityManager and parameters]({"stubs": ["src/main/java/com/example/training/service/Quiz02Service.java"], "command": "com.example.training.service.Quiz02ServiceTest#quiz02"})
