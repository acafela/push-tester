# Push Tester
- 간단히 커맨드에서 푸시 테스트를 할 수 있습니다.
- 푸시 테스트를 위해 IDE를 실행하는것이 불편해 만들었습니다. 서버 사이드에 익숙하지 않은 네이티브 개발자도 테스트 할 수 있습니다.

## 사전준비
### 안드로이드, GCM
  - GCM API key, GCM에서 단말기에 부여한 키(regisration id)
### iOS, APNs
  - APNs에서 단말기에 부여한 키, Apple 푸시 인증서

## 설치 및 사용법
- 다운로드

  ```
  git clone https://github.com/younsunghwang/push-tester
  ```

- APNs, GCM 정보 설정
  - src/main/resources/push.properties 수정

- 빌드 및 실행
  ```
  mvn install -DskipTests
  java -jar push-tester-1.0.0.jar {apns|gcm} {pushkey} {message}
  ```


- Ex) iOS 푸시발송, 내용 : helloworld, 푸시키 : 67df4ce50b922

  ```
  java -jar push-tester-1.0.0.jar apns 67df4ce50b922 helloworld
  ```

## TODO
- FCM 추가
