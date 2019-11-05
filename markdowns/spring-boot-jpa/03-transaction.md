# Transaction

Transaction 是用來管理 SQL 執行的相依性。如果有一串 SQL 處理（主要是 UPDATE 相關）需要同進同退，其中一個失敗，之前所下的 SQL update 要全部取消（rollback），則可以將這幾串 SQL 包再同一個 transaction 中。

在同一個 Transaction 中的所有 SQL，在最後會有兩種可能
* commit: 整個 Transaction 裡面所有執行的修改真正的寫進 DB
* rollback: 整個 Transaction 裡面所有執行過的修改全部取消

不過要注意的是，在同一個 Transaction 中，後面的 SQL 可以讀取到前面的 SQL update 的資料，但是在 commit 之前，其他不同的 Transaction 是讀不到這些修改的。

下面舉一個例子

```sql
INSERT INTO USERS (ID, NAME) VALUES (1, 'Nier');
-- 成功 Insert
SELECT * FROM USERS WHERE ID = 1;
-- 上面這一句，如果在同一個 Transaction 中話，可以撈到值
-- 如果上面這句是在另一個 Transaction 中執行的話，就撈不到這個新的值
INSERT INTO USERS (ID, NAME) VALUES (1, 'Yoyo');
-- Insert 失敗，因為 ID 重複
-- 如果因為上面這個 Insert 失敗導致了 rollback，則沒有任何值會被塞進去 DB
```

這些對於實際應用中是非常常見的，例如我們要儲存使用者的資料，我們可能會操作不同的 Table，把使用者的資料存到不同的 Table 中。如果其中一個 SQL 因為某些原因失敗了，我們不希望將存到一半的資料留在 DB 中，不然可能會造成資料不一致甚至錯誤的狀況。

例如一個管理借書的圖書系統，我們可能會先將 BOOK 的 IS_BORROWED 設成 'Y'，然後對 USER 的 BORROW_BOOK_ID 設定為借出書本的 ID。如果我們成功的將 IS_BORROWED 設成 'Y'，但是在插入一筆 USER 的 BORROW_BOOK_ID 的時候出錯了，如果沒有使用 transaction 來做 rollback，就會變成書已經被借出，但是找不到被誰借走的狀況。

然而我們需要非常小心 Transaction 的 scope。例如寫 log → 寄信，如果寄信失敗要 rollback，但只 rollback 這次寄信的 UPDATE SQL，而不要把之前成功寄信的 UPDATE SQL 都一起 rollback 了。

## Transaction control in Spring Boot

在 Spring Boot 提供了非常方便的方法來管理 Transaction。

預設 Spring Boot 都不會自動幫你創建 Transaction，所以前面幾章的練習中，如果沒有 Transaction 來做 UPDATE 的時候就會拋出 Exception。

而就像前面提到的，只要在 method 上面標注 ```@Transactional```，就可以把整個 method 用一個 Transaction 包起來了。

預設的情況，假設進入 method 沒有 Transaction，透過這種方法創建的 Transaction 起始點就是從 method 開始，然後到 method 結束的時候做 commit 或 rollback。

然而根據不同的設定，Transaction 的管理模式也不一樣。

### Transaction propagation modes

Transaction propagation mode 分成以下 6 種。

* REQUIRED（預設值）
    * 原本沒有 Transaction: 創一個新的
    * 原本有 Transaction: 沿用舊的
* REQUIRES_NEW
    * 原本沒有 Transaction: 創一個新的
    * 原本有 Transaction: 暫停原本的 Transaction，並創一個新的

> 暫停 Transaction 的意思是，新的 Transaction 和原本的 Transaction 不同，而且在離開現在的 method，把 Transaction commit / rollback 之後，會把原本的 Transaction 恢復並繼續執行。
> 例如有可能發生 A → B (REQUIRES_NEW) → commit B → rollback A 的情況，B 裡面的 UPDATE 已經 commit 出去，但外面那層的 A 被 rollback

* MANDATORY
    * 原本沒有 Transaction: 拋出 Exception
    * 原本有 Transaction: 延用舊的
* SUPPORTS
    * 原本沒有 Transaction: 不產生新的 Transaction，也就是保持著沒有 Transaction 的狀況
    * 原本有 Transaction: 延用舊的
* NOT_SUPPORTED
    * 原本沒有 Transaction: 不產生新的 Transaction，也就是保持著沒有 Transaction 的狀況
    * 原本有 Transaction: 暫停原本的 Transaction，以沒有 Transaction 的狀況繼續執行，並在 method 結束後恢復原本的 Transaction
* NEVER
    * 原本沒有 Transaction: 不產生新的 Transaction，也就是保持著沒有 Transaction 的狀況
    * 原本有 Transaction: 拋出 Exception

當我們需要 Transaction 的時候，比較常用的就是 ```REQUIRED``` 和 ```REQUIRES_NEW```。我們要特別注意哪些 SQL UPDATE 要同進同出，謹慎的使用。

當我們不需要 Transaction 的情況，例如只有 SELECT 之類的時候，可以使用 ```SUPPORTS```，或乾脆不要寫 ```@Transaction```。

