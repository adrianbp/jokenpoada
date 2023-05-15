package tech.ada.games.jokenpo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.ada.games.jokenpo.dto.RankingDto;
import tech.ada.games.jokenpo.service.RankingService;

@RestController
@RequestMapping("/api/v1/jokenpo/ranking")
public class RankingController implements RankingControllerDocs {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("")
    public ResponseEntity<List<RankingDto>> getRanking() {
        return ResponseEntity.ok(rankingService.getRanking());
    }

}
