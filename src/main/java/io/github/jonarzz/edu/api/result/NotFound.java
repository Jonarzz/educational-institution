package io.github.jonarzz.edu.api.result;

public record NotFound<T>(
        String entityName,
        String identifyingField,
        Object fieldValue
) implements FailedResult<T> {

    @Override
    public String getMessage() {
        return "Not found %s with %s '%s'".formatted(
                entityName.toLowerCase(), identifyingField, fieldValue);
    }

    @Override
    public <M> Result<M> mapFailure() {
        return new NotFound<>(
                entityName,
                identifyingField,
                fieldValue
        );
    }
}
