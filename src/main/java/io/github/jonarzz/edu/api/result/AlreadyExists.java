package io.github.jonarzz.edu.api.result;

public record AlreadyExists<T>(
        String entityName,
        String identifyingField,
        Object fieldValue
) implements FailedResult<T> {

    @Override
    public String getMessage() {
        return "%s with %s '%s' already exists".formatted(
                capitalize(entityName), identifyingField, fieldValue);
    }

    private static String capitalize(String value) {
        return value.substring(0, 1)
                    .toUpperCase()
               + value.substring(1);
    }
}
