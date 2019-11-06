# Entity

Entity 是用 Java POJO 來表示 DB table 的一種方法。POJO 的 fields 代表了該張 DB table 的 columns，而每一個 Entity instance 則對應 DB table 的一個 row data。

## 如何定義 Entity

1. 創一個 Java class，並在上面標注 ```@Entity```
2. 在 class 上面標注 ```@Table(name="TABLE_NAME")``` 來表示這個 Entity 對應 DB 的哪張 table
    * ```@Table``` 還可以設定 schema，可以視需求指定
3. 在 class 裡面新增 field，以及對應的 setter 和 getter
4. 在 getter 上面標注該 field 對應的是哪個 column，以及相關設定
    * ```@Column(name = "COLUMN_NAME")``` 用來表示該 field 對應的 column 名稱
    * 如果是日期，則要再加上 ```@Temporal(TemporalType.DATE)``` 或 ```@Temporal(TemporalType.TIMESTAMP)```，前者只有日期，後者包含日期與時間
    * 如果該欄位是 ID，則需要加上 ```@Id```
    * 如果該欄位是自動產生的值，則需要加上 ```@GeneratedValue```，並設定產生值的 strategy
         * 假設是 MySQL 或 H2 的 AUTO_INCREMENT，這邊可以寫上 ```@GeneratedValue(strategy=GenerationType.AUTO)```
         * 假設是用 Oracle 的 SEQUENCE 來實做，則要補上 ```@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GENERATOR_NAME")```
         * Generator 要再額外定義，例如 ```@SequenceGenerator(name="GENERATOR_NAME", sequenceName="SEQ_NAME" allocationSize=1, intialValue=1)```
5. Field 的 Type 根據 DB column type 決定
    * 字串: String
    * 整數: Long
    * 浮點數: Double
    * 日期: Date
    
> 這邊要注意的是，Entity 會自動幫你轉換型別，前面用 Native SQL 撈出來的數字其實是 BigDecimal type，但這邊你宣告 Long 或 Double 也沒問題，ORM 會自動幫你做轉換。

> 如果 POJO 裡面需要放一些儲存用不在 Table 的 field，可以將該 field 標註為 ```@Transient```，但個人不建議這樣做

```java
@Entity
@Table(name = "STUDENT")
public class Student {
    private Long id;
    private String name;
    private Date birthDate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // Oracle Sequence 用下面兩行的寫法
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_NAME")
    // @SequenceGenerator(name = "GENERATOR_NAME", sequenceName = "SEQ_NAME", allocationSize = 1, initialValue = 1)
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "NAME", length = 50, nullable = false, unique = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "BIRTH_DATE")
    @Temporal(TemporalType.DATE)
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
```

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
