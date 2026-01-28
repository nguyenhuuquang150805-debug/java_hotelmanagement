package com.nguyenhuuquang.hotelmanagement.service;

import java.util.List;
import java.util.Optional;

import com.nguyenhuuquang.hotelmanagement.entity.Invoice;
import com.nguyenhuuquang.hotelmanagement.entity.enums.InvoiceStatus;

public interface InvoiceService {
    Invoice createInvoice(Invoice invoice);

    Invoice updateInvoice(Long id, Invoice invoice);

    void deleteInvoice(Long id);

    Optional<Invoice> getInvoiceById(Long id);

    Optional<Invoice> getInvoiceByCode(String invoiceCode);

    Optional<Invoice> getInvoiceByBookingId(Long bookingId);

    List<Invoice> getAllInvoices();

    List<Invoice> getInvoicesByStatus(InvoiceStatus status);

    Invoice updateInvoiceStatus(Long id, InvoiceStatus status);
}