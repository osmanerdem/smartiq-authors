package com.smartiq.authors.controller;

import com.smartiq.authors.model.entity.Text;
import com.smartiq.authors.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class TextController {

    private final TextService textService;

    @Autowired
    public TextController(TextService textService) {
        this.textService = textService;
    }

    // Endpoint: Yazara Yeni bir yazı ekleme
    @PostMapping("/add-text")
    public ResponseEntity<Map<String, Object>> addText(@RequestParam Long authorId, @RequestBody Text text) {
        if (authorId == null || authorId <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Author ID geçersiz."));
        }
        if (text == null || text.getContent() == null || text.getContent().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Yazı içeriği boş olamaz."));
        }

        try {
            textService.addTextToAuthor(authorId, text);
            return ResponseEntity.ok(Map.of("succes", "Yazı başarıyla eklendi."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Bir hata oluştu."));
        }
    }

    // Endpoint: Yazarın en çok kullandığı 30 kelimeyi döndür
    @GetMapping("/authors/{authorId}/most-frequent-words")
    public List<Map.Entry<String, Long>> getMostFrequentWords(@PathVariable Long authorId) {
        return textService.getMostFrequentWords(authorId);
    }

    // Endpoint: Verilen yazı en çok hangi yazara benziyor döndür
    @PostMapping("/analyze-text")
    public ResponseEntity<Map<String, Object>> analyzeText(@RequestBody Map<String, String> requestBody) {
        String text = requestBody.get("text");
        if (text == null || text.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Text cannot be null or empty"));
        }
        String algorithm = requestBody.get("algorithm");
        Map<String, Object> result = textService.analyzeTextAndFindAuthor(text, algorithm);
        return ResponseEntity.ok(result);
    }
}
