# Aletheia

## 소개
금을 판매/구매하는 서비스.   
인증 서버, 자원 서버를 분리하여 `gRPC`를 통해 통신합니다.

### 사용 기술
![SpringBoot](https://img.shields.io/badge/Springboot-%236DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![gRPC](https://img.shields.io/badge/gRPC-244C5A?style=for-the-badge&logo=grpc&logoColor=white)
![Hibernate](https://img.shields.io/badge/JPA/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
![QueryDsl](https://img.shields.io/badge/QueryDsl-000000?style=for-the-badge&logo=QueryDsl&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=redis&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)


<details>
<summary>버전 상세 정보</summary>

- ```Java 17```  <br/>
- ```Spring Boot``` : 3.3.3 <br/>
- ```gRPC```: 1.66.0
- ```MySQL``` : 10.3 <br/>

</details> 
<br/>

## Quick Start
### 1. Git Clone
```shell
git clone https://github.com/stringbuckwheat/aletheia.git
```

### 2. yml 파일 등록
제출한 yml 파일을 `{project}/src/main/resource` 디렉토리에 각각 넣어주세요.

### 3. Shell Script를 통한 실행
```shell
cd aletheia

chmod +x quick_start.sh
./quick_start.sh
```

`quick_start.sh` 는 다음의 동작을 실행합니다.
1) 빌드 디렉토리 삭제
2) gRPC 프로토콜 파일 생성
3) 인증 서버, 자원 서버 빌드
4) 백그라운드 서버 실행 

### 4. 서버 종료
```shell
chmod +x stop_servers.sh
./stop_servers.sh
```

## Modeling
![aletheia_erd](https://github.com/user-attachments/assets/bdff6236-1d3d-4711-8167-07859f92b87b)

## ApiDoc
* `인증 서버 API 문서`
  * http://localhost:8888/swagger-ui/index.html
* `자원 서버 API 문서`
  * http://localhost:9999/swagger-ui/index.html
* `Postman`
  * https://documenter.getpostman.com/view/31325959/2sAXqmA4tP