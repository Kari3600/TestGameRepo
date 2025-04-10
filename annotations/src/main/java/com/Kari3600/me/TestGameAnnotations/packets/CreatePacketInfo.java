package com.Kari3600.me.TestGameAnnotations.packets;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;

public class CreatePacketInfo {
    public TypeElement clazz;
    public CreatePacketInfo parent;
    public List<CreatePacketInfo> children;
    public CreatePacketInfo(TypeElement clazz) {
        this.clazz = clazz;
        this.parent = null;
        this.children = new ArrayList<>();
    }
    @Override
    public String toString() {
        return "Class: "+clazz.getQualifiedName().toString() + "\n" + 
        "Parent: "+parent;
        
    }
}
