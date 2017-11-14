package com.hybris.students.orders;

import com.hybris.students.orders.domain.InventoryEntryImpl;

import java.util.*;

public class Lists<T> {

    public List<T> addObjToList(T obj, List<T> list) {
        if (list == null) {
            list = new ArrayList<T>();
        }
        list.add(obj);

        return list;
    }


    /*public Map<String, List<T>> addObjToMap(Collection<? extends T> collection) {
        Map<String, List<T>> objMap = new HashMap<String, List<T>>();

        for (T t : collection) {
            if (t instanceof InventoryEntry) {

                *//*objMap.put(((InventoryEntry) t).getSku(),
                        addObjToList(new InventoryEntryImpl(((InventoryEntry) t)), objMap.get()));*//*
            }
        }
    }*/
}
