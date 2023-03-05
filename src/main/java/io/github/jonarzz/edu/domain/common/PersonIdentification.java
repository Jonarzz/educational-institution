package io.github.jonarzz.edu.domain.common;

public record PersonIdentification(
        // simplified - normally could be a pair of
        // an ID and the ID type (e.g. national ID, passport etc.)
        String nationalIdNumber
) {

}
