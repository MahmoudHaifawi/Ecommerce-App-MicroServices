package com.micro.order.orderLine;

public record OrderLineResponse(
        Integer id,
        double quantity
) { }