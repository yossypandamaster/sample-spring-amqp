package net.pandamaster.sample.spring.amqp.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomEntry {
    @JsonProperty("room")
    private String room;

    /**
     * @return the room
     */
    public String getRoom() {
        return room;
    }

    /**
     * @param room the room to set
     */
    public void setRoom(String room) {
        this.room = room;
    }
}
