package com.aimoodchecker.repository;

public class EntryRepository {
    private static EntryRepository instance;
    
    private EntryRepository() {}
    
    public static EntryRepository getInstance() {
        if (instance == null) {
            instance = new EntryRepository();
        }
        return instance;
    }
    
    // TODO: Implement repository methods
}


