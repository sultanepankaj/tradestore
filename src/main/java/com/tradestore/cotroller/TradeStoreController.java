package com.tradestore.cotroller;

import com.tradestore.model.TradeStore;
import com.tradestore.service.TradeStoreService;
import com.tradestore.validate.ValidateTradeInputs;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TradeStoreController {

    private final TradeStoreService tradeStoreService;
    private final ValidateTradeInputs validateTradeInputs;

    public TradeStoreController(TradeStoreService tradeStoreService, ValidateTradeInputs validateTradeInputs) {
        this.tradeStoreService = tradeStoreService;
        this.validateTradeInputs = validateTradeInputs;
    }

    @PostMapping("/trades")
    public ResponseEntity<?> createOrUpdateTrade(@Valid @RequestBody TradeStore tradeStore) {
        validateTradeInputs.validateTradeRequest(tradeStore);
        String createOrUpdateTradeResponse = tradeStoreService.createOrUpdateTrade(tradeStore);

        return switch (createOrUpdateTradeResponse) {
            case "SAME_VERSION" -> ResponseEntity.ok().build();
            case "NEW_VERSION" -> ResponseEntity.status(HttpStatus.CREATED).build();
            default -> ResponseEntity.badRequest().build();
        };
    }

    @GetMapping("/trades/{tradeId}")
    public ResponseEntity<Integer> getTradeByTradeId(@PathVariable String tradeId) {
        return tradeStoreService.getTradeByTradeId(tradeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping("/trades/{tradeId}/versions/{version}")
    public ResponseEntity<Integer> getTradeByTradeIdAndVersion(@PathVariable String tradeId, @PathVariable int version) {
        return tradeStoreService.getTradeByTradeIdAndVersion(tradeId, version)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/trades")
    public ResponseEntity<Page<TradeStore>> getTrades(
            @RequestParam String counterPartyId,
            @RequestParam Boolean expired,
            Pageable pageable) {
        return ResponseEntity.ok(tradeStoreService.getTrades(counterPartyId, expired, pageable));
    }
}
