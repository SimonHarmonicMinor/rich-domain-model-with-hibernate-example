package com.example.demo.domain.command;

import com.example.demo.domain.Status;

public record TamagotchiUpdateRequest(String name, Status status) {

}
