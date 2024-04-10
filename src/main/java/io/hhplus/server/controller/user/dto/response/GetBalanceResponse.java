package io.hhplus.server.controller.user.dto.response;

import java.math.BigDecimal;

public record GetBalanceResponse(
        BigDecimal balance
) {
}
