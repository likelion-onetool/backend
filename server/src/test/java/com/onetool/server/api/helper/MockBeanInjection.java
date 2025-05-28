package com.onetool.server.api.helper;

import com.onetool.server.api.blueprint.business.BlueprintBusiness;
import com.onetool.server.api.blueprint.business.BlueprintInspectionBusiness;
import com.onetool.server.api.blueprint.business.BlueprintSearchBusiness;
import com.onetool.server.api.blueprint.service.BlueprintSearchService;
import com.onetool.server.api.blueprint.service.BlueprintService;
import com.onetool.server.api.member.business.MemberBusiness;
import com.onetool.server.api.member.business.MemberLoginBusiness;
import com.onetool.server.api.member.service.MemberService;
import com.onetool.server.api.qna.business.QnaBoardBusiness;
import com.onetool.server.global.auth.jwt.JwtUtil;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

@MockBean(JpaMetamodelMappingContext.class)
public class MockBeanInjection {
    @MockBean
    protected JwtUtil jwtUtil;
    @MockBean
    protected MemberService memberService;
    @MockBean
    protected MemberBusiness memberBusiness;
    @MockBean
    protected QnaBoardBusiness qnaBoardBusiness;
    @MockBean
    protected BlueprintSearchService blueprintSearchService;
    @MockBean
    protected BlueprintBusiness blueprintBusiness;
    @MockBean
    protected BlueprintSearchBusiness blueprintSearchBusiness;
    @MockBean
    protected BlueprintInspectionBusiness blueprintInspectionBusiness;
    @MockBean
    protected MemberLoginBusiness memberLoginBusiness;
    @MockBean
    protected BlueprintService blueprintService;
}
