# 대학생을 위한 빈티지 & 기부 플랫폼 WEAR

![웨어_compressed-01](https://github.com/mmije0ng/RaspberryProject/assets/127730905/8b7636f3-28d0-4013-88b5-2454eca164cd)

&nbsp;
## **프로젝트 개요**
| ![웨어_compressed-02](https://github.com/mmije0ng/RaspberryProject/assets/127730905/434b5882-eef3-49a3-a62c-b10b591f541a) |  ![웨어_compressed-03](https://github.com/mmije0ng/RaspberryProject/assets/127730905/0fa928ce-f9bc-4f9a-8de4-763d99b972ae) |
|---|---|

![웨어_compressed-10](https://github.com/mmije0ng/RaspberryProject/assets/127730905/93942cc0-9279-4d41-8c03-7d7f113775b8)

**WEAR**는 친환경적인 의류 소비와 기부를 돕는 서비스로, 재학생 인증을 통해 같은 대학교 재학생들끼리 중고 의류 거래가 가능하며 원하는 기부 단체를 선택해 편리하게 기부를 신청할 수 있습니다.

구름톤 유니브 2기에서 백엔드로 참여한 프로젝트로, 해커톤 이후 리팩토링과 추가 기능 구현을 진행했습니다.

&nbsp;
## 주요 기능

![웨어_compressed-05](https://github.com/mmije0ng/RaspberryProject/assets/127730905/058af71d-81e3-42a7-8a50-2ca24c07bf28)
![웨어_compressed-06](https://github.com/mmije0ng/RaspberryProject/assets/127730905/79ae7e95-a67a-4d4e-a184-c24cc51d7359)
![웨어_compressed-07](https://github.com/mmije0ng/RaspberryProject/assets/127730905/564b5bc1-9ec0-41ac-b289-7f2f60980523)
![웨어_compressed-08](https://github.com/mmije0ng/RaspberryProject/assets/127730905/e75fc22a-5735-41a6-8182-50eb69cb3270)
![웨어_compressed-09](https://github.com/mmije0ng/RaspberryProject/assets/127730905/e576b9c2-b36d-4512-98bb-d4dd9d6cf509)

### **1. 학교 이메일 기반 재학생 인증**을 통한 회원가입 및 로그인

- 회원 가입 시 학교 이메일을 기반으로 메일을 보내 재학생 인증
- 사용자 로그인 시 **같은 대학 학생들**과만 중고 거래 가능
- 사용자의 인증 및 권한 부여를 위해 **JWT** 방식 사용
- 로그인 시 발급된 **Refresh Token**은 **Redis**에 저장하여 **토큰 관리 및 인증**을 효율적으로 처리하고, 보안성을 높임

### **2. 환경 점수 기반 TOP5 대학 순위 제공**

- 중고 거래, 기부, 환경 퀴즈 참여를 통해 산출된 환경 점수를 활용하여 **매월 1일 12시에 자동으로 스케줄링 되어 TOP5 대학 순위 공개**

### **3. 기부 신청**

- **기부 방식과 원하는 기부 단체를 선택**하여 기부를 간편하게 신청 가능
- 사용자의 위치를 기반으로 가까운 기부 단체를 지도를 통해 보여줌

### **4. 중고 거래**

- **카테고리** 및 **최신순**으로 정렬된 게시물을 페이징 처리하여 확인
- 사용자들이 중고 거래 물품을 업로드하고 관리
- 같은 대학교 재학생들끼리만 거래 가능

### **5. 실시간 채팅 기능**

- 원하는 같은 대학 사용자와 채팅을 통해 중고 거래 진행
- WebSocket, STOMP 프로토콜, SockJS 를 활용한 채팅 기능 제공

### **6. 최근 검색어 목록 & 인기 검색어 순위**

- **최근 검색어 목록**을 통해 최근에 입력한 검색어를 확인
- 매일 정각에 스케줄링된 인기 검색어 순위 확인

### **7. 환경 관련 콘텐츠 제공**

- 퀴즈, 이벤트 등 환경 지식을 쌓을 수 있는 다양한 콘텐츠 제공
- 퀴즈 정답 시 포인트를 획득하여 동기를 부여하고 사용자 레벨 업데이트

### **8. 포인트 기반 레벨 시스템**

- 포인트 점수에 따라 사용자 레벨을 나누어 업데이트하며, 성취감 제공

&nbsp;
## 맡은 역할 (백엔드)

- JWT와 재학생 메일 인증 라이브러리를 이용한 로그인 & 회원가입
- 환경점수 기반 TOP5 대학 순위 스케줄링
- 카테고리별, 최신순으로 중고거래 상품 정렬 기능
- WebSocket, STOMP 프로토콜을 이용한 채팅 기능
- 인기 검색어 순위 스케줄링
- 기부 신청 & 퀴즈 페이지
- 마이페이지
- ERD 설계 & API 명세서 설계 & AWS RDS 연결
    - ERD 링크: https://www.erdcloud.com/d/XkvGbXT7YuNEaJvAs

&nbsp;
## 적용 기술

### 백엔드
![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=SpringBoot&logoColor=white)
![Java](https://img.shields.io/badge/Java-007396?style=flat-square&logo=OpenJDK&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white)
![STOMP](https://img.shields.io/badge/STOMP-000000?style=flat-square&logo=Protocol&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=flat-square&logo=JSONWebTokens&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-232F3E?style=flat-square&logo=AmazonAWS&logoColor=white)

### 프론트엔드
![React](https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=react&logoColor=black)
![Vite](https://img.shields.io/badge/Vite-646CFF?style=flat-square&logo=Vite&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-007ACC?style=flat-square&logo=typescript&logoColor=white) 
![SockJS](https://img.shields.io/badge/SockJS-010101?style=flat-square&logo=SockJS&logoColor=white)
