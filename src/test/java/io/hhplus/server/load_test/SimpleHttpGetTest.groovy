package io.hhplus.server.load_test

import net.grinder.plugin.http.HTTPRequest
import net.grinder.script.Grinder
import net.grinder.scriptengine.groovy.junit.GrinderRunner
import net.grinder.scriptengine.groovy.junit.annotation.BeforeProcess
import net.grinder.scriptengine.groovy.junit.annotation.BeforeThread
import net.grinder.scriptengine.groovy.junit.annotation.Test as NGrinderTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(GrinderRunner)
class SimpleHttpGetTest {

    public static Test test1;
    public static HTTPRequest request;

    @BeforeProcess
    public static void beforeProcess() {
        // 테스트 초기화
        test1 = new Test(1, "HTTP GET Test");
        request = new HTTPRequest();
        Grinder.logger.info("프로세스 시작 전 설정 완료.");
    }

    @BeforeThread
    public void beforeThread() {
        // 스레드(사용자)별 초기화
        test1.record(this, "testHttpGet");
        Grinder.logger.info("스레드 시작 전 설정 완료.");
    }

    @NGrinderTest
    public void testHttpGet() {
        // 실제 테스트 실행 부분
        try {
            def response = request.GET("http://example.com") ;// 여기에 요청하고자 하는 URL 입력
            assert response.statusCode == 200 : "예상한 응답 코드(200)가 아닙니다. 응답 코드: ${response.statusCode}";
            Grinder.logger.info("HTTP GET 요청 성공: 응답 코드 = ${response.statusCode}");
        } catch (Exception e) {
            Grinder.logger.error("HTTP GET 요청 중 오류 발생: ${e.message}");
        }
    }
}
