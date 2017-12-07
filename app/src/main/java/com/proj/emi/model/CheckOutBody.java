package com.proj.emi.model;

import com.proj.emi.model.base.OutBody;

public class CheckOutBody extends OutBody {
    private String page_num;
    private String event_uuid;

    public String getPage_num() {
        return page_num;
    }

    public void setPage_num(String page_num) {
        this.page_num = page_num;
    }

    public String getEvent_uuid() {
        return event_uuid;
    }

    public void setEvent_uuid(String event_uuid) {
        this.event_uuid = event_uuid;
    }

    @Override
    public String toString() {
        return "{\"page_num\":\"" + page_num + "\"," +
                "\"event_uuid\":\"" + event_uuid + "\"}";
    }

}
