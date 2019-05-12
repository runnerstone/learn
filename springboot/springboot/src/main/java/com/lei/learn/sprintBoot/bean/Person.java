package com.lei.learn.sprintBoot.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 将配置文件中的每一个属性值，映射到这个组件中
 *
 * @ConfigurationProperties,告诉spring boot将奔雷中的所有属性和配置文件中相关的配置进行绑定
 * prefix="person":配置文件中哪个下面的所有属性进行一一映射
 * <p>
 * 只有这个组件在容器中，才能使用容器中提供的@ConfigurationProperties功能
 */
@Component
@ConfigurationProperties(prefix = "person")
public class Person {
  private String Name;
  private Integer age;
  private Date birth;
  private Map<String, Object> maps;
  private List<Object> lists;
  private Dog dog;

  @Override
  public String toString() {
    return "Person{" + "Name='" + Name + '\'' + ", age=" + age + ", birth=" + birth + ", map="
        + maps + ", list=" + lists + ", dog=" + dog + '}';
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public Date getBirth() {
    return birth;
  }

  public void setBirth(Date birth) {
    this.birth = birth;
  }

  public Map<String, Object> getMap() {
    return maps;
  }

  public void setMap(Map<String, Object> map) {
    this.maps = map;
  }

  public List<Object> getList() {
    return lists;
  }

  public void setList(List<Object> list) {
    this.lists = list;
  }

  public Dog getDog() {
    return dog;
  }

  public void setDog(Dog dog) {
    this.dog = dog;
  }
}
