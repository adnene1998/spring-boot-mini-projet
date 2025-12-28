package com.service.banqueservice.controller;
import com.service.banqueservice.entity.Patient;
import com.service.banqueservice.service.IServicePatient;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@AllArgsConstructor
public class PatientRestController {

    private IServicePatient iServicePatient;

    @PostMapping
    public ResponseEntity<Patient> add(@Valid @RequestBody Patient patient){
        Patient created = iServicePatient.addPatient(patient);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Patient>> allPatients(){
        List<Patient> patients = iServicePatient.getAllPatient();
        return ResponseEntity.ok(patients);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getById(@PathVariable int id){
        Optional<Patient> patient = iServicePatient.getPatientById(id);
        return patient.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Patient> update(@PathVariable int id, @RequestBody Patient patient){
        try {
            Patient updated = iServicePatient.updatePatient(id, patient);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id){
        try {
            iServicePatient.deletePatient(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
