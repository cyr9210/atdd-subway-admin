package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.ui.LineController;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.TestFactory;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

	private LineResponse 신분당선;
	private StationResponse 강남역;
	private StationResponse 광교역;
	private StationResponse 양재역;
	private StationResponse 새상행역;
	private StationResponse 새하행역;
	private Map<String, String> createParams;

	@BeforeEach
	public void setUp() {
		super.setUp();

		// given
		강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
		광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
		양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
		새상행역 = StationAcceptanceTest.지하철역_등록되어_있음("새상행역").as(StationResponse.class);
		새하행역 = StationAcceptanceTest.지하철역_등록되어_있음("새하행역").as(StationResponse.class);

		createParams = new HashMap<>();
		createParams.put("name", "신분당선");
		createParams.put("color", "bg-red-600");
		createParams.put("upStationId", 강남역.getId() + "");
		createParams.put("downStationId", 광교역.getId() + "");
		createParams.put("distance", 10 + "");
		신분당선 = 지하철_노선_등록되어_있음(createParams).as(LineResponse.class);
	}

	private ExtractableResponse<Response> 지하철_노선_등록되어_있음(Map<String, String> params) {
		return TestFactory.create(LineController.BASE_URI, params);
	}

	@Test
	@DisplayName("구간 사이에 구간을 추가한다")
	void addSection() {
		// given
		Map<String, String> params = new HashMap<>();
		params.put("upStationId", 강남역.getId() + "");
		params.put("downStationId", 양재역.getId() + "");
		params.put("distance", 1 + "");

		// when
		ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, params);

		// then
		지하철_노선에_지하철역_등록됨(response);
	}

	@Test
	@DisplayName("상행 종점에 새 구간을 추가한다")
	void addSection2() {
		// given
		Map<String, String> params = new HashMap<>();
		params.put("upStationId", 새상행역.getId() + "");
		params.put("downStationId", 강남역.getId() + "");
		params.put("distance", 1 + "");

		// when
		ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, params);

		// then
		지하철_노선에_상행종점이_등록됨(response);
	}

	@Test
	@DisplayName("하행 종점에 새 구간을 추가한다")
	void addSection3() {
		// given
		Map<String, String> params = new HashMap<>();
		params.put("upStationId", 광교역.getId() + "");
		params.put("downStationId", 새하행역.getId() + "");
		params.put("distance", 1 + "");

		// when
		ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, params);

		// then
		지하철_노선에_하행종점이_등록됨(response);
	}

	private void 지하철_노선에_하행종점이_등록됨(ExtractableResponse<Response> response) {
		List<StationResponse> stations = response.as(LineResponse.class).getStations();
		assertAll(
			() -> assertThat(stations).containsExactly(강남역, 광교역, 새하행역),
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
		);
	}

	private void 지하철_노선에_상행종점이_등록됨(ExtractableResponse<Response> response) {
		List<StationResponse> stations = response.as(LineResponse.class).getStations();
		assertAll(
			() -> assertThat(stations).containsExactly(새상행역, 강남역, 광교역),
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
		);
	}

	private void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> response) {
		List<StationResponse> stations = response.as(LineResponse.class).getStations();
		assertAll(
			() -> assertThat(stations).containsExactly(강남역, 양재역, 광교역),
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
		);
	}

	private ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(LineResponse line, Map<String, String> params) {
		String uri = LineController.BASE_URI + "/" + line.getId() + "/sections";
		return TestFactory.create(uri, params);
	}

}