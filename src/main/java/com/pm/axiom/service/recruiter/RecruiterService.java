package com.pm.axiom.service.recruiter;

import com.pm.axiom.dto.recruiter.RecruiterResponse;
import com.pm.axiom.dto.recruiter.UpdateRecruiterRequest;
import com.pm.axiom.entity.Recruiter;
import com.pm.axiom.entity.Role;
import com.pm.axiom.exception.DuplicateResourceException;
import com.pm.axiom.exception.ResourceNotFoundException;
import com.pm.axiom.mapper.RecruiterMapper;
import com.pm.axiom.repository.RecruiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruiterService {

    private final RecruiterRepository recruiterRepository;
    private final RecruiterMapper recruiterMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public List<RecruiterResponse> getAllRecruiters() {
        return recruiterRepository.findAllByRole(Role.ADMIN_RECRUITER)
                .stream()
                .map(recruiterMapper::toResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.recruiter.id")
    public RecruiterResponse getRecruiterById(Long id) {
        return recruiterMapper.toResponse(getRecruiterOrThrow(id));
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.recruiter.id")
    @Transactional
    public RecruiterResponse updateRecruiter(Long id, UpdateRecruiterRequest request) {
        Recruiter recruiter = getRecruiterOrThrow(id);

        boolean emailChanged = !recruiter.getEmail().equalsIgnoreCase(request.email());
        if (emailChanged && recruiterRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("An account with this email already exists");
        }

        recruiter.setName(request.name());
        recruiter.setEmail(request.email());
        recruiter.setCompanyName(request.companyName());

        return recruiterMapper.toResponse(recruiter);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public RecruiterResponse enableRecruiter(Long id) {
        Recruiter recruiter = getRecruiterOrThrow(id);
        recruiter.setEnabled(true);
        return recruiterMapper.toResponse(recruiter);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public RecruiterResponse disableRecruiter(Long id) {
        Recruiter recruiter = getRecruiterOrThrow(id);
        recruiter.setEnabled(false);
        return recruiterMapper.toResponse(recruiter);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteRecruiter(Long id) {
        recruiterRepository.delete(getRecruiterOrThrow(id));
    }

    private Recruiter getRecruiterOrThrow(Long id) {
        return recruiterRepository.findByIdAndRole(id, Role.ADMIN_RECRUITER)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found with id: " + id));
    }
}
