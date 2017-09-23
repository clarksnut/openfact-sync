package org.openfact.repositories.user;

public class MailRepository {

    private final String email;
    private final String refreshToken;

    private MailRepository(Builder builder) {
        this.email = builder.email;
        this.refreshToken = builder.refreshToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getEmail() {
        return email;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public static class Builder {
        private String email;
        private String refreshToken;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public MailRepository build() {
            return new MailRepository(this);
        }
    }
}
