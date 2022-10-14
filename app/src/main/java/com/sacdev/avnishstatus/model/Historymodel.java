package com.sacdev.avnishstatus.model;
public class Historymodel {
    String  type ,description , status , created_date , creat_time;

    public Historymodel( String type, String description, String status, String created_date, String creat_time) {

        this.type = type;
        this.description = description;
        this.status = status;
        this.created_date = created_date;
        this.creat_time = creat_time;
    }


    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }
    public String getStatus() {
        return status;
    }
    public String getCreated_date() {
        return created_date;
    }
    public String getCreat_time() {
        return creat_time;
    }

}
