package com.teknologiinformasi.restapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teknologiinformasi.restapi.model.Produk;
import com.teknologiinformasi.restapi.repository.ProdukRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProdukService {

    @Autowired
    private ProdukRepository produkRepository;

    // Get all products
    public List<Produk> getAllProduk() {
        return produkRepository.findAll();
    }

    // Get a product by ID
    public Optional<Produk> getProdukById(Long id) {
        return produkRepository.findById(id);
    }

    // Create a new product
    public Produk createProduk(Produk produk) {
        return produkRepository.save(produk);
    }

    // Update an existing product
    public Produk updateProduk(Long id, Produk produkDetails) {
        Produk produk = produkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan dengan id " + id));

        produk.setNama(produkDetails.getNama());
        produk.setHarga(produkDetails.getHarga());
        produk.setDeskripsi(produkDetails.getDeskripsi());

        return produkRepository.save(produk);
    }

    // Delete a product by ID
    public void deleteProduk(Long id) {
        Produk produk = produkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan dengan id " + id));

        produkRepository.delete(produk);
    }
}