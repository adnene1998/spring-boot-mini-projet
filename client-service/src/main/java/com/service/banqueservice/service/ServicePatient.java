package com.service.banqueservice.service;

import com.service.banqueservice.entity.Patient;
import com.service.banqueservice.repository.PatientRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
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
}
