package the.garage.platform.iam.domain.model.entities;

import java.time.LocalDateTime;

public class EmailVerificationToken {
    private Long id;
    private Long userId;
    private String token;
    private LocalDateTime expiresAt;
    private boolean verified;

    protected EmailVerificationToken() {}

    public EmailVerificationToken(Long userId, String token, LocalDateTime expiresAt) {
        if (userId == null) throw new IllegalArgumentException("userId cannot be null");
        if (token == null || token.isBlank()) throw new IllegalArgumentException("token cannot be null or blank");
        if (expiresAt == null) throw new IllegalArgumentException("expiresAt cannot be null");
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.verified = false;
    }

    /**
     * Reconstructs a token from persistence. Used by the assembler layer only.
     */
    public static EmailVerificationToken reconstitute(Long id, Long userId, String token, LocalDateTime expiresAt, boolean verified) {
        var instance = new EmailVerificationToken(userId, token, expiresAt);
        instance.id = id;
        instance.verified = verified;
        return instance;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public void markVerified() {
        if (verified) throw new IllegalStateException("Token was already used");
        if (isExpired()) throw new IllegalStateException("Token has expired");
        this.verified = true;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getToken() { return token; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public boolean isVerified() { return verified; }
}
