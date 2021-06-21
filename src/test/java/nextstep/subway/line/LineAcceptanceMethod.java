package nextstep.subway.line;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;

public class LineAcceptanceMethod {
	public static ExtractableResponse<Response> createLine(LineRequest lineRequest) {
		return RestAssured
			.given().log().all()
			.body(lineRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.post("/lines")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> findLine(String createLineId) {
		return RestAssured
			.given().log().all()
			.when()
			.get("/lines" + "/" + createLineId)
			.then().log().all()
			.extract();
	}

	public static String getLineID(ExtractableResponse<Response> createResponse) {
		return createResponse.header("Location").split("/")[2];
	}

	public static ExtractableResponse<Response> updateLine(String id, LineRequest lineRequest) {
		return RestAssured
			.given().log().all()
			.body(lineRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.put("/lines" + "/" + id)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> deleteLine(String id) {
		return RestAssured
			.given().log().all()
			.when()
			.delete("/lines" + "/" + id)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> findAllLines() {
		return RestAssured
			.given().log().all()
			.when()
			.get("/lines")
			.then().log().all()
			.extract();
	}
}