package com.example.training.entity;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.beans.PropertyDescriptor;

public class UsersTest {
    @Test
    public void checkEntity() {
        Class<?> testClass = Users.class;

        // Class
        Entity entityAnnotation = testClass.getAnnotation(Entity.class);
        Assert.assertNotNull(entityAnnotation);

        Table tableAnnotation = testClass.getAnnotation(Table.class);
        Assert.assertNotNull(tableAnnotation);
        Assert.assertEquals("USERS", tableAnnotation.name());

        // ID
        PropertyDescriptor idPd = BeanUtils.getPropertyDescriptor(testClass, "id");
        Assert.assertNotNull(idPd);
        Assert.assertTrue(Number.class.isAssignableFrom(idPd.getPropertyType()));

        Id idAnnotation = idPd.getReadMethod().getAnnotation(Id.class);
        Assert.assertNotNull(idAnnotation);

        GeneratedValue generatedValueAnnotation = idPd.getReadMethod().getAnnotation(GeneratedValue.class);
        Assert.assertNotNull(generatedValueAnnotation);
        Assert.assertEquals(GenerationType.AUTO, generatedValueAnnotation.strategy());

        Column columnAnnotation = idPd.getReadMethod().getAnnotation(Column.class);
        Assert.assertNotNull(columnAnnotation);
        Assert.assertEquals("ID", columnAnnotation.name());

        // FIRST_NAME
        PropertyDescriptor firstNamePd = BeanUtils.getPropertyDescriptor(testClass, "firstName");
        Assert.assertNotNull(firstNamePd);
        Assert.assertTrue(String.class.isAssignableFrom(firstNamePd.getPropertyType()));

        columnAnnotation = firstNamePd.getReadMethod().getAnnotation(Column.class);
        Assert.assertNotNull(columnAnnotation);
        Assert.assertEquals("FIRST_NAME", columnAnnotation.name());

        // LAST_NAME
        PropertyDescriptor lastNamePd = BeanUtils.getPropertyDescriptor(testClass, "lastName");
        Assert.assertNotNull(lastNamePd);
        Assert.assertTrue(String.class.isAssignableFrom(lastNamePd.getPropertyType()));

        columnAnnotation = lastNamePd.getReadMethod().getAnnotation(Column.class);
        Assert.assertNotNull(columnAnnotation);
        Assert.assertEquals("LAST_NAME", columnAnnotation.name());

        // AGE
        PropertyDescriptor agePd = BeanUtils.getPropertyDescriptor(testClass, "age");
        Assert.assertNotNull(agePd);
        Assert.assertTrue(Number.class.isAssignableFrom(agePd.getPropertyType()));

        columnAnnotation = agePd.getReadMethod().getAnnotation(Column.class);
        Assert.assertNotNull(columnAnnotation);
        Assert.assertEquals("AGE", columnAnnotation.name());
    }
}
