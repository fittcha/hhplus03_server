FROM openjdk:17-jdk-slim

ENV TZ=Asia/Seoul

# 컨테이너 내부에서 애플리케이션 파일을 저장할 디렉토리를 생성
WORKDIR /app

# 빌드된 JAR 파일을 현재 위치에서 컨테이너의 /app 디렉토리로 복사
COPY build/libs/hhplus03_server-1.0-SNAPSHOT.jar /app/hhplus03_server.jar

# 기본값으로 'local' 설정
ENV ACTIVE_PROFILES=local

# 애플리케이션을 실행
ENTRYPOINT ["java", "-jar", "/app/hhplus03_server.jar", "--spring.profiles.active=${ACTIVE_PROFILES}"]
