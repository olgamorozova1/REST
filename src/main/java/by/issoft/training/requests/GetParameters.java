package by.issoft.training.requests;

public enum GetParameters {
    OlDER_THAN("olderThan"),
    YOUNGER_THAN("youngerThan"),
    SEX("sex");
    private String paramStringValue;

    GetParameters(String paramStringValue) {
        this.paramStringValue = paramStringValue;
    }

    public String getParamStringValue() {
        return paramStringValue;
    }
}
