package io.github.emvnuel;

import javax.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String telephone) {

    public User toModel() {
        return new User(this.name, this.email, this.telephone);
    }

}
