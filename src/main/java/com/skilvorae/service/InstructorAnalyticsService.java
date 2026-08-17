package com.skilvorae.service;

import com.skilvorae.entity.Course;
import com.skilvorae.repository.CourseRepository;
import com.skilvorae.repository.EnrollmentRepository;
import com.skilvorae.repository.InstructorEarningsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InstructorAnalyticsService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final InstructorEarningsRepository earningsRepository;

    public Map<String, Object> getInstructorAnalytics(Long instructorId) {
        Map<String, Object> analytics = new HashMap<>();
        
        List<Course> courses = courseRepository.findByInstructorId(instructorId);
        
        int totalLearners = 0;
        double totalEarnings = 0.0;
        int activeCourses = courses.size();
        
        for (Course c : courses) {
            totalLearners += c.getEnrollmentCount();
        }
        
        // Mock earnings for now if no real data is there
        var earnings = earningsRepository.findByInstructorId(instructorId);
        for(var e : earnings) {
            totalEarnings += e.getAmount();
        }
        
        analytics.put("totalLearners", totalLearners);
        analytics.put("activeCourses", activeCourses);
        analytics.put("totalEarnings", totalEarnings);
        analytics.put("averageRating", calculateAverageRating(courses));
        
        // Add completion data
        List<Map<String, Object>> courseStats = new java.util.ArrayList<>();
        for (Course c : courses) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("courseName", c.getTitle());
            long enrolled = enrollmentRepository.findByCourseId(c.getId()) != null ? enrollmentRepository.findByCourseId(c.getId()).size() : 0;
            long completed = enrollmentRepository.findByCourseId(c.getId()) != null ? enrollmentRepository.findByCourseId(c.getId()).stream().filter(e -> e.getStatus() == com.skilvorae.enums.EnrollmentStatus.COMPLETED).count() : 0;
            stat.put("enrolled", enrolled);
            stat.put("completed", completed);
            double completionRate = enrolled > 0 ? (double) completed / enrolled * 100 : 0.0;
            stat.put("completionRate", Math.round(completionRate));
            courseStats.add(stat);
        }
        analytics.put("courseStats", courseStats);
        
        return analytics;
    }
    
    private double calculateAverageRating(List<Course> courses) {
        if(courses.isEmpty()) return 0.0;
        double sum = 0;
        for(Course c : courses) {
            sum += c.getRating() != null ? c.getRating() : 0.0;
        }
        return Math.round((sum / courses.size()) * 10.0) / 10.0;
    }
}
