package com.amalitech.usermanagementservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "auth_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String token;

    private boolean blacklisted;

    @Column(nullable = false)
    private Long userId;

}

