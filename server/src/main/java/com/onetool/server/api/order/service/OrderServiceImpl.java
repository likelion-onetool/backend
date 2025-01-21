package com.onetool.server.api.order.service;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.repository.BlueprintRepository;
import com.onetool.server.global.auth.MemberAuthContext;
import com.onetool.server.global.exception.BaseException;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.repository.MemberRepository;
import com.onetool.server.api.order.dto.request.OrderItem;
import com.onetool.server.api.order.dto.request.OrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.onetool.server.global.exception.codes.ErrorCode.*;
import static com.onetool.server.api.order.dto.response.OrderResponse.OrderPageMemberResponseDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository;
    private final BlueprintRepository blueprintRepository;

    public OrderPageMemberResponseDto getMemberInfoForOrder(MemberAuthContext user, OrderRequest request){

        Member member = findMember(user.getEmail());
        List<Blueprint> blueprints = new ArrayList<>();
        for(OrderItem blueprintId : request.orderList()){
            blueprints.add(getDiabetes(blueprintId.foodId()));
        }
        return OrderPageMemberResponseDto
                .orderPage(request.totalPrice(), member, blueprints);
    }


    private Member findMember(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(NON_EXIST_USER));
    }

    public Blueprint getDiabetes(Long foodId) {
        return blueprintRepository
                .findById(foodId)
                .orElseThrow(() -> new BaseException(NO_BLUEPRINT_FOUND));
    }
}
