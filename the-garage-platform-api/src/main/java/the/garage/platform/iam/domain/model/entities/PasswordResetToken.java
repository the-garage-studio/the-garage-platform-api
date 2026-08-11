package the.garage.platform.iam.domain.model.entities;

import java.time.LocalDateTime;

public class PasswordResetToken {

    private Long id;
    private Long userId;
    private String token;
    private LocalDateTime expiresAt;
    private boolean used;

    public PasswordResetToken(Long userId, String token, LocalDateTime expiresAt) {
        if (userId == null) throw new IllegalArgumentException("userId cannot be null");
        if (token == null || token.isBlank()) throw new IllegalArgumentException("token cannot be null or blank");
        if (expiresAt == null) throw new IllegalArgumentException("expiresAt cannot be null");
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.used = false;
    }

    /**
     * Reconstructs a token from persistence. Used by the assembler layer only.
     */
    public static PasswordResetToken reconstitute(Long id, Long userId, String token, LocalDateTime expiresAt, boolean used) {
        var instance = new PasswordResetToken(userId, token, expiresAt);
        instance.id = id;
        instance.used = used;
        return instance;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public void consume() {
        if (used) throw new IllegalStateException("Token was already used");
        if (isExpired()) throw new IllegalStateException("Token has expired");
        this.used = true;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getToken() { return token; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public boolean isUsed() { return used; }
}