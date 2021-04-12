package by.issoft.training.objects;


import java.util.Objects;

public class UserDto {
    int age;
    String name;
    String sex;
    String zipCode;

    public UserDto() {
    }

    public UserDto(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }

    public UserDto(int age, String name, String sex) {
        this.age = age;
        this.name = name;
        this.sex = sex;
    }

    public UserDto(int age, String name, String sex, String zipCode) {
        this.age = age;
        this.name = name;
        this.sex = sex;
        this.zipCode = zipCode;
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

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return age == userDto.age &&
                Objects.equals(name, userDto.name) &&
                Objects.equals(sex, userDto.sex) &&
                Objects.equals(zipCode, userDto.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, name, sex, zipCode);
    }
}