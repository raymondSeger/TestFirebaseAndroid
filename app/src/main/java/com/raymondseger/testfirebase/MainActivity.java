package com.raymondseger.testfirebase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.raymondseger.testfirebase.User.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // firebase analytic
    private FirebaseAnalytics mFirebaseAnalytics;

    // firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // firebase remote config
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

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
        /*
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
        */

        // test login user
        mAuth.signInWithEmailAndPassword("testemail1@yahoo.com", "123123")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "login fail", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "login success", Toast.LENGTH_SHORT).show();

                            AuthCredential credential = EmailAuthProvider.getCredential("testemail1@yahoo.com", "123123");

                            // Prompt the user to re-provide their sign-in credentials
                            FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(MainActivity.this, "User re-authenticated.", Toast.LENGTH_SHORT).show();

                                            // update profile
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName("Raymond Goldman")
                                                    .build();
                                            FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);

                                        }
                                    });
                        }
                    }

                });

        // set database realtime
        // Write a message to the database
        /*
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("test_message");
        myRef.push().setValue("test_value_2"); // test string value
        User user = new User("testName", "testEmail");
        myRef.push().setValue(user); // test simple Class

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Object value = dataSnapshot.getValue();
                Toast.makeText(MainActivity.this, value.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        */

        // firebase storage
        /*
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef             = storage.getReferenceFromUrl("gs://project-4354686347802697840.appspot.com");
        StorageReference picture_1              = storageRef.child("1462070121170.jpg");
        StorageReference picture_2              = storageRef.child("testFolder1/test.jpg");
        StorageReference picture_does_not_exist = storageRef.child("testFolder1/test2.jpg");

        picture_1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("BILLY_TAG", uri.toString() );
            }
        });

        Toast.makeText(MainActivity.this, picture_1.getPath() + " , " + picture_2.getPath() + " , " + picture_does_not_exist.getPath(), Toast.LENGTH_SHORT).show();
        */

        // remote config
        /*
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        mFirebaseRemoteConfig.fetch(5)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mFirebaseRemoteConfig.activateFetched();
                        Toast.makeText(MainActivity.this, mFirebaseRemoteConfig.getString("promo_enabled") , Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
        */

        // test firebase crash
        FirebaseCrash.report(new Exception("My first Android non-fatal error"));

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
