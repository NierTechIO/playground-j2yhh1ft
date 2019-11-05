# Native SQL with EntityManager

## 在 Bean 中注入 EntityManager

我們可以在一個 Spring Bean 中用 DI 的方法注入 EntityManager。

```java
@Service
public class Example {
    final private EntityManager em;
    
    @Autowired
    public Example(EntityManager em) {
        this.em = em;
    }
}
```

## 使用 EntityManager 執行 Native SQL

我們可以使用 EntityManager 的 createNativeQuery 來準備 Native SQL 的 Query，這時候還沒有真正的執行 SQL。

```java
Query query = em.createNativeQuery("SELECT FIRST_NAME, LAST_NAME FROM USERS WHERE AGE > 25");
```

根據 SQL 的內容，使用不同的方法來執行。
* 例如如果是 SELECT，我們可以使用 getResultList()，或是 getSingleResult()，建議是都直接用 getResultList() 就好
* 如果是 UPDATE / INSERT / DELETE，則必須要使用 executeUpdate()，回傳值是這個 query 影響到的 row count

```java
// getResultList() 回傳的是 List，必須透過 unchecked type cast 變成 List<Object[]>
// 而且因為是 unchecked type cast，會有 warning，我們可以加上 @SuppressWarnings("unchecked") 來去除 warning message
@SuppressWarnings("unchecked")
List<Object[]> queryResult = (List<Object[]>) query.getResultList();
// 回傳的 Object[] 代表的一個 row 的資料，每一個 column 對應 array 裡面的每個值
for (Object[] row : queryResult) {
    // SELECT FIRST_NAME, LAST_NAME
    String firstName = (String) row[0];
    String lastName = (String) row[1];
}

// 如果你的 SQL 回傳是只有一個值，則 List 就是 List<T>，例如假設我們 SQL 只回傳 FIRST_NAME，可以 type cast 成 List<String>
@SuppressWarnings("unchekced")
List<String> queryResult = (List<String>) query.getResultList();
```

下面則是假設 Query 是 Update 類型的用法。

```java
int rowCount = query.executeUpdate();
```

## 練習

我們有張 Table，內容如下:

| ID<br>NUMBER | FIRST_NAME<br>VARCHAR(20) | LAST_NAME<br>VARCHAR(20) | AGE<br>NUMBER |
|:------------:|:-------------------------:|:------------------------:|:-------------:|
|       1      |            Nier           |           Wang           |       29      |
|       2      |             WL            |           Chang          |       20      |
|       3      |             SJ            |            Pig           |       18      |

請補完以下的 Class
* 注入 EntityManager
* 根據 TODO 完成 quiz01 method

@[Native SQL with EntityManager]({"stubs": ["src/main/java/com/example/training/service/Quiz01Service.java"], "command": "com.example.training.service.Quiz01ServiceTest#quiz01"})
