package co.com.sofkau.entrenamiento.curso.values;

import co.com.sofka.domain.generic.ValueObject;

import java.util.Objects;

public class Directiz implements ValueObject<String> {

    private final String value;

    public Directiz(String value) {
        this.value = Objects.requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException("La directriz no debe estar vacía");
        }
    }

    @Override
    public String value() {
        return value;
    }
}
