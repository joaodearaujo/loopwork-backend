package loopwork.backend.occurrence;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/occurrences")
public class OccurrenceController {

    private final OccurrenceService service;

    public OccurrenceController(OccurrenceService service) {
        this.service = service;
    }

//    @GetMapping
//    public ResponseEntity<List<OccurrenceResponse>> list(
//            @RequestParam String professionalId,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
//    ) {
//        List<OccurrenceResponse> occurrences = service.getOccurrencesOfProfessional(professionalId, start, end);
//        return ResponseEntity.ok(occurrences);
//    }

    @GetMapping
    public ResponseEntity<PageResponse<OccurrenceResponse>> listPage(
            @RequestParam String professionalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<OccurrenceResponse> occurrencePage = service.getOccurrencesOfProfessionalByPage(professionalId, start, end, pageable);

        // Record to control api format
        PageResponse<OccurrenceResponse> response = new PageResponse<>(
                occurrencePage.getContent(),
                occurrencePage.getNumber(),
                occurrencePage.getTotalElements(),
                occurrencePage.getTotalPages(),
                occurrencePage.hasNext(),
                occurrencePage.hasPrevious()
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{occurrenceId}/cancel")
    public ResponseEntity<OccurrenceResponse> cancel(@PathVariable String occurrenceId) {
        OccurrenceResponse response = service.cancel(occurrenceId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{occurrenceId}/reschedule")
    public ResponseEntity<OccurrenceResponse> reschedule(
            @Valid @RequestBody RescheduleRequest rescheduleRequest,
            @PathVariable String occurrenceId
    ) {
        OccurrenceResponse response = service.reschedule(rescheduleRequest, occurrenceId);
        return ResponseEntity.ok(response);
    }
}