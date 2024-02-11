package com.ricardo.nlw.modules.students.useCase;

import org.springframework.stereotype.Service;

import com.ricardo.nlw.modules.students.dto.VerifyHasCertificationDTO;

@Service
public class VerifyIfHasCertificationUseCase {

    public boolean execute(VerifyHasCertificationDTO dto) {
        if (dto.getEmail().equals("ricardomenck@rl.com") && dto.getTechnology().equals("JAVA")) {
            return true;
        }
        return false;
    }

}
