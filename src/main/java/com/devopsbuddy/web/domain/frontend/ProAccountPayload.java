package com.devopsbuddy.web.domain.frontend;

/**
 * Created by tedonema on 24/04/2016.
 */
public class ProAccountPayload extends BasicAccountPayload {

    private String cardNumber;
    private String cardCode;
    private String cardMonth;
    private String cardYear;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardMonth() {
        return cardMonth;
    }

    public void setCardMonth(String cardMonth) {
        this.cardMonth = cardMonth;
    }

    public String getCardYear() {
        return cardYear;
    }

    public void setCardYear(String cardYear) {
        this.cardYear = cardYear;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString() + "ProAccountPayload{");
        sb.append("cardNumber='").append(cardNumber).append('\'');
        sb.append(", cardCode='").append(cardCode).append('\'');
        sb.append(", cardMonth='").append(cardMonth).append('\'');
        sb.append(", cardYear='").append(cardYear).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
