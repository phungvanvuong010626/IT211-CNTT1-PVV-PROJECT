package org.example.it211_pvv_project.service;

import org.example.it211_pvv_project.model.dto.request.GradeRequest;
import org.example.it211_pvv_project.model.dto.request.SubmissionRequest;
import org.example.it211_pvv_project.model.dto.response.SubmissionResponse;

import java.util.List;

public interface SubmissionService {
    SubmissionResponse submitProject(SubmissionRequest request, String username);
    List<SubmissionResponse> getMySubmissions(String username);
    List<SubmissionResponse> getAllSubmissions();
    SubmissionResponse gradeSubmission(GradeRequest request);
}