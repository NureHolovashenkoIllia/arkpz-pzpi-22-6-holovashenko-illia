package ua.nure.arkpz.task2.flameguard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.nure.arkpz.task2.flameguard.dto.PaymentDto;
import ua.nure.arkpz.task2.flameguard.service.PaymentService;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing payments.
 * Provides endpoints for CRUD operations on payments.
 */
@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "Endpoints for managing payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Retrieve all payments.
     *
     * @return List of all payments.
     */
    @Operation(summary = "Get all payments", description = "Retrieve a list of all payments.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of payments",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDto.class))),
    })
    @PreAuthorize("hasAnyAuthority('System_Administrator', 'Global_Administrator')")
    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        List<PaymentDto> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    /**
     * Retrieve payment for a specific maintenance.
     *
     * @param maintenanceId ID of the maintenance.
     * @return Payment information for the specified maintenance.
     */
    @Operation(summary = "Get payment by maintenance ID", description = "Retrieve payment information for a specific maintenance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved payment information",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDto.class))),
            @ApiResponse(responseCode = "404", description = "Payment not found for the specified maintenance ID",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasAnyAuthority('Customer', 'Global_Administrator')")
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

    /**
     * Create a new payment.
     *
     * @param paymentDto Payment details to create.
     * @return Details of the created payment.
     */
    @Operation(summary = "Create a new payment", description = "Add a new payment to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error",
                    content = @Content(mediaType = "application/json"))
    })
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

    /**
     * Update an existing payment.
     *
     * @param id ID of the payment to update.
     * @param updatedPaymentDto Updated payment details.
     * @return Details of the updated payment.
     */
    @Operation(summary = "Update a payment", description = "Modify details of an existing payment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentDto.class))),
            @ApiResponse(responseCode = "404", description = "Payment not found with the specified ID",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasAnyAuthority('System_Administrator', 'Global_Administrator')")
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

    /**
     * Delete a payment.
     *
     * @param id ID of the payment to delete.
     */
    @Operation(summary = "Delete a payment", description = "Remove a payment from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Payment successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Payment not found with the specified ID",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasAnyAuthority('Global_Administrator')")
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
