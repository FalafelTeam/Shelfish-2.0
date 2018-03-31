package com.falafelteam.shelfish.controller;

import lombok.Data;

@Data
class DocumentForm {

    private String name;
    private String description;
    private Boolean isBestseller;
    private Integer copies;
    private Integer price;
    private Boolean isReference;
    private String authors;
    private String editor;
    private String publisher;
}
