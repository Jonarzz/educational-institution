package io.github.jonarzz.edu.domain.common.result;

import io.github.jonarzz.edu.api.*;

public enum RejectionReason implements FailedResult {

    ENTITY_NOT_FOUND,
    ALREADY_EXISTS

}
