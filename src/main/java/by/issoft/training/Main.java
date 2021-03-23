package by.issoft.training;

import by.issoft.training.authorization.Scope;
import by.issoft.training.authorization.TokenGenerator;

public class Main {
    public static void main(String[] args) {
        TokenGenerator tokenGenerator1 = TokenGenerator.getInstance(Scope.READ);
        TokenGenerator tokenGenerator2 = TokenGenerator.getInstance(Scope.WRITE);
        String tokenForReadRequest = tokenGenerator1.createAccessToken();
        String tokenForWriteRequest = tokenGenerator2.createAccessToken();
    }
}
