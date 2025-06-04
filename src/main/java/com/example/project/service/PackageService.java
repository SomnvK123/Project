package com.example.project.service;

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
import com.example.project.userdetail.UserDetailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        pkg.setStatus(0);
        pkg.setUsers(user);

        double totalWeight = 0;
        for (ProductInputDto pDto : dto.getProducts()) {
            totalWeight += pDto.getWeight() * pDto.getQuantity();
        }
        totalWeight += 0.2; //200g hộp giấy
        pkg.setWeight(totalWeight);

        calculateExtraFee(pkg);
        packagesRepository.save(pkg);
        System.out.println("Saved package ID: " + pkg.getId());


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
                product.setStatus(0); // Mặc định trạng thái là 0 (có thể thay đổi theo yêu cầu)
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

    // Update package status with validation
    // 1
//    public void updatePackageStatus(int id, int newStatus) {
//        // Check if the package exists
//        if (!packagesRepository.existsById(id)) {
//            throw new IllegalArgumentException("Package with ID " + id + " does not exist.");
//        }
//        // Update the package status using the repository method
//        packagesRepository.updatePackageStatus(id, newStatus);
//    }

    // 2
//    public boolean updatePackageStatus(int id, int newStatus) {
//        // Validate allowed newStatus values
//        List<Integer> allowedStatuses = List.of(3, 5, 7, 11, 14, 17, 20, -1);
//        if (!allowedStatuses.contains(newStatus)) {
//            throw new IllegalArgumentException("Invalid newStatus value: " + newStatus);
//        }
//        if (!packagesRepository.existsById(id)) {
//            throw new IllegalArgumentException("Package with ID " + id + " does not exist.");
//        }
//        // Update the package status using the repository method
//        int updated = packagesRepository.updatePackageStatus(id, newStatus);
//        return updated > 0;
//    }

    // 3
    public boolean updatePackageStatus(int id, int newStatus) {
        // Only allow status transitions as defined in the query
        Packages pkg = packagesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Package with ID " + id + " does not exist."));
        int oldStatus = pkg.getStatus();

        boolean isValid =
                (oldStatus == 0 && newStatus == 3) ||
                        (oldStatus == 3 && newStatus == 5) ||
                        (oldStatus == 5 && newStatus == 7) ||
                        (oldStatus == 7 && (newStatus == 11 || newStatus == 14 || newStatus == -1)) ||
                        (oldStatus == 14 && (newStatus == 11 || newStatus == 20)) ||
                        (oldStatus == 11 && newStatus == 17) ||
                        ((oldStatus == 0 || oldStatus == 3 || oldStatus == 5) && newStatus == -1);

        if (!isValid) {
            throw new IllegalArgumentException("Invalid status transition: " + oldStatus + " -> " + newStatus);
        }

        int updated = packagesRepository.updatePackageStatus(id, newStatus);
        return updated > 0;
    }

    // Service Pageable packages limit 20 filter by id or tel
    public Page<Packages> findPackageByIdOrCustomerTel(Pageable pageable, String textfindP) {
        if (textfindP.startsWith("0")) {
            return packagesRepository.findPackageByCustomerTel(pageable, textfindP);
        } else {
            int id = Integer.parseInt(textfindP);
            return packagesRepository.findPackageById(pageable, id);
        }
    }

}