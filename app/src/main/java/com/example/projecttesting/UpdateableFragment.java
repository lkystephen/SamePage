package com.example.projecttesting;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import java.util.List;

public interface UpdateableFragment {
    public void update(List<EventTypes> data);

}
