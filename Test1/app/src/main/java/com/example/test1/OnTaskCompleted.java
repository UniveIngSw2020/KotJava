package com.example.test1;

import java.util.List;
// serve per ritornare i ReceiveItem dall' AsyncReceive
public interface OnTaskCompleted{
    void onTaskCompleted(List<ReceiveItem> fromserver);
}
