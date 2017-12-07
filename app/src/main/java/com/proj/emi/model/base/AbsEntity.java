package com.proj.emi.model.base;

public class AbsEntity<HEAD, BODY> {

    private HEAD head;
    private BODY body;

    public HEAD getHead() {
        return head;
    }

    public void setHead(HEAD head) {
        this.head = head;
    }

    public BODY getBody() {
        return body;
    }

    public void setBody(BODY body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "AbsBody [head=" + head + ", body=" + body + "]";
    }

}
