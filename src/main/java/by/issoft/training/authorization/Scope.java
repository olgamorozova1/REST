package by.issoft.training.authorization;

public enum Scope {
    READ("read"),
    WRITE("write");

    private String scopeStringValue;

    Scope(String scopeStringValue) {
        this.scopeStringValue = scopeStringValue;
    }

    public String getScopeStringValue() {
        return scopeStringValue;
    }
}
