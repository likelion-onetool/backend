package com.onetool.server.api.payments.controller;

import com.onetool.server.api.payments.domain.PaymentProperties;
import com.onetool.server.api.payments.dto.PaymentErrorResponse;
import com.onetool.server.api.payments.domain.TossPaymentMethod;
import com.onetool.server.api.payments.dto.SaveAmountRequest;
import com.onetool.server.api.payments.repository.PaymentRepository;
import com.onetool.server.api.payments.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
@RestController
public class PaymentController {

    private final PaymentProperties properties;
    private final PaymentService paymentService;

    public PaymentController(PaymentProperties properties, PaymentService paymentService) {
        this.properties = properties;
        this.paymentService = paymentService;
    }

    @PostMapping("/saveAmount")
    public ResponseEntity<?> tempSave(HttpSession session, @RequestBody SaveAmountRequest saveAmountRequest) {
        session.setAttribute(saveAmountRequest.orderId(), saveAmountRequest.amount());
        return ResponseEntity.ok("Payment temp save successful");
    }

    @PostMapping("/verifyAmount")
    public ResponseEntity<?> verifyAmount(HttpSession session, @RequestBody SaveAmountRequest saveAmountRequest) {
        String amount = (String) session.getAttribute(saveAmountRequest.orderId());

        if(amount == null || !amount.equals(saveAmountRequest.amount())) {
            return ResponseEntity.badRequest().body(PaymentErrorResponse.builder().code(400).message("결제 금액 정보가 유효하지 않습니다.").build());
        }

        session.removeAttribute(saveAmountRequest.orderId());

        return ResponseEntity.ok("Payment is valid");
    }

    @RequestMapping(value = "/payments/confirm")
    public ResponseEntity<JSONObject> confirmPayment(@RequestBody SaveAmountRequest requestData) throws Exception {
        JSONParser parser = new JSONParser();
        String authorizationHeader = createAuthorizationHeader();
        JSONObject response = sendPaymentConfirmation(requestData, authorizationHeader, parser);
        paymentService.savePayment(response);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/cancel")
    public HttpResponse requestPaymentCancel(HttpServletRequest servletRequest, String paymentKey, String cancelReason) throws IOException, InterruptedException {
        log.info("cancel paymentKey : {}", paymentKey);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("<https://api.toospayments.com/v1/payments/>" + paymentKey + "/cancel"))
                .header("Authorization", createAuthorizationHeader())
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"" + cancelReason + "\"}"))
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String createAuthorizationHeader() {
        // 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
        // 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((properties.getSecretKey() + ":").getBytes(StandardCharsets.UTF_8));
        log.info(Arrays.toString(encodedBytes));
        return "Basic " + new String(encodedBytes);
    }

    private JSONObject sendPaymentConfirmation(SaveAmountRequest requestData, String authorizationHeader, JSONParser parser) throws IOException {
        // 결제를 승인하면 결제수단에서 금액이 차감돼요.
        log.info(authorizationHeader);
        URL url = new URL(properties.getConfirmURL());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizationHeader);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        log.info(requestData.toString());

        try(OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(requestData.toString().getBytes(StandardCharsets.UTF_8));
        }

        int code = connection.getResponseCode();
        try (InputStream responseStream = code == 200 ? connection.getInputStream() : connection.getErrorStream();
            Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
            return (JSONObject) parser.parse(reader);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
