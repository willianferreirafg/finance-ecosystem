package com.finance.backend.core.domain.model;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    private UUID id;
    private String name;
    private String icon;
    private String color;
    private User user; // Entidade pura de domínio
}
