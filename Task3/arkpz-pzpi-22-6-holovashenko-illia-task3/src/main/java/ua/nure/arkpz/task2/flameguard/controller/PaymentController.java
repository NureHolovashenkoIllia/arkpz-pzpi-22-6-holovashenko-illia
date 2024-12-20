package ua.nure.arkpz.task2.flameguard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.arkpz.task2.flameguard.dto.PaymentDto;
import ua.nure.arkpz.task2.flameguard.service.PaymentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Retrieve all payments
    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        List<PaymentDto> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    // Retrieve payment for a specific maintenance
    @GetMapping("/maintenance/{maintenanceId}")
    public ResponseEntity<?> getPaymentByMaintenanceId(@PathVariable int maintenanceId) {
        Optional<PaymentDto> payment = paymentService.getPaymentByMaintenanceId(maintenanceId);

        if (payment.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(payment);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\":\"No payment found for maintenance with id - " +
                        maintenanceId + "\"}");
    }

    // Create a new payment
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentDto paymentDto) {
        try {
            Optional<PaymentDto> createdPayment = paymentService.createPayment(paymentDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createdPayment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // Update an existing payment
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayment(@PathVariable int id,
                                           @RequestBody PaymentDto updatedPaymentDto) {
        try {
            Optional<PaymentDto> updatedPayment =
                    paymentService.updatePayment(id, updatedPaymentDto);
            return ResponseEntity.ok(updatedPayment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // Delete a payment
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable int id) {
        try {
            paymentService.deletePayment(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
