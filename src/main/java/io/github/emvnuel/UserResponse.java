package io.github.emvnuel;

public record UserResponse(Long id, String name, String email, String telephone) {

    public UserResponse(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getTelephone());
    }
}
