package com.example.project.controller;


import com.example.project.dto.PackagesProductDto;
import com.example.project.service.PackageProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/package-products")
public class PackageProductsController {

    @Autowired
    private PackageProductsService packageProductsService;

    @RequestMapping("/list")
    public ResponseEntity<Page<PackagesProductDto>> listPackageProducts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String[] sort
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Page<PackagesProductDto> packageProducts = packageProductsService.listPackageProducts(
                PageRequest.of(page, size, Sort.by(direction, sort[0]))
        );
        return ResponseEntity.ok(packageProducts);
    }
}
