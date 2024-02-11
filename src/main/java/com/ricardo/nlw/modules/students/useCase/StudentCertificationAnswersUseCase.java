package com.ricardo.nlw.modules.students.useCase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ricardo.nlw.modules.questions.entities.QuestionEntity;
import com.ricardo.nlw.modules.questions.repositories.QuestionRepository;
import com.ricardo.nlw.modules.students.dto.StudentCertificationAnswerDTO;
import com.ricardo.nlw.modules.students.entities.AnswersCertificationsEntity;
import com.ricardo.nlw.modules.students.entities.CertificationStudentEntity;
import com.ricardo.nlw.modules.students.entities.StudentEntity;
import com.ricardo.nlw.modules.students.repositories.CertificationStudentRepository;
import com.ricardo.nlw.modules.students.repositories.StudentRepository;

@Service
public class StudentCertificationAnswersUseCase {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CertificationStudentRepository certificationStudentRepository;

    public CertificationStudentEntity execute(StudentCertificationAnswerDTO dto) {

        // Buscar as alternativas das perguntas
        // Correct ou Incorrect
        List<QuestionEntity> questionsEntity = questionRepository.findByTechnology(dto.getTechnology());
        List<AnswersCertificationsEntity> answersCertifications = new ArrayList<>();

        dto.getQuestionsAnswers().stream().forEach(questionAnswer -> {
            var question = questionsEntity.stream()
                    .filter(q -> q.getId().equals(questionAnswer.getQuestiondID())).findFirst().get();

            var findCorrectAlternative = question.getAlternatives().stream()
                    .filter(alternative -> alternative.isCorrect()).findFirst().get();

            if (findCorrectAlternative.getId().equals(questionAnswer.getAlternativeID())) {
                questionAnswer.setCorrect(true);
            } else {
                questionAnswer.setCorrect(false);
            }

            var answerCertificationsEntity = AnswersCertificationsEntity.builder()
                    .answerID(questionAnswer.getAlternativeID())
                    .questionID(questionAnswer.getQuestiondID())
                    .isCorrect(questionAnswer.isCorrect()).build();

            answersCertifications.add(answerCertificationsEntity);
        });

        // Verificar se o estudante existe pelo email
        var student = studentRepository.findByEmail(dto.getEmail());
        UUID studentID;
        if (student.isEmpty()) {
            var studentCreated = StudentEntity.builder().email(dto.getEmail()).build();
            studentCreated = studentRepository.save(studentCreated);
            studentID = studentCreated.getId();
        } else {
            studentID = student.get().getId();
        }

        CertificationStudentEntity certificationStudentEntity = CertificationStudentEntity.builder()
                .technology(dto.getTechnology())
                .studentID(studentID)
                // .answersCertificationsEntities(answersCertifications)

                .build();

        var certificationStudentCreated = certificationStudentRepository.save(certificationStudentEntity);

        answersCertifications.stream().forEach(answerCertification -> {
            answerCertification.setCertificationID(certificationStudentEntity.getId());
            answerCertification.setCertificationStudentEntity(certificationStudentEntity);
        });

        certificationStudentEntity.setAnswersCertificationsEntities(answersCertifications);

        certificationStudentRepository.save(certificationStudentEntity);

        return certificationStudentCreated;
        // Salvar as informações da certificação
    }
}
