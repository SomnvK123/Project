package com.example.project.service;


import com.example.project.dto.PackagesProductDto;
import com.example.project.repository.PackageProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageProductsService {

    @Autowired
    private PackageProductsRepository packageProductsRepository;

    public Page<PackagesProductDto> listPackageProducts(Pageable pageable) {
        return packageProductsRepository.listPackageProducts(pageable);
    }
}
