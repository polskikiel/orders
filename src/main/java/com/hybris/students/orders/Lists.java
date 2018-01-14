package com.hybris.students.orders;

import java.util.ArrayList;
import java.util.List;

public class Lists<T> {

    public List<T> addObjToList(T obj, List<T> list) {
        if (list == null) {
            list = new ArrayList<T>();
        }
        list.add(obj);

        return list;
    }
}
