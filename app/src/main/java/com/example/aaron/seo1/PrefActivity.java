package com.example.aaron.seo1;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class PrefActivity extends PreferenceActivity
{
  public void onCreate(Bundle savedInstanceState)
  {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.preferences);
  }
}
