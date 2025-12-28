package com.service.banqueservice.service;

import com.service.banqueservice.entity.Patient;
import com.service.banqueservice.repository.PatientRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class ServicePatient implements IServicePatient {

    private final PatientRepository patientRepository;

    @Override
    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getAllPatient() {
        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> getPatientById(int id) {
        return patientRepository.findById(id);
    }
    
    @Override
    public Patient updatePatient(int id, Patient patientDetails) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        
        if (patientDetails.getNom() != null) {
            patient.setNom(patientDetails.getNom());
        }
        if (patientDetails.getPrenom() != null) {
            patient.setPrenom(patientDetails.getPrenom());
        }
        if (patientDetails.getAge() > 0) {
            patient.setAge(patientDetails.getAge());
        }
        if (patientDetails.getTel() > 0) {
            patient.setTel(patientDetails.getTel());
        }
        if (patientDetails.getEmail() != null) {
            patient.setEmail(patientDetails.getEmail());
        }
        if (patientDetails.getAddress() != null) {
            patient.setAddress(patientDetails.getAddress());
        }
        if (patientDetails.getStatus() != null) {
            patient.setStatus(patientDetails.getStatus());
        }
        
        return patientRepository.save(patient);
    }
    
    @Override
    public void deletePatient(int id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        patientRepository.delete(patient);
    }
}
