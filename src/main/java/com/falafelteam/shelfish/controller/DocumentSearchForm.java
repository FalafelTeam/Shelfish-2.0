package com.falafelteam.shelfish.controller;

import lombok.Data;

@Data
public class DocumentSearchForm {
    private String type;
    private String searchCriteria;
    private String search;
}
