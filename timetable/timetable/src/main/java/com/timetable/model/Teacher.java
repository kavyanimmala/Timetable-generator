package com.timetable.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "teachers")
@Data
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "teacher_name", nullable = false)
    private String teacherName;

    @Column(name = "subject", nullable = false)
    private String subject;
}
