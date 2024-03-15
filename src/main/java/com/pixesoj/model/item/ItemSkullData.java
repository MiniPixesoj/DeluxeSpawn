package com.pixesoj.model.item;

public class ItemSkullData {
    private String owner;
    private String texture;
    private String id;

    public ItemSkullData(String owner, String texture, String id) {
        this.owner = owner;
        this.texture = texture;
        this.id = id;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTexture() {
        return this.texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ItemSkullData clone() {
        return new ItemSkullData(this.owner, this.texture, this.id);
    }
}
