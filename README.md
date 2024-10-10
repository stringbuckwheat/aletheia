# Aletheia 👑🛍️🏅

목차

1. Aletheia?
    * [API 문서](https://documenter.getpostman.com/view/31325959/2sAXqmA4tP)
    * 기술 스택
    * ERD
2. 주요 기능
    * gRPC
    * 금 구매/판매
    * 통계
3. 트러블 슈팅
    * Shell Script
        * 통합 테스트 효율성을 위해 Shell Script 작성
    * 로깅 AOP
4. 구현 상세

# Aletheia 👑🛍️🏅?

금을 판매/구매하는 서비스 Aletheia의 백엔드 서버  
인증 서버, 자원 서버를 분리하여 `gRPC`를 통해 통신합니다.

Postman API 문서는 [이곳](https://documenter.getpostman.com/view/31325959/2sAXqmA4tP)에서 확인하실 수 있습니다.

## 기술 스택

![SpringBoot](https://img.shields.io/badge/Spring_boot(3.3)-%236DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![gRPC](https://img.shields.io/badge/gRPC-244C5A?style=for-the-badge&logo=grpc&logoColor=white)
![Hibernate](https://img.shields.io/badge/JPA/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
![QueryDsl](https://img.shields.io/badge/QueryDsl-000000?style=for-the-badge&logo=QueryDsl&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB(10.3)-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=redis&logoColor=white)

## ERD

![aletheia_erd](https://github.com/user-attachments/assets/bdff6236-1d3d-4711-8167-07859f92b87b)
(간편한 설명을 위한 ERD이며, 실제 `user` 테이블은 Auth 서버와 통신하는 DB에, `purchase`, `sales` 테이블은 Resource 서버와 통신하는 DB에 위치합니다)

# 주요 기능🛠️

## 1) gRPC 기반 MSA 아키텍처

![aletheia_flow](https://github.com/user-attachments/assets/9baaafc4-b63e-4878-bcff-3587c5d83588)

### - 서버 아키텍처 분리

* **Auth 서버**: 사용자 **인증/인가**를 전담하는 독립적인 서버
* **Resource** 서버: **리소스 처리와 데이터 제공** 기능 담당
    * 인증이 필요한 요청이 도착하면 `gRPC`를 사용하여 Auth 서버로 인증 요청 수행

## 2) 금 구매/판매

![스크린샷 2024-10-10 14 08 30](https://github.com/user-attachments/assets/7ecdc673-b97e-4808-b3e5-d894c50edb26)

* 금 구매/판매 시 사용자 ID와 함께 **Human Readable한 주문 번호**를 생성하여 관리를 용이하게 구현
* Spring Validation과 enum, `Custom Annotation`을 사용해 입력 데이터 유효성 검증

```java
    @Schema(description = "수량", example = "3.75")
@NotNull(message = "수량은 필수입니다.")
@DecimalMin(value = "0.01", message = "수량은 최소 0.01g 이상이어야 합니다.")
@Digits(integer = 10, fraction = 2, message = "수량은 최대 소수점 둘째 자리까지 입력 가능합니다.")
private BigDecimal quantity;

@IsValidSalesState
private String salesStatus;
```

## 3) 통계

* 금 판매/구매 데이터를 필터링 하여 각 기간 동안의 거래 통계 제공
    * `시작일`, `종료일`, `구매/판매/전체`, 페이징 등
* QueryDsl 동적 쿼리를 사용하여 유연한 데이터 조회 구현

![스크린샷 2024-10-10 14 24 42](https://github.com/user-attachments/assets/9fadb9f1-0708-42d1-91bd-ccb1c9063f6b)

# 💡 트러블 슈팅

## 1) Shell Script를 통한 서버 빌드 자동화

* 배경, 문제
    * 인증 서버, 자원 서버는 독립적으로 실행되며 gRPC와 RESTful API를 통해 상호작용
    * 개발 과정에서 **빈번히 gRPC 프로토콜 파일 생성, 프로젝트 빌드, 테스트**해야 하는데 이를 수동으로 관리하기는 **비효율적**
* 해결: `자동화 스크립트` 작성
    * `quick_start.sh`와 `stop_servers.sh` 두 개의 Shell Script를 통해 서버 관리, 빌드 자동화
    * `quick_start.sh` 상세
        * (기존) 빌드 디렉토리 삭제
        * gRPC 프로토콜 파일 생성
        * 인증 서버, 자원 서버 빌드
        * 서버 실행
        * 실행 로그는 각각 auth.log, resource.log 파일에 기록

## 2) AOP를 사용하여 예외 로깅 자동화

* 배경
    * 기존 코드: `log.info()` 등의 메서드를 사용해 수동으로 로그 기록
        * 개발자가 직접 로깅을 하지 않으면 예외가 발생해도 로그가 남지 않는 문제
        * 메서드명이나 인수 정보가 일관되지 않아 예외 원인 추적 어려움
        * 각 서비스 클래스마다 별도의 try-catch 블록을 추가하여 예외를 처리하고 로그를 남기는 방식은 코드 중복
* 해결: `AOP`를 도입하여 예외 로깅 자동화
    * `@AfterThrowing` 어노테이션을 사용하여 서비스 메서드에서 발생하는 모든 예외를 캐칭
    * 예외 발생 시 메서드명, 클래스명, 전달된 인수, 예외 메시지를 자동으로 로깅

## 🏃‍♂️‍➡️ Quick Start

### 1. yml 파일 등록

`application.yml` 파일을 `{auth 혹은 resource}/src/main/resource` 디렉토리에 적절히 생성해주세요

### 3. Shell Script를 통한 실행

```shell
cd aletheia

chmod +x quick_start.sh
./quick_start.sh
```

### 4. 서버 종료

```shell
chmod +x stop_servers.sh
./stop_servers.sh
```