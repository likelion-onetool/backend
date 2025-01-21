package com.onetool.server.counting;

import com.onetool.server.blueprint.repository.BlueprintRepository;
import com.onetool.server.counting.dto.ServiceCountingResponse;
import com.onetool.server.member.repository.MemberRepository;
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