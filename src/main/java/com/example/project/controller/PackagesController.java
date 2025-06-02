package com.example.project.controller;


import com.example.project.dto.PackagesDto;
import com.example.project.model.Packages;
import com.example.project.service.PackageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/packages")
public class PackagesController {
    @Autowired
    private PackageService packageService;

    @PostMapping("/insert")
    public ResponseEntity<?> insertPackages(@RequestBody PackagesDto packagesDto) {
        packageService.batchInsertPackages(packagesDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Packages inserted successfully");
    }

    @PutMapping("/update-status/{id}/{newStatus}")
    @Transactional
    public ResponseEntity<String> updatePackageStatus(@PathVariable int id, @PathVariable int newStatus) {
        try {
            packageService.updatePackageStatus(id, newStatus);
            return ResponseEntity.ok("Package status updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while updating the package status.");
        }
    }

    @GetMapping("/findpackages/{textfindP}")
    public ResponseEntity<Page<Packages>> findPackages(
            @PathVariable String textfindP,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "id, asc") String[] sort){
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<Packages> packages = packageService.findPackageByIdOrCustomerTel(pageable, textfindP);
        return ResponseEntity.ok(packages);
    }
}