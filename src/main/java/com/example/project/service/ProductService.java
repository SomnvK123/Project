package com.example.project.service;

import com.example.project.dto.ProductsDto;
import com.example.project.model.PackageProducts;
import com.example.project.model.Products;
import com.example.project.model.Users;
import com.example.project.repository.PackagesRepository;
import com.example.project.repository.ProductsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public Page<Products> getAllProducts(Pageable pageable) {
        return productsRepository.findAll(pageable);
    }


    @Transactional
    public void batchInsertProducts(List<ProductsDto> productsDto) {
        List<Products> products = new ArrayList<>();
        for (ProductsDto productDto : productsDto) {
            Products existingProduct = productsRepository.findByName(productDto.getName());
            if (existingProduct != null) {
                continue;
            }
            Products product = getProducts(productDto);
            products.add(product);
        }
        if(!products.isEmpty()) {
            productsRepository.saveAll(products);
        }
    }

    public Products findProducts(String textfind) {
        if (textfind.startsWith("BC")) {
            return productsRepository.findByBarcode(textfind);
        } else {
            return productsRepository.findByName(textfind);
        }
    }

    private Products getProducts(ProductsDto dto) {
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
        if (dto.getUsersId() != null) {
            Users user = new Users();
            user.setId(dto.getUsersId());
            product.setUsers(user);
        }
        return product;
    }


    @Transactional
    public Products updateProducts(int id, ProductsDto productDto) {
        boolean exists = packagesRepository.countPackagesWithProduct(id) > 0;
        if (exists) {
            throw new IllegalStateException(" Không thể cập nhật sản phẩm nếu sản phẩm đang nằm trong đơn có trạng thái 5, 7, 11");
        }
        Products product = productsRepository.findById(id).orElse(null);
        if (product != null) {
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setBarcode(productDto.getBarcode());
            product.setImage(productDto.getImage());
            product.setWeight(productDto.getWeight());
            product.setHeight(productDto.getHeight());
            product.setWidth(productDto.getWidth());
            product.setLength(productDto.getLength());
            product.setDeleted(false);
        }
        return productsRepository.save(product);
    }

    @Transactional
    public void softDeleteProducts(int id) {
       Optional<Products> product = productsRepository.findById(id);
        if (product.isPresent()) {
            Products products = product.get();
            products.setDeleted(true);
            productsRepository.save(products);
        } else {
            throw new IllegalStateException("Sản phẩm không tồn tại");
        }
    }
}