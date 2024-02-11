package com.ricardo.nlw.modules.students.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ricardo.nlw.modules.students.dto.StudentCertificationAnswerDTO;
import com.ricardo.nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.ricardo.nlw.modules.students.entities.CertificationStudentEntity;
import com.ricardo.nlw.modules.students.useCase.StudentCertificationAnswersUseCase;
import com.ricardo.nlw.modules.students.useCase.VerifyIfHasCertificationUseCase;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

    @Autowired
    private StudentCertificationAnswersUseCase studentCertificationAnswersUseCase;

    @PostMapping("/verifyIfHasCertification")
    public String verifyIfHasCertification(@RequestBody VerifyHasCertificationDTO verifyHasCertificationDTO) {

        // Email
        // Tecnologia
        var result = this.verifyIfHasCertificationUseCase.execute(verifyHasCertificationDTO);
        if (result) {
            return "Usuário já fez a prova!";
        }
        System.out.println(verifyHasCertificationDTO);
        return "Usuário pode fazer a prova!";
    }

    @PostMapping("/certification/answer")
    public CertificationStudentEntity certificationAnswer(
            @RequestBody StudentCertificationAnswerDTO studentCertificationAnswerDTO) {
        return studentCertificationAnswersUseCase.execute(studentCertificationAnswerDTO);
    }
}
