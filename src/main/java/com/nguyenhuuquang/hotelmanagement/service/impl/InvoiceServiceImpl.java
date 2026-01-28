package com.nguyenhuuquang.hotelmanagement.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nguyenhuuquang.hotelmanagement.entity.Invoice;
import com.nguyenhuuquang.hotelmanagement.entity.enums.InvoiceStatus;
import com.nguyenhuuquang.hotelmanagement.repository.InvoiceRepository;
import com.nguyenhuuquang.hotelmanagement.service.InvoiceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;

    @Override
    @Transactional
    public Invoice createInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Invoice updateInvoice(Long id, Invoice invoice) {
        return invoiceRepository.findById(id)
                .map(existing -> {
                    invoice.setId(id);
                    return invoiceRepository.save(invoice);
                })
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }

    @Override
    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public Optional<Invoice> getInvoiceByCode(String invoiceCode) {
        return invoiceRepository.findByInvoiceCode(invoiceCode);
    }

    @Override
    public Optional<Invoice> getInvoiceByBookingId(Long bookingId) {
        return invoiceRepository.findByBookingId(bookingId);
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public List<Invoice> getInvoicesByStatus(InvoiceStatus status) {
        return invoiceRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public Invoice updateInvoiceStatus(Long id, InvoiceStatus status) {
        return invoiceRepository.findById(id)
                .map(invoice -> {
                    invoice.setStatus(status);
                    return invoiceRepository.save(invoice);
                })
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
    }
}