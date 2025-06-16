package com.example.helloboot.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.helloboot.entity.ListItem;
import com.example.helloboot.repository.ListRepository;
import java.util.*;
import java.time.LocalDateTime;
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
    public List<ListItem> all() {
        return repo.findAll();
    }
    // Read by category
    @GetMapping("/category/{category}")
    public List<ListItem> byCategory(@PathVariable String category) {
        return repo.findAll().stream()
            .filter(item -> item.getCategory().equals(category))
            .toList();
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
    // private final Map<Long, List> db = new HashMap<>();
    // private long nextId = 1;
    // @PostMapping
    // public ResponseEntity<List> create(@RequestBody List body) {
    //     if (body.getTitle() == null || body.getCategory() == null)
    //         return ResponseEntity.badRequest().build();
    //     body.setId(nextId++);
    //     body.setCreatedAt(LocalDateTime.now());
    //     body.setUpdatedAt(LocalDateTime.now());
    //     db.put(body.getId(), body);
    //     return ResponseEntity.ok(body);
    // }
    // @GetMapping
    // public Collection<List> all() {
    //     return db.values();
    // }
    // @GetMapping("/{id}")
    // public ResponseEntity<List> get(@PathVariable long id) {
    //     List found = db.get(id);
    //     if (found == null)
    //         return ResponseEntity.notFound().build();
    //     return ResponseEntity.ok(found);
    // }
    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> delete(@PathVariable long id) {
    //     List removed = db.remove(id);
    //     if (removed == null)
    //         return ResponseEntity.notFound().build();
    //     return ResponseEntity.noContent().build();
    // }
    // @PutMapping("/{id}")
    // public ResponseEntity<List> update(@PathVariable long id, @RequestBody List body) {
    //     List existing  = db.get(id);
    //     if (existing == null)
    //         return ResponseEntity.notFound().build();
    //     if (body.getTitle() == null || body.getCategory() == null)
    //         return ResponseEntity.badRequest().build();
    //     existing.setTitle(body.getTitle());
    //     existing.setCategory(body.getCategory());
    //     existing.setUpdatedAt(LocalDateTime.now());
    //     return ResponseEntity.ok(existing);
    // }
}