package com.rakesh.sms.beans;

public class PromotionDTO {
    private String name;
    private String pid;
    private String count;
    private String start;
    private boolean paused;

    // constructor
    public PromotionDTO(String name, String pid, String count, String start, boolean paused) {
        this.name = name;
        this.pid = pid;
        this.count = count;
        this.start = start;
        this.paused = paused;
    }

    // getters & setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPid() { return pid; }
    public void setPid(String pid) { this.pid = pid; }

    public String getCount() { return count; }
    public void setCount(String count) { this.count = count; }

    public String getStart() { return start; }
    public void setStart(String start) { this.start = start; }

    public boolean isPaused() { return paused; }
    public void setPaused(boolean paused) { this.paused = paused; }
}
