package com.urbanflow.event.presentation.controller;

import com.urbanflow.common.constant.ApiConstants;
import com.urbanflow.common.constant.HttpHeaderConstants;
import com.urbanflow.common.dto.ApiResponse;
import com.urbanflow.common.dto.PageMeta;
import com.urbanflow.event.application.service.UrbanEventApplicationService;
import com.urbanflow.event.presentation.dto.ScheduleUrbanEventRequest;
import com.urbanflow.event.presentation.dto.UpdateUrbanEventRequest;
import com.urbanflow.event.presentation.dto.UrbanEventResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/events")
@Tag(name = "Urban Events", description = "Planned urban events such as concerts and markets")
public class UrbanEventController {

    private final UrbanEventApplicationService urbanEventApplicationService;

    public UrbanEventController(UrbanEventApplicationService urbanEventApplicationService) {
        this.urbanEventApplicationService = urbanEventApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('OPERATOR', 'ADMIN')")
    @Operation(summary = "Schedule a new urban event")
    public ApiResponse<UrbanEventResponse> schedule(
            @Valid @RequestBody ScheduleUrbanEventRequest request,
            HttpServletRequest httpRequest
    ) {
        return ApiResponse.ok(
                UrbanEventResponse.from(urbanEventApplicationService.scheduleEvent(
                        request.type(),
                        request.title(),
                        request.description(),
                        request.zoneId(),
                        request.latitude(),
                        request.longitude(),
                        request.startsAt(),
                        request.endsAt(),
                        request.expectedAttendance(),
                        request.organizer(),
                        httpRequest.getHeader(HttpHeaderConstants.CORRELATION_ID)
                )),
                "Urban event scheduled"
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get urban event by id")
    public ApiResponse<UrbanEventResponse> getById(@PathVariable UUID id) {
        return ApiResponse.ok(UrbanEventResponse.from(urbanEventApplicationService.getEvent(id)));
    }

    @GetMapping
    @Operation(summary = "List urban events with pagination")
    public ApiResponse<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        UrbanEventApplicationService.PagedEvents result = urbanEventApplicationService.getAllEvents(page, size);
        PageMeta meta = result.page();
        return ApiResponse.ok(Map.of(
                "content", result.content().stream().map(UrbanEventResponse::from).toList(),
                "page", meta
        ));
    }

    @GetMapping("/zone/{zoneId}")
    @Operation(summary = "List urban events by zone")
    public ApiResponse<List<UrbanEventResponse>> getByZone(@PathVariable String zoneId) {
        List<UrbanEventResponse> events = urbanEventApplicationService.getEventsByZone(zoneId).stream()
                .map(UrbanEventResponse::from)
                .toList();
        return ApiResponse.ok(events);
    }

    @GetMapping("/zone/{zoneId}/upcoming")
    @Operation(summary = "List upcoming urban events for a zone")
    public ApiResponse<List<UrbanEventResponse>> getUpcoming(
            @PathVariable String zoneId,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<UrbanEventResponse> events = urbanEventApplicationService.getUpcomingEvents(zoneId, limit).stream()
                .map(UrbanEventResponse::from)
                .toList();
        return ApiResponse.ok(events);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'ADMIN')")
    @Operation(summary = "Update a scheduled urban event")
    public ApiResponse<UrbanEventResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUrbanEventRequest request,
            HttpServletRequest httpRequest
    ) {
        return ApiResponse.ok(
                UrbanEventResponse.from(urbanEventApplicationService.updateEvent(
                        id,
                        request.title(),
                        request.description(),
                        request.startsAt(),
                        request.endsAt(),
                        request.expectedAttendance(),
                        httpRequest.getHeader(HttpHeaderConstants.CORRELATION_ID)
                )),
                "Urban event updated"
        );
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('OPERATOR', 'ADMIN')")
    @Operation(summary = "Cancel a scheduled urban event")
    public ApiResponse<UrbanEventResponse> cancel(
            @PathVariable UUID id,
            HttpServletRequest httpRequest
    ) {
        return ApiResponse.ok(
                UrbanEventResponse.from(urbanEventApplicationService.cancelEvent(
                        id,
                        httpRequest.getHeader(HttpHeaderConstants.CORRELATION_ID)
                )),
                "Urban event cancelled"
        );
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('OPERATOR', 'ADMIN')")
    @Operation(summary = "Mark an urban event as completed")
    public ApiResponse<UrbanEventResponse> complete(
            @PathVariable UUID id,
            HttpServletRequest httpRequest
    ) {
        return ApiResponse.ok(
                UrbanEventResponse.from(urbanEventApplicationService.completeEvent(
                        id,
                        httpRequest.getHeader(HttpHeaderConstants.CORRELATION_ID)
                )),
                "Urban event completed"
        );
    }
}
