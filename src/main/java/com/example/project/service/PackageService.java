package com.example.project.service;

import com.example.project.config.SecurityUtil;
import com.example.project.dto.PackagesDto;
import com.example.project.dto.ProductInputDto;
import com.example.project.model.PackageProducts;
import com.example.project.model.Packages;
import com.example.project.model.Products;
import com.example.project.model.Users;
import com.example.project.repository.PackageProductsRepository;
import com.example.project.repository.PackagesRepository;
import com.example.project.repository.ProductsRepository;
import com.example.project.repository.UsersRepository;
import com.example.project.userdetail.CustomUserDetails;
import com.example.project.userdetail.UserDetailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PackageService {
    @Autowired
    private PackagesRepository packagesRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private PackageProductsRepository packageProductsRepository;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private UsersRepository usersRepository;

    //Phụ phí vận chuyển 10.000d/kg nếu cân nặng của đơn vượt quá 2kg
    private void calculateExtraFee(Packages pkg) {
        double extraFee = 0.0;
        if (pkg.getWeight() > 2.0) {
            double feePerKg = 10000;
            double weightOver = pkg.getWeight() - 2.0;
            extraFee = weightOver * feePerKg;
            pkg.setExtraFee(extraFee);
        } else {
            pkg.setExtraFee(0.0);
        }
    }

    @Transactional
    public void batchInsertPackages(PackagesDto dto) {
        Integer currentUserId = userDetailService.getCurrentUserId();
        Users user = usersRepository.findById(currentUserId)
            .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        Packages pkg = new Packages();
        pkg.setPickAddress(dto.getPickAddress());
        pkg.setCustomerName(dto.getCustomerName());
        pkg.setCustomerTel(dto.getCustomerTel());
        pkg.setCustomerAddress(dto.getCustomerAddress());
        pkg.setPickMoney(dto.getPickMoney());
        pkg.setValue(dto.getValue());
        pkg.setShipMoney(dto.getShipMoney());
        pkg.setStatus(dto.getStatus());
        pkg.setUsers(user);

        double totalWeight = 0;
        for (ProductInputDto pDto : dto.getProducts()) {
            totalWeight += pDto.getWeight() * pDto.getQuantity();
        }
        totalWeight += 0.2; //200g hộp giấy
        pkg.setWeight(totalWeight);

        calculateExtraFee(pkg);
        packagesRepository.save(pkg);

        List<Products> productsToSave = new ArrayList<>();
        List<PackageProducts> packageProducts = new ArrayList<>();

        for (ProductInputDto pDto : dto.getProducts()) {
            Optional<Products> productOpt = Optional.empty();

            if (pDto.getBarcode() != null) {
                productOpt = productsRepository.findByBarcode(pDto.getBarcode());
            }
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
                product.setUsers(user); // Gán user
            } else {
                // Tạo mới sản phẩm
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
                product.setUsers(user);
            }

            productsToSave.add(product);
        }
        productsRepository.saveAll(productsToSave);

        for (ProductInputDto pDto : dto.getProducts()) {
            Optional<Products> productOpt = productsRepository.findByBarcode(pDto.getBarcode());
            if (productOpt.isPresent()) {
                PackageProducts pp = new PackageProducts();
                pp.setAPackage(pkg);
                pp.setProduct(productOpt.get());
                pp.setQuantity(pDto.getQuantity());
                packageProducts.add(pp);
            }
        }
        packageProductsRepository.saveAll(packageProducts);
    }

    public void updatePackageStatus(int id, int newStatus) {
        // Check if the package exists
        if (!packagesRepository.existsById(id)) {
            throw new IllegalArgumentException("Package with ID " + id + " does not exist.");
        }
        // Update the package status using the repository method
        packagesRepository.updatePackageStatus(id, newStatus);
    }
}