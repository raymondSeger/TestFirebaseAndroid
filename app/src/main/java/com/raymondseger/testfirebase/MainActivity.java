package com.raymondseger.testfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    // firebase analytic
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set firebase
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        // https://firebase.google.com/docs/reference/android/com/google/firebase/analytics/FirebaseAnalytics.Param.html#constants
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "12312");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "randomName");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "random type");
        mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT , bundle); // https://firebase.google.com/docs/reference/android/com/google/firebase/analytics/FirebaseAnalytics.Event.html#constants
    }
}
