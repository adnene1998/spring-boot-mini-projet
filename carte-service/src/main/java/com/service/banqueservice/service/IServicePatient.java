package com.service.banqueservice.service;



import com.service.banqueservice.entity.Patient;

import java.util.List;
import java.util.Optional;

public interface IServicePatient {
    public Patient addPatient(Patient patient);

    public List<Patient> getAllPatient();
    public Optional<Patient> getPatientById(int id);
}