```NOT_SUPPORT``` 和 ```NEVER``` 用在你真的不想要有 Transaction，```NOT_SUPPORT``` 主要用在現在有可能產生 error，但不想要影響到之前的 Transaction 的情況。

### 什麼時候會 commit？什麼時候會 rollback？

當一個 Transaction 結束的時候就會 commit 或 rollback，主要是看 Transaction 有沒有途中被標記為 rollback。

在預設的情況下，當在一個被標注 ```@Transaction``` 的 method 中朝外拋出 Unchecked Exception 的時候，就會將 Transaction 標記為 rollback。

相反的，如果是拋出 checked Exception，就不會標記為 rollback。

一但 Transaction 被標記為 rollback 就沒救了，在最後如果想要 commit 他就會出現 ```UnexpectedRollbackException```。

因此，如果沒有被標記為 ```@Transactional``` 的 method 拋出了 Unchecked Exception，即便他其實身處於一個 Transaction 中（call 他的人有 Transaction 之類的），也不會標記為 rollback。

Spring Boot 會在拋出 Exception 的時候檢查該 method 有沒有被標記為 ```@Transactional```，有的話才會把現存的 Transaction 標記為 rollback。

如果想要特別指定拋出哪些 Unchecked Exception 的時候不要 rollback，可以在 ```@Transcational``` 標注中加上 ```noRollbackFor```。

相反的，如果想要指定拋出哪些 Checked Exception 的時候也要 rollback，可以加上 ```rollbackFor```。

```java
// 如果拋出 NumberFormatException 則不 rollback；若拋出 IllegalAccessException 則要 rollback
@Transactional(noRollbackFor = {NumberFormatException.class}, rollbackFor = {IllegalAccessException.class})
``` 

### 手動 rollback 的方法

```java
TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
```

### Transaction 的 isolation

* READ_UNCOMMITED
    * 不同的 Transaction 間可以拿到彼此 UPDATE 的資料，就物還沒有被 committed。
    * 當 Transaction T1 去 SELECT / UPDATE Table A1，Transaction T2 也可以去 SELECT / UPDATE Table A1。
    * 當 T1 去 Update A1 之後，即使 T1 還沒 commit，T2 就可以 SELECT 到 A1 新的資料，但是問題是如果 T1 之後 rollback 了，T2 手中留著那筆 A1 的資料就是錯誤的。

* READ_COMMITTED (預設)
    * 不同的 Transaction 間可以拿到彼此已經 committed 的資料。
    * 當 Transaction T1 去 SELECT / UPDATE Table A1，Transaction T2 也可以去 SELECT / UPDATE Table A1。
    * 而在 T1 去 Update A1 之後，T1 可以拿到 Update 之後的結果，而在 T1 commit 之前，T2 是無法獲得 T1 的 Update 結果，要等到 T1 commit 之後，T2 再去 SELECT 就可以拿到新的資料。

* REPEATABLE_READ
    * 不同的 Transaction 間無法拿到彼此 UPDATE 的資料。
    * 當 Transaction T1 去 SELECT / UPDATE Table A1，Transaction T2 也可以去 SELECT / UPDATE Table A1。
    * 然而在同一個 Transaction 中的 READ 除非被**自己**修改，否則不管其他的 transaction 有沒有動過，有沒有 commit，都不會影響到 READ 的值。

* SERIALIZABLE
    * Transaction 對 Table 的 WRITE 會 lock Table 直到 commit。
    * 因此可以保證所有的資料都不會是 dirty，但效率最差。

### Spring Boot 的 AOP 實現對 @Transactional 的影響

Spring Boot 對於 Transaction 的處理是用 AOP 來實現的（AOP 在其他地方會詳細說明），因此要特別注意，**在同一個 class 中呼叫另一個 method 並不會觸發 ```@Transactional``` 的設定**。

```java
public class ClassA() {
    final private EntityManager em;
    
    public ClassA(EntityManager em) {
        this.em = em;
    }

    public void methodA() {
        methodB();
    }
    
    @Transactional
    public void methodB() {
        em.createNativeQuery("INSERT INTO USER (ID, NAME) VALUES (1, 'Nier')");
    }
}
```

像上面的 code，假設我們呼叫 ```classA.methodA()``` 會拋出 ```TransactionRequiredException```，因為從同一個 class 中的 methodA 呼叫 methodB 並不會觸發 ```@Transactional```，因此沒有 Transaction。

如果直接呼叫 ```classA.methodB()```，就會觸發 ```@Transactional``` 並正常執行。

## 為什麼沒有 Transaction 也能做 Read Only 的 SQL，像是 SELECT？

其實不管你有沒有宣告 Transaction，任何的 DB SQL 都會被包在 Transaction 裡面。假設你沒有告訴 DB 什麼時候開始 Transaction，什麼時候結束，每一個 SQL statement 都會被各自獨立的 Transaction 包起來。

所以，照理說 UPDATE 的 SQL 也應該可以被 DB 執行的，但 Spring Boot 的 Transaction handler 在 proxy 層會檢查這件事情並拋出 Exception。

## 練習

> 試著想想看，在什麼情況下會用到哪些 Transaction propagation modes？各想出一個例子，並說服自己這個模式最適合這個範例。
