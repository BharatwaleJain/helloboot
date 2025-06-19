package com.example.helloboot.controller;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.example.helloboot.entity.ListItem;
import com.example.helloboot.repository.ListRepository;
import com.example.helloboot.dto.PagedResponse;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import java.util.*;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
@RestController
@RequestMapping("/list")
@CrossOrigin(originPatterns = "*")
public class ListController {
    private final ListRepository repo;
    public ListController(ListRepository repo) {
        this.repo = repo;
    }
    // Create
    @PostMapping
    public ResponseEntity<Map<String,Object>> create(@RequestBody ListItem body) {
        if (body.getTitle() == null || body.getCategory() == null)
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "success", false,
                    "message", "Failed to Add Item"
                )
            );
        body.setCreatedAt(LocalDateTime.now());
        body.setUpdatedAt(body.getCreatedAt());
        ListItem saved = repo.save(body);
        return ResponseEntity.ok(
            Map.of(
                "success", true,
                "message", "Item Added Successfully",
                "item", saved
            )
        );
    }
    // Read all
    @GetMapping
    public PagedResponse<ListItem> all(@PageableDefault(
        size=5,
        sort="createdAt",
        direction=Sort.Direction.DESC
    ) Pageable pageable) {
        return new PagedResponse<>(
            repo.findAll(pageable)
        );
    }
    // Read by category
    @GetMapping("/category/{category}")
    public PagedResponse<ListItem> byCategory(
        @PathVariable String category,
        @RequestParam(name = "q", required = false) String q,
        @RequestParam(name = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam(name = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
        @PageableDefault(
            size=5,
            sort="createdAt",
            direction=Sort.Direction.DESC
        ) Pageable pageable
    ) {
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
        return new PagedResponse<>(page);
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
    public ResponseEntity<Map<String,Object>> update(@PathVariable long id, @RequestBody ListItem body) {
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
                    "item", saved
                )
            );
        }).orElseGet(() -> ResponseEntity
            .status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", "Failed to Update Item"
            ))
        );
    }
    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,Object>> delete(@PathVariable long id) {
        if (!repo.existsById(id))
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "success", false,
                    "message", "Failed to Delete Item"
                ));
        repo.deleteById(id);
        return ResponseEntity.ok(
            Map.of(
                "success", true,
                "message", "Item Deleted Successfully"
            )
        );
    }
}