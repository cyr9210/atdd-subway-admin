package nextstep.subway.ui;

import java.net.URI;
import nextstep.subway.application.LineService;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Lines;
import nextstep.subway.dto.LineRequest;
import nextstep.subway.dto.LineResponse;
import nextstep.subway.dto.LinesResponse;
import nextstep.subway.dto.SectionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/lines")
@RestController
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> create(@RequestBody LineRequest request) {
        Line line = lineService.create(request);
        LineResponse lineResponse = new LineResponse(line);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(lineResponse);
    }

    @GetMapping
    public ResponseEntity<LinesResponse> getLists() {
        Lines lines = lineService.getLines();
        return ResponseEntity.ok(lines.toResponse());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> get(@PathVariable Long id) {
        Line line = lineService.get(id);
        return ResponseEntity.ok(new LineResponse(line));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody LineRequest request) {
        lineService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        lineService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<LineResponse> addSection(@PathVariable Long id, @RequestBody SectionRequest request) {
        Line line = lineService.addSection(id, request);
        return ResponseEntity.ok(new LineResponse(line));
    }

    @DeleteMapping("/{id}/sections")
    public ResponseEntity<LineResponse> removeSection(@PathVariable Long id, Long stationId) {
        Line line = lineService.removeSection(id, stationId);
        return ResponseEntity.ok(new LineResponse(line));
    }

}
