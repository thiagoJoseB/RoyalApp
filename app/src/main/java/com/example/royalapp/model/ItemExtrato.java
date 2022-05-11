package com.example.royalapp.model;

//**05 passo aula de recyclerView*//


///06 passo aula de recyclerView

public class ItemExtrato {

    private int type;
    private Object object;




///07 construtor  passo aula de recyclerView
    public ItemExtrato(int type, Object object){
        this.type = type;
        this.object = object;
    }

    ///08 get and set passo aula de recyclerView


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
