package com.onetool.server.api.counting;

import com.onetool.server.api.counting.dto.ServiceCountingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "메인 페이지", description = "메인 페이지 관련 API")
@RestController
@RequiredArgsConstructor
public class MainPageController {

    private final ServiceCountingService serviceCountingService;

    @Operation(summary = "서비스 현황 조회 API", description = "현재 서비스의 누적 이용자 수, 도면 수 등을 조회합니다.")
    @GetMapping("/status")
    public ResponseEntity getServiceStatus() {
        ServiceCountingResponse response = serviceCountingService.getServiceStatus();
        return ResponseEntity.ok().body(response);
    }
}
