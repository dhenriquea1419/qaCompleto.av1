package com.example.demo.controller;

import com.example.demo.exception.InvalidItemDataException;
import com.example.demo.model.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/items")
public class ApiController {

    private List<Item> items = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(items);
    }

    @GetMapping(value = "/search", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Item>> searchItemsByName(@RequestParam("name") String name) {
        validateSearchName(name);
        List<Item> result = items.stream()
                .filter(item -> item.getName() != null && item.getName().toLowerCase().contains(name.trim().toLowerCase()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        validateItem(item);
        item.setId(counter.incrementAndGet());
        items.add(item);
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item item) {
        validateItem(item);
        return items.stream()
                .filter(existing -> id.equals(existing.getId()))
                .findFirst()
                .map(existing -> {
                    existing.setName(item.getName());
                    existing.setDescription(item.getDescription());
                    return ResponseEntity.ok(existing);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Item> patchItem(@PathVariable Long id, @RequestBody Item item) {
        validatePatchItem(item);
        return items.stream()
                .filter(existing -> id.equals(existing.getId()))
                .findFirst()
                .map(existing -> {
                    if (item.getName() != null) {
                        existing.setName(item.getName());
                    }
                    if (item.getDescription() != null) {
                        existing.setDescription(item.getDescription());
                    }
                    return ResponseEntity.ok(existing);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        boolean removed = items.removeIf(item -> id.equals(item.getId()));
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/count", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Long> getItemCount() {
        return ResponseEntity.ok((long) items.size());
    }

    @PostMapping(value = "/bulk", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Item>> createItemsBulk(@RequestBody List<Item> newItems) {
        if (newItems == null || newItems.isEmpty()) {
            throw new InvalidItemDataException("Lista de itens não pode ser vazia.");
        }
        List<Item> createdItems = new ArrayList<>();
        for (Item item : newItems) {
            validateItem(item);
            item.setId(counter.incrementAndGet());
            createdItems.add(item);
            items.add(item);
        }
        return new ResponseEntity<>(createdItems, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return items.stream()
                .filter(item -> id.equals(item.getId()))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private void validateItem(Item item) {
        if (item == null) {
            throw new InvalidItemDataException("Item não pode ser nulo.");
        }
        if (item.getName() == null || item.getName().trim().isEmpty()) {
            throw new InvalidItemDataException("Nome do item é obrigatório e não pode ser vazio.");
        }
        if (item.getDescription() == null || item.getDescription().trim().isEmpty()) {
            throw new InvalidItemDataException("Descrição do item é obrigatória e não pode ser vazia.");
        }
    }

    private void validateSearchName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidItemDataException("Parâmetro de busca 'name' é obrigatório e não pode ser vazio.");
        }
    }

    private void validatePatchItem(Item item) {
        if (item == null) {
            throw new InvalidItemDataException("Dados de patch não podem ser nulos.");
        }
        boolean hasName = item.getName() != null;
        boolean hasDescription = item.getDescription() != null;
        if (!hasName && !hasDescription) {
            throw new InvalidItemDataException("Pelo menos um campo deve ser informado para atualização parcial.");
        }
        if (hasName && item.getName().trim().isEmpty()) {
            throw new InvalidItemDataException("Nome do item não pode ser vazio.");
        }
        if (hasDescription && item.getDescription().trim().isEmpty()) {
            throw new InvalidItemDataException("Descrição do item não pode ser vazia.");
        }
    }
}
