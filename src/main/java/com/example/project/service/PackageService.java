package com.example.project.service;

import com.example.project.dto.PackagesDto;
import com.example.project.dto.ProductInputDto;
import com.example.project.model.PackageProducts;
import com.example.project.model.Packages;
import com.example.project.model.Products;
import com.example.project.repository.PackageProductsRepository;
import com.example.project.repository.PackagesRepository;
import com.example.project.repository.ProductsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class PackageService {
    @Autowired
    private PackagesRepository packagesRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private PackageProductsRepository packageProductsRepository;

    @Transactional
    public void batchInsertPackages(PackagesDto dto) {
        Packages pkg = new Packages();
        pkg.setPickAddress(dto.getPickAddress());
        pkg.setCustomerName(dto.getCustomerName());
        pkg.setCustomerTel(dto.getCustomerTel());
        pkg.setCustomerAddress(dto.getCustomerAddress());
        pkg.setPickMoney(dto.getPickMoney());
        pkg.setValue(dto.getValue());
        pkg.setShipMoney(dto.getShipMoney());
        pkg.setExtraFee(dto.getExtraFee());
        pkg.setWeight(dto.getWeight());
        pkg.setStatus(dto.getStatus());
        packagesRepository.save(pkg);

        for (ProductInputDto pDto : dto.getProducts()) {
            Optional<Products> productOpt = productsRepository.findById(pDto.getProductId());
            Products product;
            if (productOpt.isPresent()) {
                product = productOpt.get();
                product.setName(pDto.getName());
                product.setBarcode(pDto.getBarcode());
                product.setImage(pDto.getImage());
                product.setWeight(pDto.getWeight());
                product.setHeight(pDto.getHeight());
                product.setWidth(pDto.getWidth());
                product.setLength(pDto.getLength());
                product.setPrice(pDto.getPrice());
                product.setStatus(pDto.getStatus());
                productsRepository.save(product);
            } else {
                product = new Products();
                product.setName(pDto.getName());
                product.setBarcode(pDto.getBarcode());
                product.setImage(pDto.getImage());
                product.setWeight(pDto.getWeight());
                product.setHeight(pDto.getHeight());
                product.setWidth(pDto.getWidth());
                product.setLength(pDto.getLength());
                product.setPrice(pDto.getPrice());
                product.setStatus(pDto.getStatus());
                productsRepository.save(product);
            }
        }

        for (ProductInputDto pDto : dto.getProducts()) {
            Optional<Products> productOpt = productsRepository.findByBarcode(pDto.getBarcode());
            if (productOpt.isPresent()) {
                PackageProducts pp = new PackageProducts();
                pp.setAPackage(pkg);
                pp.setProduct(productOpt.get());
                pp.setQuantity(pDto.getQuantity());
                packageProductsRepository.save(pp);
            }
        }
    }
}