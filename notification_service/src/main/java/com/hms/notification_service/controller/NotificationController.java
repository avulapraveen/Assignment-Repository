package com.hms.notification_service.controller;

import com.hms.notification_service.dto.NotificationResponse;
import com.hms.notification_service.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Service", description = "Manage notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Get notifications for a user with pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated list of notifications",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters")
            })
    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getNotifications(
            @Parameter(description = "User ID")
            @RequestParam Long userId,
            @Parameter(description = "User type: PATIENT or DOCTOR")
            @RequestParam String userType,
            @Parameter(description = "Page number (default 0)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (default 10)")
            @RequestParam(defaultValue = "10") int size) {

        if (!"PATIENT".equalsIgnoreCase(userType) && !"DOCTOR".equalsIgnoreCase(userType)
        && !"ADMIN".equalsIgnoreCase(userType)) {
            return ResponseEntity.badRequest().build();
        }

        Page<NotificationResponse> notifications = notificationService.getNotifications(userId, userType.toUpperCase(), PageRequest.of(page, size));
        return ResponseEntity.ok(notifications);
    }
}