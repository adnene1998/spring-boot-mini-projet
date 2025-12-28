package com.service.banqueservice.controller;
import com.service.banqueservice.entity.Patient;
import com.service.banqueservice.service.IServicePatient;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patient/")
@AllArgsConstructor
public class PatientRestController {

    private IServicePatient iServicePatient;

    @PostMapping("add")
    public Patient add(@RequestBody Patient patient){
        return iServicePatient.addPatient(patient);
    }

    @GetMapping("all")
    public List<Patient> allPatients(){
        return iServicePatient.getAllPatient();

    }
    @GetMapping("{id}")
    public Optional<Patient> getById(@PathVariable int id){
        return iServicePatient.getPatientById(id);
    }
}
