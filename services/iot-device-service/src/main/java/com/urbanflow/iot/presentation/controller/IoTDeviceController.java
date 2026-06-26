package com.urbanflow.iot.presentation.controller;

import com.urbanflow.common.constant.ApiConstants;
import com.urbanflow.common.constant.HttpHeaderConstants;
import com.urbanflow.common.dto.ApiResponse;
import com.urbanflow.common.dto.PageMeta;
import com.urbanflow.iot.application.service.IoTDeviceApplicationService;
import com.urbanflow.iot.presentation.dto.DeviceResponse;
import com.urbanflow.iot.presentation.dto.RegisterDeviceRequest;
import com.urbanflow.iot.presentation.dto.TelemetryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping(ApiConstants.API_BASE_PATH + "/devices")
@Tag(name = "IoT Devices", description = "IoT device registry, heartbeat and telemetry")
public class IoTDeviceController {

    private final IoTDeviceApplicationService deviceApplicationService;

    public IoTDeviceController(IoTDeviceApplicationService deviceApplicationService) {
        this.deviceApplicationService = deviceApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new IoT device")
    public ApiResponse<DeviceResponse> register(@Valid @RequestBody RegisterDeviceRequest request) {
        return ApiResponse.ok(
                DeviceResponse.from(deviceApplicationService.registerDevice(
                        request.deviceId(),
                        request.name(),
                        request.sensorType(),
                        request.zoneId(),
                        request.latitude(),
                        request.longitude()
                )),
                "Device registered"
        );
    }

    @GetMapping("/{deviceId}")
    @Operation(summary = "Get device by id")
    public ApiResponse<DeviceResponse> getById(@PathVariable String deviceId) {
        return ApiResponse.ok(DeviceResponse.from(deviceApplicationService.getDevice(deviceId)));
    }

    @GetMapping
    @Operation(summary = "List all devices with pagination")
    public ApiResponse<Map<String, Object>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        IoTDeviceApplicationService.PagedDevices result = deviceApplicationService.getAllDevices(page, size);
        PageMeta meta = result.page();
        return ApiResponse.ok(Map.of(
                "content", result.content().stream().map(DeviceResponse::from).toList(),
                "page", meta
        ));
    }

    @GetMapping("/zone/{zoneId}")
    @Operation(summary = "List devices by zone")
    public ApiResponse<List<DeviceResponse>> getByZone(@PathVariable String zoneId) {
        List<DeviceResponse> devices = deviceApplicationService.getDevicesByZone(zoneId).stream()
                .map(DeviceResponse::from)
                .toList();
        return ApiResponse.ok(devices);
    }

    @PutMapping("/{deviceId}/heartbeat")
    @Operation(summary = "Send device heartbeat")
    public ApiResponse<DeviceResponse> heartbeat(@PathVariable String deviceId) {
        return ApiResponse.ok(
                DeviceResponse.from(deviceApplicationService.heartbeat(deviceId)),
                "Heartbeat recorded"
        );
    }

    @PostMapping("/{deviceId}/telemetry")
    @Operation(summary = "Submit sensor telemetry (publishes to Kafka)")
    public ApiResponse<DeviceResponse> telemetry(
            @PathVariable String deviceId,
            @Valid @RequestBody TelemetryRequest request,
            HttpServletRequest httpRequest
    ) {
        return ApiResponse.ok(
                DeviceResponse.from(deviceApplicationService.submitTelemetry(
                        deviceId,
                        request.value(),
                        request.unit(),
                        httpRequest.getHeader(HttpHeaderConstants.CORRELATION_ID)
                )),
                "Telemetry published"
        );
    }
}
