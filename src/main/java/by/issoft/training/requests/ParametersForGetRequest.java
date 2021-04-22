package by.issoft.training.requests;

public enum ParametersForGetRequest {
    OLDER_THAN("olderThan"),
    YOUNGER_THAN("youngerThan"),
    SEX("sex");
    private String paramStringValue;

    ParametersForGetRequest(String paramStringValue) {
        this.paramStringValue = paramStringValue;
    }

    public String getParamStringValue() {
        return paramStringValue;
    }
}
