package com.micro.product.handler;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) {

}