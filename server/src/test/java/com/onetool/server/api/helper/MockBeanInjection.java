package com.onetool.server.api.helper;

import com.onetool.server.api.member.business.MemberBusiness;
import com.onetool.server.api.member.service.MemberService;
import com.onetool.server.api.order.business.OrderBusiness;
import com.onetool.server.api.qna.business.QnaBoardBusiness;
import com.onetool.server.global.auth.jwt.JwtUtil;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

@MockBean(JpaMetamodelMappingContext.class)
public class MockBeanInjection {
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    protected MemberService memberService;
    @MockBean
    protected MemberBusiness memberBusiness;
    @MockBean
    protected QnaBoardBusiness qnaBoardBusiness;
    @MockBean
    protected OrderBusiness orderBusiness;
}
