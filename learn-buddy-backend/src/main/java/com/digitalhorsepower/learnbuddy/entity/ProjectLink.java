package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.Embeddable;

@Embeddable
@Data
public class ProjectLink {
    private String name;
    private String url;
    private String description;
}
