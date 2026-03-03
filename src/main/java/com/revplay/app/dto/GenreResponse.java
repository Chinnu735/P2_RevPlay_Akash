package com.revplay.app.dto;

public class GenreResponse {
    private Long id;
    private String name;
    private String description;

    public GenreResponse() {
    }

    public GenreResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public GenreResponse(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static GenreResponseBuilder builder() {
        return new GenreResponseBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class GenreResponseBuilder {
        private GenreResponse response = new GenreResponse();

        public GenreResponseBuilder id(Long id) {
            response.setId(id);
            return this;
        }

        public GenreResponseBuilder name(String name) {
            response.setName(name);
            return this;
        }

        public GenreResponseBuilder description(String description) {
            response.setDescription(description);
            return this;
        }

        public GenreResponse build() {
            return response;
        }
    }
}
