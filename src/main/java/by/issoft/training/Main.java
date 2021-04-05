package by.issoft.training;

import by.issoft.training.authorization.Scope;
import by.issoft.training.authorization.TokenGenerator;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        TokenGenerator tokenGenerator = TokenGenerator.getInstance();
        Map<Scope, String> tokens = tokenGenerator.createAccessToken(Scope.READ);
        tokens.putAll(tokenGenerator.createAccessToken(Scope.WRITE));
    }
}
