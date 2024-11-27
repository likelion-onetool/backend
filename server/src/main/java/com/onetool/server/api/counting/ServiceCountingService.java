package com.onetool.server.api.counting;

import com.onetool.server.api.blueprint.repository.BlueprintRepository;
import com.onetool.server.api.counting.dto.ServiceCountingResponse;
import com.onetool.server.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceCountingService {

    private final MemberRepository memberRepository;
    private final BlueprintRepository blueprintRepository;

    public ServiceCountingResponse getServiceStatus() {
        Long memberCount = memberRepository.countAllMember();
        Long blueprintCount = blueprintRepository.countAllBlueprint();

        return ServiceCountingResponse.builder()
                .totalFiles(blueprintCount)
                .totalUser(memberCount)
                .build();
    }
}