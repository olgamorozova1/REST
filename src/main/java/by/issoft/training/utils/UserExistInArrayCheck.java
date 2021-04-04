package by.issoft.training.utils;

import by.issoft.training.objects.User;


public class UserExistInArrayCheck {
    public static boolean checkIfUserExistInArray(User[] listOfUsers, User user) {
        for (User singleUser : listOfUsers) {
            if (singleUser.equals(user)) {
                return true;
            }
        }
        return false;
    }
}
