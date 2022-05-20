////////////////////////////////////////////////////////////////////
// DAVIDE MILAN 1216733
// MARCO VOLPATO 1224826
////////////////////////////////////////////////////////////////////

package it.unipd.mtss.model;

import java.util.Date;

public class User {
    private final int id;
    private final String name;
    private final Date birth;

    public User(int id, String name, Date birth) {
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

    public Date getBirth() {
        return this.birth;
    }
}