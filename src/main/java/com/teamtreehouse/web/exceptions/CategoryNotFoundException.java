package com.teamtreehouse.web.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException() {
        super("Sorry, no such category exists. Please try again.");
    }
}
