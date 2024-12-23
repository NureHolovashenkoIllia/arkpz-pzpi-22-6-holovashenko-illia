package ua.nure.arkpz.task2.flameguard.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.arkpz.task2.flameguard.dto.PaymentDto;
import ua.nure.arkpz.task2.flameguard.dto.PaymentResponse;
import ua.nure.arkpz.task2.flameguard.service.PayPalService;
import ua.nure.arkpz.task2.flameguard.service.PaymentService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/payments")
public class PayPalController {

    @Autowired
    private PayPalService payPalService;
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/paypal")
    public ResponseEntity<?> createPayPalPayment(@RequestParam Double total, @RequestParam String currency,
                                                 @RequestParam String description, @RequestParam Integer maintenanceId) {
        try {
            String cancelUrl = "http://localhost:8080/api/payments/cancel";
            String successUrl = "http://localhost:8080/api/payments/success?maintenanceId=" + maintenanceId;

            Payment payment = payPalService.createPayment(total, currency, "paypal",
                    "sale", description, cancelUrl, successUrl);

            try {
                String approvalUrl = payPalService.getApprovalUrl(payment);
                return ResponseEntity.ok("{\"approvalUrl\":\"" + approvalUrl + "\"}");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/success")
    public ResponseEntity<?> executePayPalPayment(@RequestParam String paymentId, @RequestParam String PayerID,
                                                  @RequestParam Integer maintenanceId) {
        try {
            Payment payment = payPalService.executePayment(paymentId, PayerID);

            PaymentResponse response = new PaymentResponse(payment);

            PaymentDto paymentDto = new PaymentDto();
            String paymentMethod = payment.getPayer().getPaymentMethod();
            if ("paypal".equalsIgnoreCase(paymentMethod)) {
                paymentDto.setPaymentMethod("PayPal");
            } else {
                paymentDto.setPaymentMethod(paymentMethod);
            }
            paymentDto.setPaymentStatus("Completed");
            paymentDto.setPaymentDateTime(LocalDateTime.now());
            paymentDto.setMaintenanceId(maintenanceId);

            paymentService.createPayment(paymentDto);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<?> cancelPayPalPayment() {
        return ResponseEntity.ok("{\"message\":\"Payment canceled\"}");
    }
}
