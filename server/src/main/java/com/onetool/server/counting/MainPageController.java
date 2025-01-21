package com.onetool.server.counting;

import com.onetool.server.counting.dto.ServiceCountingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainPageController {

    private final ServiceCountingService serviceCountingService;

    @GetMapping("/status")
    public ResponseEntity getServiceStatus() {
        ServiceCountingResponse response = serviceCountingService.getServiceStatus();
        return ResponseEntity.ok().body(response);
    }
}
