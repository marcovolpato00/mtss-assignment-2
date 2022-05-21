////////////////////////////////////////////////////////////////////
// DAVIDE MILAN 1216733
// MARCO VOLPATO 1224826
////////////////////////////////////////////////////////////////////

package it.unipd.mtss.model;

import java.time.LocalDate;
import java.time.Period;


public class User {
    private final int id;
    private final String name;
    private final LocalDate birth;

    public User(int id, String name, LocalDate birth) {
        this.id = id;
        this.name = name;
        this.birth = birth;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public LocalDate getBirth() {
        return this.birth;
    }

    public int getAge(){
        LocalDate now = LocalDate.now();
        return Period.between(birth, now).getYears();        
    }
}