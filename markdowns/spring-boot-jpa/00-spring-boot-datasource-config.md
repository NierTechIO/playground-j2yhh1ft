# Spring Boot Datasource Configuration

## 在 pom.xml 中引入所需要的 dependencies

第一個需要的就是為了 Spring Data JPA 而使用的 spring-boot-starter-data-jpa

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

第二的根據對應的資料庫，使用對應的 Dialect 和 JDBC。

Spring Data JPA 中使用的 Hibernate 已經內建一些 Dialect（h2, Oracle 都有），如果使用這些資料庫就不需要額外加入 Dialect，不支援的像是 SQLite 就要額外加。

JDBC 像是 Oracle 就需要 ojdbc 之類的 dependency，請根據各自的 Database 處理。

本次範例中使用到的 h2，需要的 pom.xml dependencies 則為

```xml
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
</dependency>
```

## 修改 application.properties

每個 DB 的值都不一樣，但基本上都是修改這幾個值，以本專案範例的 h2 為例：

```properties
spring.datasource.url=jdbc:h2:mem:h2test
spring.datasource.username=sa
spring.datasource.password=password
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

當然你也可以使用 jndi-name 之類的，則就可以使用 spring.datasource.jndi-name 來設定：

```properties
spring.datasource.jndi-name=java:jboss/datasources/YourDS
```

上面的步驟完成之後就可以在 Spring Boot 裡面連接 Datasource 了。