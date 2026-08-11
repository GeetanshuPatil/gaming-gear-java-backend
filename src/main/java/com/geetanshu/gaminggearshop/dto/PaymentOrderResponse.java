package com.geetanshu.gaminggearshop.dto;

public class PaymentOrderResponse {

    private String orderId;
    private int amount;
    private String currency;
    private String keyId;

    public PaymentOrderResponse(
            String orderId,
            int amount,
            String currency,
            String keyId
    ) {
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.keyId = keyId;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getKeyId() {
        return keyId;
    }
}