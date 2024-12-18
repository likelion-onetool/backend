package com.onetool.server.api.member.dto;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.order.dto.response.OrderResponse;
import jakarta.validation.constraints.Past;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record MemberInfoResponse(
        Long id,
        String email,
        String password,
        String name,
        @Past LocalDate birthDate,
        String development_field,
        String phoneNum,
        boolean isNative,
        boolean service_accept,
        LocalDate user_registered_at,
        List<OrderResponse.OrderCompleteResponseDto> orderCompleteResponseDtos
) {
    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getName(),
                member.getBirthDate(),
                member.getField(),
                member.getPhoneNum(),
                member.isNative(),
                member.isServiceAccept(),
                member.getUser_registered_at(),
                member.getOrders().stream().map(OrderResponse.OrderCompleteResponseDto::response).toList()
        );
    }
}