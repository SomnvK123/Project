package com.example.project.controller;


import com.example.project.dto.PackagesDto;
import com.example.project.service.PackageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}