# 빌더 이미지
FROM gradle:7.2.0-jdk17 AS builder

WORKDIR /app
COPY . .
RUN gradle clean build

# 프로덕션 이미지
FROM openjdk:17-jdk-slim
# 타임존
ENV TZ=Asia/Seoul
# 기본값으로 'local' 설정
ENV ACTIVE_PROFILES=local

# 필요한 폴더 생성
RUN mkdir -p /main
RUN mkdir -p /configs

RUN apt-get update && apt-get install -y ca-certificates && update-ca-certificates

WORKDIR /app

# 빌더 스테이지에서 생성된 JAR 파일 복사
COPY configs/ app/configs/
COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=${ACTIVE_PROFILES}"]

