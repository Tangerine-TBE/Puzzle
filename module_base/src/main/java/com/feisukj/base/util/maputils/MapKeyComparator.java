package com.feisukj.base.util.maputils;

import java.util.Comparator;

class MapKeyComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {

        return str1.compareTo(str2);
    }
}
