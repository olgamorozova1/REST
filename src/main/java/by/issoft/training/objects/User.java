package by.issoft.training.objects;


import java.util.Objects;

public class User {
    int age;
    String name;
    String sex;
    String zipCode;

    public User(int age, String name, String sex, String zipCode) {
        this.age = age;
        this.name = name;
        this.sex = sex;
        this.zipCode = zipCode;
    }

    public User(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }

    public User() {
    }

    public User(int age, String name, String sex) {
        this.age = age;
        this.name = name;
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getZipCode() {
        return zipCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age &&
                Objects.equals(name, user.name) &&
                Objects.equals(sex, user.sex) &&
                Objects.equals(zipCode, user.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, name, sex, zipCode);
    }
}
