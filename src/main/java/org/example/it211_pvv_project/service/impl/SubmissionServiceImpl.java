package org.example.it211_pvv_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.it211_pvv_project.exception.ConflictException;
import org.example.it211_pvv_project.exception.InvalidStateException;
import org.example.it211_pvv_project.exception.ResourceNotFoundException;
import org.example.it211_pvv_project.model.dto.request.GradeRequest;
import org.example.it211_pvv_project.model.dto.request.SubmissionRequest;
import org.example.it211_pvv_project.model.dto.response.SubmissionResponse;
import org.example.it211_pvv_project.model.entity.Course;
import org.example.it211_pvv_project.model.entity.Submission;
import org.example.it211_pvv_project.model.entity.User;
import org.example.it211_pvv_project.model.enums.SubmissionStatus;
import org.example.it211_pvv_project.repository.CourseRepository;
import org.example.it211_pvv_project.repository.SubmissionRepository;
import org.example.it211_pvv_project.repository.UserRepository;
import org.example.it211_pvv_project.service.SubmissionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

//Xử lý nộp bài và chấm điểm.
@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    // Sinh viên nộp link GitHub
    @Override
    public SubmissionResponse submitProject(SubmissionRequest request, String username) {
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên"));

        Course course = courseRepository.findById(request.getCourseId())
                .filter(Course::getActive)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học"));

        if (submissionRepository.existsByStudentAndCourse(student, course)) {
            throw new ConflictException("Bạn đã nộp bài cho khóa học này");
        }

        Submission submission = Submission.builder()
                .student(student)
                .course(course)
                .githubUrl(request.getGithubUrl())
                .note(request.getNote())
                .status(SubmissionStatus.SUBMITTED)
                .submittedAt(LocalDateTime.now())
                .build();

        return toResponse(submissionRepository.save(submission));
    }

    // Sinh viên xem bài đã nộp
    @Override
    public List<SubmissionResponse> getMySubmissions(String username) {
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên"));

        return submissionRepository.findByStudent(student)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Giảng viên xem tất cả bài nộp
    @Override
    public List<SubmissionResponse> getAllSubmissions() {
        return submissionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Giảng viên chấm điểm
    @Override
    public SubmissionResponse gradeSubmission(GradeRequest request) {
        Submission submission = submissionRepository.findById(request.getSubmissionId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài nộp"));

        if (submission.getStatus() == SubmissionStatus.PENDING) {
            throw new InvalidStateException("Sinh viên chưa nộp bài");
        }

        submission.setScore(request.getScore());
        submission.setFeedback(request.getFeedback());
        submission.setStatus(SubmissionStatus.GRADED);
        submission.setGradedAt(LocalDateTime.now());

        return toResponse(submissionRepository.save(submission));
    }

    private SubmissionResponse toResponse(Submission submission) {
        return SubmissionResponse.builder()
                .id(submission.getId())
                .studentName(submission.getStudent().getFullName())
                .courseName(submission.getCourse().getName())
                .githubUrl(submission.getGithubUrl())
                .reportUrl(submission.getReportUrl())
                .note(submission.getNote())
                .score(submission.getScore())
                .feedback(submission.getFeedback())
                .status(submission.getStatus().name())
                .build();
    }
}