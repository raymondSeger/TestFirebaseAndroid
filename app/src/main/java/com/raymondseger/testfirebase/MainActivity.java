package com.raymondseger.testfirebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // firebase analytic
    private FirebaseAnalytics mFirebaseAnalytics;
    
    // firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextView logged_in_or_not;
    private TextView profile_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set firebase analytics
        mFirebaseAnalytics  = FirebaseAnalytics.getInstance(this);
        Bundle bundle       = new Bundle();
        // https://firebase.google.com/docs/reference/android/com/google/firebase/analytics/FirebaseAnalytics.Param.html#constants
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "12312");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "randomName");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "random type");
        mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT , bundle); // https://firebase.google.com/docs/reference/android/com/google/firebase/analytics/FirebaseAnalytics.Event.html#constants

        // set firebase auth
        mAuth               = FirebaseAuth.getInstance();
        logged_in_or_not    = (TextView) findViewById(R.id.logged_in_or_not);
        profile_data        = (TextView) findViewById(R.id.profile_data);

        // auth observer
        mAuthListener   = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    logged_in_or_not.setText("YES");
                    profile_data.setText("User Name: " + user.getDisplayName() + " , email: " + user.getEmail());
                }
                else if(user == null) {
                    // No user is signed in
                    logged_in_or_not.setText("NO");
                    profile_data.setText("User Name: null, email: null");
                }
                else {
                    // User is signed out
                    Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // test create user
        mAuth.createUserWithEmailAndPassword("testemail1@yahoo.com", "123123")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "auth fail", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "create user succeed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
