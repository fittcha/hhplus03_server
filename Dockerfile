#
# Dev phase
#
FROM openjdk:17-alpine as dev
RUN apk add --no-cache bash dos2unix

# 프로젝트 디렉토리 생성
WORKDIR /app

COPY gradlew .
# gradlew 파일의 줄바꿈을 LF로 강제
RUN dos2unix ./gradlew && chmod +x ./gradlew
COPY gradle gradle
COPY config config

# Gradle 설정 파일 복사
COPY build.gradle settings.gradle ./

# Gradle 종속성 다운로드
RUN bash ./gradlew --version
RUN bash ./gradlew dependencies

# 소스 코드 복사
COPY src src

#
# Prod-build phase
#
FROM dev as build

# 프로덕션 환경 설정
ENV SPRING_PROFILES_ACTIVE=prod

# 애플리케이션 빌드
RUN bash ./gradlew build -x test

# 생성된 JAR 파일을 더 Docker 친화적인 구조로 추출
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*.jar)

#
# Prod-deploy phase
#
FROM openjdk:17-alpine as prod

WORKDIR /app

# 앱 실행에 필요한 환경 변수 설정
ENV SPRING_PROFILES_ACTIVE=prod

# build 단계에서 빌드된 애플리케이션 파일 복사
COPY --from=build /app/build/dependency/BOOT-INF/lib /app/lib
COPY --from=build /app/build/dependency/META-INF /app/META-INF
COPY --from=build /app/build/dependency/BOOT-INF/classes /app

ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=${ACTIVE_PROFILES}"]
