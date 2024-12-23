package ua.nure.arkpz.task2.flameguard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.arkpz.task2.flameguard.dto.PaymentDto;
import ua.nure.arkpz.task2.flameguard.entity.Payment;
import ua.nure.arkpz.task2.flameguard.entity.Maintenance;
import ua.nure.arkpz.task2.flameguard.repository.PaymentRepository;
import ua.nure.arkpz.task2.flameguard.repository.MaintenanceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    // Retrieve all payments
    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToPaymentDto)
                .toList();
    }

    // Retrieve payment for a specific maintenance
    public Optional<PaymentDto> getPaymentByMaintenanceId(int maintenanceId) {
        return paymentRepository.findPaymentByMaintenance_MaintenanceId(maintenanceId)
                .map(this::convertToPaymentDto);
    }

    // Create a new payment
    public Optional<PaymentDto> createPayment(PaymentDto paymentDto) {
        validatePaymentDto(paymentDto);

        Optional<Maintenance> maintenance = paymentDto.getMaintenanceId() != null
                ? maintenanceRepository.findById(paymentDto.getMaintenanceId())
                : Optional.empty();

        Payment payment = new Payment();
        payment.setPaymentMethod(paymentDto.getPaymentMethod());
        payment.setPaymentStatus(paymentDto.getPaymentStatus());
        payment.setPaymentDateTime(paymentDto.getPaymentDateTime());
        maintenance.ifPresent(payment::setMaintenance);

        Payment savedPayment = paymentRepository.save(payment);
        return Optional.of(convertToPaymentDto(savedPayment));
    }

    // Update an existing payment
    public Optional<PaymentDto> updatePayment(int id, PaymentDto updatedPaymentDto) {
        return paymentRepository.findById(id)
                .map(payment -> {
                    payment.setPaymentMethod(updatedPaymentDto.getPaymentMethod());
                    payment.setPaymentStatus(updatedPaymentDto.getPaymentStatus());
                    payment.setPaymentDateTime(updatedPaymentDto.getPaymentDateTime());

                    if (updatedPaymentDto.getMaintenanceId() != null) {
                        Optional<Maintenance> maintenance = maintenanceRepository
                                .findById(updatedPaymentDto.getMaintenanceId());
                        maintenance.ifPresent(payment::setMaintenance);
                    } else {
                        payment.setMaintenance(null);
                    }

                    validatePaymentDto(convertToPaymentDto(payment));

                    Payment savedPayment = paymentRepository.save(payment);
                    return convertToPaymentDto(savedPayment);
        });
    }

    // Delete a payment
    public boolean deletePayment(int id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Validate payment fields
    private void validatePaymentDto(PaymentDto paymentDto) {
        if (!paymentDto.getPaymentMethod().matches("Credit Card|PayPal|Cash")) {
            throw new IllegalArgumentException("Invalid payment method: " + paymentDto.getPaymentMethod());
        }
        if (!paymentDto.getPaymentStatus().matches("Pending|Completed|Failed")) {
            throw new IllegalArgumentException("Invalid payment status: " + paymentDto.getPaymentStatus());
        }
    }

    private PaymentDto convertToPaymentDto(Payment payment) {
        return new PaymentDto(
                payment.getPaymentId(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getPaymentDateTime(),
                payment.getMaintenance() != null ? payment.getMaintenance().getMaintenanceId() : null
        );
    }
}
