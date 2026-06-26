package com.urbanflow.notification.presentation.controller;

import com.urbanflow.common.constant.ApiConstants;
import com.urbanflow.common.dto.ApiResponse;
import com.urbanflow.common.dto.PageMeta;
import com.urbanflow.notification.application.service.NotificationApplicationService;
import com.urbanflow.notification.presentation.dto.CreateSubscriptionRequest;
import com.urbanflow.notification.presentation.dto.NotificationResponse;
import com.urbanflow.notification.presentation.dto.SubscriptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping(ApiConstants.API_BASE_PATH + "/notifications")
@Tag(name = "Notifications", description = "User notifications and zone subscriptions")
public class NotificationController {

    private final NotificationApplicationService notificationApplicationService;

    public NotificationController(NotificationApplicationService notificationApplicationService) {
        this.notificationApplicationService = notificationApplicationService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification by id")
    public ApiResponse<NotificationResponse> getById(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ApiResponse.ok(NotificationResponse.from(
                notificationApplicationService.getNotification(id, jwt.getSubject())
        ));
    }

    @GetMapping
    @Operation(summary = "List notifications for current user")
    public ApiResponse<Map<String, Object>> list(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        NotificationApplicationService.PagedNotifications result =
                notificationApplicationService.getNotificationsForUser(jwt.getSubject(), page, size);
        PageMeta meta = result.page();
        return ApiResponse.ok(Map.of(
                "content", result.content().stream().map(NotificationResponse::from).toList(),
                "page", meta
        ));
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Mark notification as read")
    public ApiResponse<NotificationResponse> markRead(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ApiResponse.ok(
                NotificationResponse.from(notificationApplicationService.markAsRead(id, jwt.getSubject())),
                "Notification marked as read"
        );
    }

    @PostMapping("/subscriptions")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Subscribe to zone notifications")
    public ApiResponse<SubscriptionResponse> subscribe(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateSubscriptionRequest request
    ) {
        return ApiResponse.ok(
                SubscriptionResponse.from(notificationApplicationService.subscribe(
                        jwt.getSubject(),
                        request.zoneId()
                )),
                "Subscription created"
        );
    }

    @GetMapping("/subscriptions")
    @Operation(summary = "List current user subscriptions")
    public ApiResponse<List<SubscriptionResponse>> listSubscriptions(@AuthenticationPrincipal Jwt jwt) {
        List<SubscriptionResponse> subscriptions = notificationApplicationService
                .getSubscriptions(jwt.getSubject()).stream()
                .map(SubscriptionResponse::from)
                .toList();
        return ApiResponse.ok(subscriptions);
    }

    @DeleteMapping("/subscriptions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Unsubscribe from zone notifications")
    public void unsubscribe(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        notificationApplicationService.unsubscribe(id, jwt.getSubject());
    }
}
