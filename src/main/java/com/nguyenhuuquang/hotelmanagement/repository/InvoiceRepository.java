package com.nguyenhuuquang.hotelmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nguyenhuuquang.hotelmanagement.entity.Invoice;
import com.nguyenhuuquang.hotelmanagement.entity.enums.InvoiceStatus;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByInvoiceCode(String invoiceCode);

    Optional<Invoice> findByBookingId(Long bookingId);

    List<Invoice> findByStatus(InvoiceStatus status);
}