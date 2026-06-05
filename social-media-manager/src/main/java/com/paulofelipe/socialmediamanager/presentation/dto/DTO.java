package com.paulofelipe.socialmediamanager.presentation.dto;

    public class DTO {

        private Long id;
        private String name;
        private String email;

        public DTO(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
    }

