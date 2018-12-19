package com.link.dheyaa.textme;

public class Message {
    private String value;
    private long time;

    private Message(){

    }
    public Message(String value,long time){
        this.value=value;
        this.time=time;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value=value;
    }

    public long getTime(){
        return time;
    }

    public void setTime(long time){
        this.time=time;
    }

    public Message clone(){
        Message msg=new Message(value,time);
        return msg;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Message){
            Message msg=(Message)o;
            if(this==msg){
                return true;
            }
            else{
                return (this.time==msg.time&&this.value.equals(msg.value));
            }
        }
        return false;
    }

    @Override
    public String toString(){
        String s="Message{ time='"+time+"\', Content='"+value+"}";
        return s;
    }
}
