
# 멤버십 서비스 API

## Feature
* Java 8, Sprint boot 2.5.x
* JPA, h2DB
* Gradle

## 프로젝트 설명
> 멤버십 적립을 위한 서비스 입니다. <br>
사용자는 원하는 멤버십을 등록할 수 있습니다.(3가지 고정) <br>
> membership_id : spc, shinsegae, cj <br>
> membership_name : happypoint, shinsegae, cjone

이 프로젝트는 UI를 제외한 REST API 구현하는 것이 목표입니다.

### 기능정의
- 멤버십 전체조회하기
- 멤버십 등록하기
- 멤버십 상세조회하기
- 멤버십 삭제하기
- 포인트 적립하기

## Getting Started
* build
```cmd
./gradlew clean bootJar
```
* run
```cmd
java -jar build/libs/pay-0.0.1-SNAPSHOT.jar
```
##API
~~~
요청 HEADER
X_USER_ID : ID입력하세요.
~~~
### 멤버십등록
> 멤버십 등록 <br>
> 유저가 있으면 멤버십 상세데이터 등록. <br>
> 유저가 없으면 유저 등록 후 멤버십 상세데이터 등록. <br>
> 요청 : POST /api/v1/membership

### 멤버십 전체조회

> 유저 하위의 멤버십 전체조회<br>
> 요청 : GET /api/v1/membership`

### 멤버십 상세조회
> 유저키와 멤버십키를 통한 멤버십 상세조회<br>
> 요청 : GET /api/v1/membership/{membershipId}

### 멤버십 삭제
> 유저키와 멤버십키를 기반으로 멤버십 상세데이터 비활성화 전환<br>
> 요청 : PUT /api/v1/membership/{membershipId}

### 포인트 적립
> 유저키와 멤버십키를 기반으로 멤버십 상세데이터 포인트 적립<br>
> 요청 : PATCH /api/v1/membership/point