package com.example.project.controller;

import com.example.project.dto.ProductsDto;
import com.example.project.model.Products;
import com.example.project.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ProductController {

    @Autowired
    private ProductService productService;

    // Insert multiple products (Admin only)
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/insert")
    @Transactional
    public ResponseEntity<?> insertProducts(@RequestBody List<ProductsDto> productsDto) {
        productService.batchInsertProducts(productsDto);
        return ResponseEntity.ok("Products inserted successfully");
    }

    // Retrieve all products with pagination and sorting
    @GetMapping("/all")
    public ResponseEntity<Page<Products>> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String[] sort) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<Products> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    // Find a product by barcode or name
    @GetMapping("/find/{textfind}")
    public Optional<Products> findProducts(@PathVariable String textfind) {
        return productService.findProducts(textfind);
    }

    // Update product details
    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<?> updateProducts(@RequestBody ProductsDto productDto, @PathVariable Integer id) {
        Products updatedProduct = productService.updateProducts(id, productDto);
        if (updatedProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        return ResponseEntity.ok("Product updated successfully: " + updatedProduct);
    }

    // Soft delete a product
    @PutMapping("/softdelete/{id}")
    @Transactional
    public ResponseEntity<?> softDeleteProducts(@PathVariable Integer id) {
        productService.softDeleteProducts(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}