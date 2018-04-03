package com.falafelteam.shelfish.controller;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class SearchByIdForm {

    @Positive
    private int id;
}
