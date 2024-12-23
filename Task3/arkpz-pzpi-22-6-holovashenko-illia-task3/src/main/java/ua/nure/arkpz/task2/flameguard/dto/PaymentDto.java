package ua.nure.arkpz.task2.flameguard.dto;

import java.time.LocalDateTime;

public class PaymentDto {

    private Integer paymentId;

    private String paymentMethod;

    private String paymentStatus;

    private LocalDateTime paymentDateTime;

    private Integer maintenanceId;

    public PaymentDto() {}

    public PaymentDto(Integer paymentId, String paymentMethod, String paymentStatus, LocalDateTime paymentDateTime,
                      Integer maintenanceId) {
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentDateTime = paymentDateTime;
        this.maintenanceId = maintenanceId;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Integer getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(Integer maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public LocalDateTime getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(LocalDateTime paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }
}
