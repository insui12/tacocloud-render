# ── 실습과제 25-3 — Render 호스팅용 Dockerfile ─────────────────────
# 강의자료 슬라이드 25 (Create a Dockerfile) 의 지시를 그대로 따른다:
#   FROM          베이스 이미지
#   VOLUME        mount point
#   COPY          target 폴더의 jar 를 taco.jar 로 복사
#   ENTRYPOINT    실행 명령
#   EXPOSE        외부 노출 포트 (Render 는 자체 PORT 환경변수도 함께 사용)

# 빌드 단계 — Maven + JDK 21 베이스로 jar 패키징
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
# pom.xml 만 먼저 복사해 의존성을 다운로드 (Render 빌드 캐시 효과)
RUN mvn -q -B -DskipTests dependency:go-offline || true
COPY src src
RUN mvn -q -B -DskipTests package

# 실행 단계 — slim JRE 만으로 띄움
FROM eclipse-temurin:21-jre
VOLUME /tmp
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/taco.jar
EXPOSE 8085
ENV SPRING_PROFILES_ACTIVE=prod
# Render Free tier 는 512 MB 메모리 제한 → JVM heap 을 명시적으로 매우 작게 두지 않으면 OOM kill
# -Xmx256m: 힙 상한 256 MB
# -XX:MaxMetaspaceSize=96m: class metadata 영역 상한
# -XX:ReservedCodeCacheSize=48m: JIT 코드 캐시 상한
# -XX:+UseSerialGC: 단일 코어 인스턴스에서 G1 보다 메모리 효율적
# -XX:TieredStopAtLevel=1: JIT 컴파일을 C1 까지만 사용 (메모리/CPU 절약, 시작 빠름)
# -Djava.security.egd=file:/dev/./urandom: SecureRandom 초기화 지연 회피
ENTRYPOINT ["sh", "-c", "java -Xms64m -Xmx256m -XX:MaxMetaspaceSize=96m -XX:ReservedCodeCacheSize=48m -XX:+UseSerialGC -XX:TieredStopAtLevel=1 -Djava.security.egd=file:/dev/./urandom -jar /app/taco.jar"]
