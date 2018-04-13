package com.falafelteam.shelfish.controller;

import lombok.Data;

import javax.validation.constraints.Positive;

/**
 * Class for the form used in document and user search by id
 */
@Data
public class SearchByIdForm {

    @Positive
    private int id;
}
