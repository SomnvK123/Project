package com.example.project.service;

import com.example.project.dto.ProductsDto;
import com.example.project.model.Products;
import com.example.project.model.Users;
import com.example.project.repository.PackagesRepository;
import com.example.project.repository.ProductsRepository;
import com.example.project.repository.UsersRepository;
import com.example.project.userdetail.UserDetailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ProductService {
    
    @Autowired
    private ProductsRepository productsRepository;
    
    @Autowired
    private PackagesRepository packagesRepository;
    
    @Autowired
    private UserDetailService userDetailService;
    
    @Autowired
    private UsersRepository usersRepository;

    // Retrieve all products with pagination
    public Page<Products> getAllProducts(Pageable pageable) {
        return productsRepository.findAll(pageable);
    }

    // Batch inserts new products, skipping duplicates by name
    @Transactional
    public void batchInsertProducts(List<ProductsDto> productsDtoList) {
        List<Products> newProducts = new ArrayList<>();
        for (ProductsDto dto : productsDtoList) {
            if (productsRepository.findByName(dto.getName()).isPresent()) {
                continue; // Skip duplicate
            }
            Products product = createProductFromDto(dto);
            newProducts.add(product);
        }
        if (!newProducts.isEmpty()) {
            productsRepository.saveAll(newProducts);
        }
    }

    // Find a product by barcode or name
    public Optional<Products> findProducts(String searchText) {
        if (searchText.startsWith("BC")) {
            return productsRepository.findByBarcode(searchText);
        } else {
            return productsRepository.findByName(searchText);
        }
    }

    // Create a product entity from DTO and current user info
    private Products createProductFromDto(ProductsDto dto) {
        Integer currentUserId = userDetailService.getCurrentUserId();
        Users user = usersRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        Products product = new Products();
        product.setName(dto.getName());
        product.setBarcode(dto.getBarcode());
        product.setImage(dto.getImage());
        product.setWeight(dto.getWeight());
        product.setHeight(dto.getHeight());
        product.setWidth(dto.getWidth());
        product.setLength(dto.getLength());
        product.setPrice(dto.getPrice());
        product.setDeleted(dto.isDeleted());
        product.setStatus(dto.getStatus());
        product.setUsers(user);
        return product;
    }

    // Update product details if not associated with active packages
    @Transactional
    public Products updateProducts(int id, ProductsDto dto) {
        boolean isInActivePackage = packagesRepository.countPackagesWithProduct(id) > 0;
        if (isInActivePackage) {
            throw new IllegalStateException("Cannot update product that belongs to active orders with status 5, 7, or 11");
        }
        Products product = productsRepository.findById(id).orElse(null);
        if (product != null) {
            product.setName(dto.getName());
            product.setPrice(dto.getPrice());
            product.setBarcode(dto.getBarcode());
            product.setImage(dto.getImage());
            product.setWeight(dto.getWeight());
            product.setHeight(dto.getHeight());
            product.setWidth(dto.getWidth());
            product.setLength(dto.getLength());
            product.setDeleted(false);
            return productsRepository.save(product);
        }
        return null;
    }

    // Soft delete a product by marking it as deleted
    @Transactional
    public void softDeleteProducts(int id) {
        Optional<Products> productOpt = productsRepository.findById(id);
        if (productOpt.isPresent()) {
            Products product = productOpt.get();
            product.setDeleted(true);
            productsRepository.save(product);
        } else {
            throw new IllegalStateException("Product does not exist");
        }
    }
}