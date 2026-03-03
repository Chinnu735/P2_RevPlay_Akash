package com.revplay.app.dto;

public class UserStatsResponse {
    private Long userId;
    private String username;
    private long totalPlaylists;
    private long totalFavorites;
    private long totalListeningCount;

    public UserStatsResponse() {
    }

    public static UserStatsResponseBuilder builder() {
        return new UserStatsResponseBuilder();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long id) {
        this.userId = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public long getTotalPlaylists() {
        return totalPlaylists;
    }

    public void setTotalPlaylists(long count) {
        this.totalPlaylists = count;
    }

    public long getTotalFavorites() {
        return totalFavorites;
    }

    public void setTotalFavorites(long count) {
        this.totalFavorites = count;
    }

    public long getTotalListeningCount() {
        return totalListeningCount;
    }

    public void setTotalListeningCount(long count) {
        this.totalListeningCount = count;
    }

    public static class UserStatsResponseBuilder {
        private UserStatsResponse response = new UserStatsResponse();

        public UserStatsResponseBuilder userId(Long id) {
            response.setUserId(id);
            return this;
        }

        public UserStatsResponseBuilder username(String name) {
            response.setUsername(name);
            return this;
        }

        public UserStatsResponseBuilder totalPlaylists(long count) {
            response.setTotalPlaylists(count);
            return this;
        }

        public UserStatsResponseBuilder totalFavorites(long count) {
            response.setTotalFavorites(count);
            return this;
        }

        public UserStatsResponseBuilder totalListeningCount(long count) {
            response.setTotalListeningCount(count);
            return this;
        }

        public UserStatsResponse build() {
            return response;
        }
    }
}
