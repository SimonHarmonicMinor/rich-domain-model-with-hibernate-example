package com.example.demo.domain.command;

import com.example.demo.domain.Status;

public record TamagotchiCreateRequest(String name, Status status) {

}
