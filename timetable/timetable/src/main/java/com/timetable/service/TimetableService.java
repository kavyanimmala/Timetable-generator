package com.timetable.service;

import com.timetable.model.Subject;
import com.timetable.model.Teacher;
import com.timetable.model.Timetable;
import com.timetable.repository.SubjectRepository;
import com.timetable.repository.TeacherRepository;
import com.timetable.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TimetableService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TimetableRepository timetableRepository;

    private static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private static final int PERIODS_PER_DAY = 7;

    public List<Timetable> generateTimetable() {
        List<Subject> subjects = subjectRepository.findAll();
        if (subjects.isEmpty()) {
            throw new RuntimeException("No subjects found. Please add subjects first.");
        }

        // Build a pool of subject slots based on hours_per_week
        List<String> subjectPool = new ArrayList<>();
        for (Subject s : subjects) {
            for (int i = 0; i < s.getHoursPerWeek(); i++) {
                subjectPool.add(s.getSubjectName());
            }
        }

        // Pad or trim pool to exactly 30 slots (5 days x 6 periods)
        int totalSlots = DAYS.length * PERIODS_PER_DAY;
        while (subjectPool.size() < totalSlots) {
            subjectPool.addAll(subjectPool.subList(0, Math.min(subjects.size(), totalSlots - subjectPool.size())));
        }
        if (subjectPool.size() > totalSlots) {
            subjectPool = subjectPool.subList(0, totalSlots);
        }

        Collections.shuffle(subjectPool);

        // Clear old timetable
        timetableRepository.deleteAll();

        List<Timetable> generated = new ArrayList<>();
        int slotIndex = 0;

        for (String day : DAYS) {
            String prevSubject = "";
            for (int period = 1; period <= PERIODS_PER_DAY; period++) {
                String subject = subjectPool.get(slotIndex);

                // Avoid consecutive same subject — swap with next if possible
                if (subject.equals(prevSubject) && slotIndex + 1 < subjectPool.size()) {
                    Collections.swap(subjectPool, slotIndex, slotIndex + 1);
                    subject = subjectPool.get(slotIndex);
                }

                Teacher teacher = teacherRepository.findBySubjectIgnoreCase(subject);
                String teacherName = (teacher != null) ? teacher.getTeacherName() : "TBD";

                Timetable entry = new Timetable();
                entry.setDay(day);
                entry.setPeriod(period);
                entry.setSubject(subject);
                entry.setTeacher(teacherName);

                generated.add(entry);
                prevSubject = subject;
                slotIndex++;
            }
        }

        return timetableRepository.saveAll(generated);
    }

    public List<Timetable> getAllTimetable() {
        return timetableRepository.findAll();
    }
}
