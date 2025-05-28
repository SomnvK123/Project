package com.example.project.controller;


import com.example.project.dto.PackagesDto;
import com.example.project.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

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
}