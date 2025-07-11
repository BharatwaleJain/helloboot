package com.example.helloboot.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.example.helloboot.entity.*;
import com.example.helloboot.repository.*;
import com.example.helloboot.dto.*;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import java.util.*;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("/list")
@CrossOrigin(originPatterns = "*")
public class ListController {
    private final ListItemRepository repo;

    public ListController(ListItemRepository repo) {
        this.repo = repo;
    }

    // Create
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody ListItem body) {
        if (body.getTitle() == null || body.getCategory() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to Add Item"));
        }
        ListItem item = new ListItem();
        item.setTitle(body.getTitle());
        item.setCategory(body.getCategory());
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(item.getCreatedAt());
        ListItem saved = repo.save(item);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Item Added Successfully",
                        "item", saved));
    }

    // Read all
    @GetMapping
    public PagedResponse<ListItemResponse> all(
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ListItem> page = repo.findAll(pageable);
        Page<ListItemResponse> responsePage = page.map(item -> new ListItemResponse(
                item.getId(),
                item.getTitle(),
                item.getCategory(),
                item.getUser() != null ? item.getUser().getName() : null,
                item.getCreatedAt(),
                item.getUpdatedAt()));
        return new PagedResponse<>(responsePage);
    }

    // Read by category
    @GetMapping("/category/{category}")
    public PagedResponse<ListItemResponse> byCategory(
            @PathVariable String category,
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ListItem> page;
        boolean hasQ = q != null && !q.isBlank();
        boolean hasDates = startDate != null && endDate != null;
        if (hasQ && hasDates) {
            page = repo.findByCategoryAndTitleContainingIgnoreCaseAndCreatedAtBetween(
                    category, q, startDate, endDate, pageable);
        } else if (hasQ) {
            page = repo.findByCategoryAndTitleContainingIgnoreCase(category, q, pageable);
        } else if (hasDates) {
            page = repo.findByCategoryAndCreatedAtBetween(category, startDate, endDate, pageable);
        } else {
            page = repo.findByCategory(category, pageable);
        }
        Page<ListItemResponse> responsePage = page.map(item -> new ListItemResponse(
                item.getId(),
                item.getTitle(),
                item.getCategory(),
                item.getUser() != null ? item.getUser().getName() : null,
                item.getCreatedAt(),
                item.getUpdatedAt()));
        return new PagedResponse<>(responsePage);
    }

    // Read one
    @GetMapping("/{id}")
    public ResponseEntity<ListItem> get(@PathVariable long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable long id, @RequestBody ListItem body) {
        return repo.findById(id).map(existing -> {
            if (body.getTitle() == null || body.getCategory() == null)
                return null;
            existing.setTitle(body.getTitle());
            existing.setCategory(body.getCategory());
            existing.setUpdatedAt(LocalDateTime.now());
            ListItem saved = repo.save(existing);
            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "Item Updated Successfully",
                            "item", saved));
        }).orElseGet(() -> ResponseEntity
                .status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Failed to Update Item")));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable long id) {
        if (!repo.existsById(id))
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to Delete Item"));
        repo.deleteById(id);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Item Deleted Successfully"));
    }
}