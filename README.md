<div align="center">

<img width="200" height="200" alt="image" src="https://github.com/user-attachments/assets/c369d019-4feb-48a2-a7fa-2131d943d774" />
  <h1>🏗️ 나만의 건축사수, 3D 모델 플랫폼 원툴</h1>
  <p>
    자신만의 3D CAD 도면을 만나보세요!
  </p>
<h4>
    <a href="https://onetoolcad.vercel.app">Web Page</a>
  <span> · </span>
    <a href="https://garrulous-bearskin-817.notion.site/ONETOOL-e7a9e586415142ab9a2f49d3b4f0146d?source=copy_link">Documentation</a>
  <span> · </span>
    <a href="https://github.com/likelion-onetool/backend/issues/">Report Bug</a>
  <span> · </span>
    <a href="https://github.com/likelion-onetool/backend/issues/">Request Feature</a>
  </h4>
</div>

# 🧭 Table of contents
* [✨ About Project](#-about-project)
    * [📌 Purpose](#-purpose)
    * [🔖 Summary](#-summary)
* [🛠️ Tech Stacks](#%EF%B8%8F-tech-stacks)
    * [🤔 Why](#-why)
* [🖼️ Architecture & Pattern](#%EF%B8%8F-architecture--pattern)
    * [🗺️ Infrastructure](#%EF%B8%8F-infrastructure)
    * [🧩 Layered Pattern](#-layered-pattern)
* [🗂️ ERD & User Scenario](#%EF%B8%8F-erd--user-scenario)
    * [🌫️ ERD](#%EF%B8%8F-erd)
    * [🎬 User Scenario](#-user-scenario)
* [☑️ Trouble Shooting](#%EF%B8%8F-trouble-shooting)
* [🔥 Performance Enhancement](#-performance-enhancement)
* [👥 Contributors](#-contributors)


<br/>

## ✨ About Project
<img width="800" height="800" alt="image" src="https://github.com/user-attachments/assets/2c54c286-7417-4123-add6-fffb97cbd1f0" />

### 📌 Purpose

저희 팀 원툴은 3D 도면 모델을 주로 사용하는 건축업계 종사자들을 위한 도면 서비스를 제공합니다.

1. **필요한 도면만 빠르게**
2. **도면 구매를 보다 간편하게**
3. **내가 만든 도면 판매를 안전하게**

현재 건축 업계에선 아래와 같은 문제점이 있어요.

- 건축 업계에서의 도면 유통 한계
- 교육용 도면 배포 환경 구축

팀 원툴은 건축 업계의 도면 사용을 활성화하기 위해 노력하고 있습니다.

<!-- 기대 효과, 동기 -->

<br/>

### 🔖 Summary

- 프로젝트 이름: 원툴 Onetool
- 개발 기간: 2024.03 ~ 2024.12
- 성과

| 이름 | 성과 | 일시 |
| --- | --- | --- |
| 세종대캠퍼스타운 창업아이디아톤 | **대상** | 2024.07.31 |
| 세종대학교 IT컨퍼런스 | 우수상 (5/15팀) | 2024.09.26 |
| 세종대 캠퍼스타운 창업세미나 모의 IR | 우수상 (2등/50팀) | 2024.09.11 |
| 세종대 피칭&멘토링대회 | 우수상 (3등/6명) | 2024.09.11 |
| 세종대 창업 컨설팅 | 수료 | 2024.08.14 |

<!-- ## (실제 기능 움짤) -->

<br/>

## 🛠️ Tech Stacks

**[Language & Frameworks]**

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white"> <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> <img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=JUnit5&logoColor=white"> 

**[DB]**

<img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white"> <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white">  

**[DevOps]**

<img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white"> <img src="https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=fff&logoColor=white"> <img src="https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=Amazon%20EC2&logoColor=white"> <img src="https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=Prometheus&logoColor=white"> <img src="https://img.shields.io/badge/grafana-%23F46800.svg?style=for-the-badge&logo=grafana&logoColor=white"> <img src="https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white">

### 🤔 Why?

| **요구 사항** | **선택지** | **기술 선택 이유** |
| ----- | ------ | ----------- |
| **💽Database** | 1. MySQL <br/> 2. PostgreSQL <br/> 3. MariaDB <br/> 4. MongoDB <br/> 5. Redis | - 도면 목록 및 검색 최적화를 위해 인덱스, 캐시, 버퍼 풀 등의 성능 최적화 기능을 제공하는 MySQL 사용 <br/> - 결제 및 구매 내역와 같은 중요한 데이터를 위한 InnoDB의 ACID 트랜잭션 사용 <br/> - Redis의 인메모리 데이터 저장 방식으로 토큰 조회 및 삽입 성능 개선 <br/> - 인증 코드의 자동 삭제를 위한 Redis의 TTL 기능 사용 |
| **🖥️Monitoring** | 1. Jmeter <br/> 2. Prometheus <br/> 3. AWS CloudWatch | - 설치 용이성과 비용적 장점을 가진 Prometheus 사용 <br/> - Expert 설치를 통한 풀링 방식으로 메트릭 수집이 용이 |
| **🛠️CI/CD** | 1. Github Actions <br/> 2. Jenkins | - 별도의 서버 구축이 필요없는 Github Actions 사용 <br/> - YAML 기반 간편하고 쉬운 스크립트 작성 |

**[👉 기술적 의사 결정 문서 보러가기 👈](https://acoustic-rest-b1b.notion.site/20964b4a4ab480abb4c7c2b6b1f2d75c?source=copy_link)**

<!-- DB, 부하테스트, 모니터링, CICD, 검색기능개선(동적쿼리, 인덱스 등등) -->

<br/>

## 🖼️ Architecture & Pattern

### 🗺️ Infrastructure

<img width="800" height="800" alt="image" src="https://github.com/user-attachments/assets/d7695b56-334a-4164-8452-50f9217d41fc" />

- **Q. 개발 서버와 운영 서버를 나눈 이유가 무엇인가요?**
  - 저희는 판매 기능을 제공합니다.
  - 이런 기능은 실제 배포된 환경에서 정상적으로 작동하는지 확인이 필요합니다.
- **Q. ALB와 Nginx를 두 개 모두 사용하나요?**
  - 공식 서비스에선 품질 보증이 가능한 AWS 서비스를 사용했습니다.
  - 하지만, 비용 문제가 발생했고 품질이 중요하지 않은 개발 서버까지 ALB를 도입할 필요성이 없었습니다.
  - 따라서, 비용이 발생하지 않는 Nginx를 개발 서버에 도입했습니다.

### 🧩 Layered Pattern

![image](https://github.com/user-attachments/assets/1ffa3146-0f79-43b5-9278-6b8e69b80cec)

- Q. **왜 `Service`와 `Business` 레이어가 나뉘어 있나요?**
  - 각 도메인 Service 내에서 중복되는 코드(DB 접근 및 DTO 매핑)가 많다는 것을 파악하였습니다.
  - 따라서, 핵심 비즈니스 로직은 Business에, 비즈니스 로직으로 보기 애매한 중복 로직은 Service에 담았습니다.
- Q. **`Repository 인터페이스`는 왜 사용되나요?**
  - **저희는 라이브러리에 종속되는 것을 지양합니다.** JpaRepository는 RDB에 국한되기 때문에 추후 NoSQL로의 마이그레이션에 폐쇄적입니다.
  - 따라서, Repository 인터페이스에 역할을 정하고, 이를 상속받는 JpaRepository를 사용 중입니다.

<br/>

## 🗂️ ERD & User Scenario

### 🌫️ ERD
<img src="https://github.com/user-attachments/assets/1436a9f7-42ad-4601-8e1d-9bf97551fbdc" height=700 width=700 />

### 🎬 User Scenario

[👉 Notion: 유저 시나리오 보러가기 👈](https://garrulous-bearskin-817.notion.site/66ed82a478514cd5ae13836b30e2909e?pvs=4)

<br/>

## ☑️ Trouble Shooting
<!-- 도면 구매, 도면 검색, 도면 업로드 -->
|이름                                               |     태그                   | URL                                                      |
|--------------------------------------------------|---------------------------|----------------------------------------------------------|
| **RestAssured가 포트를 찾지 못하는 문제**                 | Spring, Test, RestAssured | [URL](https://acoustic-rest-b1b.notion.site/java-net-ConnectException-Connection-refused-connect-9ca094793a6a4372bec14cd532e9fa00?source=copy_link) |
| **커넥션이 Read-Only인 문제**                            | Spring, JPA               | [URL](https://acoustic-rest-b1b.notion.site/Connection-is-read-only-Queries-leading-to-data-modification-are-not-allowed-51f0afd37d3f4aa99a08ceb57eafaed9?source=copy_link) |
| **JWT의 Claim이 Long인데 Double로 인식되는 버그**          | JWT                     | [URL](https://acoustic-rest-b1b.notion.site/Cannot-convert-existing-claim-value-of-type-class-java-lang-Double-to-desired-type-class-java-lan-65b44f34c03a4430a0cc68a9f192beb9?source=copy_link) |
| **MySQL 루트 비밀번호 분실 시, 초기화**                     | AWS, MySQL                | [URL](https://acoustic-rest-b1b.notion.site/MySQL-29684d1ecdab421594a61d647376c923?source=copy_link) | 
| **permitAll() 적용해도 Filter 통과되는 문제**              | Spring                   | [URL](https://acoustic-rest-b1b.notion.site/permitAll-Filter-7dd00a396d3847998ce9b491b00a277b?source=copy_link) |
| **리액트에서만 CORS 발생 문제**                            | Axios                     | [URL](https://acoustic-rest-b1b.notion.site/CORS-1ad80f3d3cc347a2b41796593ccb0839?source=copy_link) |
| **서브 모듈 카피가 작동하지 않는 문제**                       | Git                      | [URL](https://acoustic-rest-b1b.notion.site/ff3f2475835c450ba029fda7835c5faf?source=copy_link) |
| **Spring security Auhentication이 저장되지 않는 버그**     | Spring Security          | [URL](https://acoustic-rest-b1b.notion.site/Spring-security-Auhentication-5a082722f14a49ffa5a7849e06d17323?source=copy_link) |
| **Redis 저장 시, 공백이 앞쪽에 붙는 버그**                   | Redis                    | [URL](https://acoustic-rest-b1b.notion.site/Redis-ca1708d9ff1240a1a676b9fb9fdf67ed?source=copy_link) |
| **도커 컴포즈에서 이미지를 pull하지 못하는 문제**              | Docker                  | [URL](https://acoustic-rest-b1b.notion.site/pull-14a64b4a4ab48076adfad12afbe655db?source=copy_link) |
| **RequestMatcher가 작동하지 않는 문제**                   | Spring Security           | [URL](https://acoustic-rest-b1b.notion.site/Can-t-configure-requestMatchers-after-anyRequest-14b64b4a4ab48029a849f3d290153a95?source=copy_link) |
| **Controller Mock 테스트 시, 빈 주입이 안되는 문제**        | Spring, Test, Mockito     | [URL](https://acoustic-rest-b1b.notion.site/Controller-Mock-1b264b4a4ab480358b89e02ad240f174?source=copy_link) |
| **Jwt 토큰이 제대로 작동하지 않는 문제**                     | JWT                     | [URL](https://acoustic-rest-b1b.notion.site/Jwt-1b364b4a4ab4807fbaf6dc105438ec11?source=copy_link) |
| **@CreationTimestamp로 생성된 일자가 DTO에 담기지 않는 문제** | Spring, JPA              | [URL](https://acoustic-rest-b1b.notion.site/CreationTimestamp-DTO-20164b4a4ab480378776eebc56481726?source=copy_link) |
| **도커 이미지에 업데이트가 반영되지 않는 문제**                 | 🐋Docker                  | [URL](https://acoustic-rest-b1b.notion.site/20264b4a4ab4802fb1c5ead8253931e3?source=copy_link) |
| **WebSocket 시, 경로를 MVC가 인식하지 못하는 문제**          | Spring, WebSocket         | [URL](https://acoustic-rest-b1b.notion.site/WebSocket-MVC-22464b4a4ab480a6924af4856f879d9e?source=copy_link) |

**[👉 트러블 슈팅 목록 보러가기 👈](https://acoustic-rest-b1b.notion.site/5942ba02109f4cb29281de29b12d9775?v=25e8e1693b8a4bef85f4d83d91342768&source=copy_link)**

<br/>

## 🔥 Performance Enhancement
1. **커버링 인덱스로 검색 쿼리 속도 향상** ([🔗PR#182](https://github.com/likelion-onetool/backend/pull/182))
    - `커버링 인덱스`를 이용해 검색 기능의 수행 시간이 637ms → 473ms로 약 `34%` 향상
    - Count 쿼리의 인덱스 사용 시, 미비한 차이로 인한 불필요한 인덱스 사용 방지

2. **WebSocket 기반 다대다 채팅 시스템 구현** ([🔗PR#234](https://github.com/likelion-onetool/backend/pull/234))
    - 부하테스트를 통한 읽기, 쓰기 처리량 기반 데이터베이스 선정(MySQL, MongoDB, PostgreSQL 비교)
    - 내부 메시지 큐 시스템의 동시 쓰기로 인한 에러를, `@Async`를 통해 해결하여 TPS가 6.7/s → 11.9/s로 약 `77%` 향상

3. **대용량 데이터의 키워드 기반 검색 최적화** ([🔗아티클](https://parvegoongame.tistory.com/197))
    - MySQL의 `Full-Text Index`를 도입하여 문장 토큰화 기반 인덱싱 도입
    - 100만 건 데이터 기준 검색 수행 시간을 16,981ms → 0.224ms 약 `99.9%` 성능

4. **Fake 기법을 통해 테스트 속도 개선** ([🔗PR#209](https://github.com/likelion-onetool/backend/pull/209))
    - 빈 컨텍스트 사용 대신 `Fake`를 이용하여 컨텍스트 멤버 생성 기능의 테스트 속도를 990ms에서 460ms로 `46%` 향상
    - 테스트의 대부분을 차지하는 단위 테스트의 수행 속도를 평균 `36%` 개선

5. **N+1 문제 해결로 실행 쿼리 감소** ([🔗PR#115](https://github.com/likelion-onetool/backend/pull/115))
     - 다대일 관계의 적은 중복 문제 해결을 위해 `inner join`을 사용하여 키워드 검색 쿼리를 6개 → 4개 감소

<br/>

## 👥 Contributors

| Front | Back | Back | Back | Back |
|:-----:|:----:|:----:|:----:|:----:|
| [<img src="https://avatars.githubusercontent.com/u/105264785?v=4" height=100 width=100>](https://github.com/tnqkr3494) | [<img src="https://avatars.githubusercontent.com/u/123933574?v=4" height=100 width=100>](https://github.com/LEEDONGH00N) | [<img src="https://avatars.githubusercontent.com/u/63222221?v=4" height=100 width=100> ](https://github.com/mete0rfish) | [<img src="https://avatars.githubusercontent.com/u/92675692?v=4" height=100 width=100> ](https://github.com/day024) | [<img src="https://avatars.githubusercontent.com/u/164465431?v=4" height=100 width=100> ](https://github.com/PlusUltraCode) |
| 강인권 | 이동훈 | 윤성원 | 정다영 | 이동호 |

