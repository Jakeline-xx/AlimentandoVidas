package com.projeto.alimentandovidas.model;

public record RestValidationError(
        Integer code,
        String field,
        String message
) {}