package ru.sql.proj.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

public class Order {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long id;

    private String name;

    private Date date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static class Row extends Order {

        @JsonIgnore
        private final int entryNumber;

        public Row(int entryNumber) {
            this.entryNumber = entryNumber;
        }

        public int getEntryNumber() {
            return entryNumber;
        }

    }


}
