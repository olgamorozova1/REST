package by.issoft.training;

import by.issoft.training.Constants.Scope;

public class Main {
    public static void main(String[] args) {
        TokenGenerator tokenGenerator = TokenGenerator.getInstance(Scope.READ);
        TokenGenerator tokenGenerator2 = TokenGenerator.getInstance(Scope.WRITE);
    }
}
