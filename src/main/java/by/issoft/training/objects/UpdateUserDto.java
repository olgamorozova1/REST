package by.issoft.training.objects;

public class UpdateUserDto {

    UserDto userNewValues;
    UserDto userToChange;

    public UpdateUserDto(UserDto userNewValues, UserDto userToChange) {
        this.userNewValues = userNewValues;
        this.userToChange = userToChange;
    }

    public UserDto getUserNewValues() {
        return userNewValues;
    }

    public UserDto getUserToChange() {
        return userToChange;
    }

    public void setUserNewValues(UserDto userNewValues) {
        this.userNewValues = userNewValues;
    }

    public void setUserToChange(UserDto userToChange) {
        this.userToChange = userToChange;
    }
}
