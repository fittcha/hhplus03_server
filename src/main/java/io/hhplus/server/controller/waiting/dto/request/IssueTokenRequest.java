package io.hhplus.server.controller.waiting.dto.request;

import jakarta.validation.constraints.NotNull;

public record IssueTokenRequest(
        @NotNull Long userId
) {
}
