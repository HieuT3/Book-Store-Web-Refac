package com.bookstore.app.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class CartItems {
    private final Map<Long, Integer> items = new LinkedHashMap<>();

    public void addItem(Long bookId, int quantity) {
        if(items.containsKey(bookId)) items.put(bookId, items.get(bookId) + quantity);
        else items.put(bookId, quantity);
    }

    public void updateItem(Long bookId, int quantity) {
        if(!items.containsKey(bookId)) return;
        if (quantity == 0) items.remove(bookId);
        else items.put(bookId, quantity);
    }

    public void removeItem(Long bookId) {
        items.remove(bookId);
    }

    public int getTotalItems() {
        return items.size();
    }

    public int getTotalQuantities() {
        return items.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public void clearItems() {
        items.clear();
    }
}
