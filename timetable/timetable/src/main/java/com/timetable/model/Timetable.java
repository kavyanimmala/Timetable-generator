package com.timetable.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "timetable")
@Data
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day", nullable = false)
    private String day;

    @Column(name = "period", nullable = false)
    private int period;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "teacher", nullable = false)
    private String teacher;
}
