package com.anhtong8x.logingoogle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GoogleSignInClient mGoogleSignInClient;
    public static int RC_SIGN_IN = 011;
    SignInButton mSignInButton;

    TextView txtEmail, txtName;
    ImageView imgEmail;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mSignInButton = findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_STANDARD);
        mSignInButton.setOnClickListener(this);

        txtEmail = findViewById(R.id.txtEmail);
        txtName = findViewById(R.id.txtUserName);
        imgEmail = findViewById(R.id.imgEmail);
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);

        // hidden view
        hiddenView(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.btnLogout:
                logoutGmail();
                break;
            // ...
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN ) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d("mLog", account.getDisplayName());

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("HandLogin", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(GoogleSignInAccount account) {
        hiddenView(true);

        txtName.setText(account.getDisplayName());
        txtEmail.setText(account.getEmail());
        Picasso.with(this).load(account.getPhotoUrl()).into(imgEmail);
    }

    private void logoutGmail() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        hiddenView(false);
                    }
                });
    }

    private void hiddenView(boolean isShow) {
        if(!isShow){
            txtEmail.setVisibility(View.INVISIBLE);
            txtName.setVisibility(View.INVISIBLE);
            imgEmail.setVisibility(View.INVISIBLE);
            txtName.setText("");
            txtName.setText("");
            imgEmail.setImageURI(null);
            btnLogout.setVisibility(View.INVISIBLE);
            mSignInButton.setVisibility(View.VISIBLE);

        }else {
            txtEmail.setVisibility(View.VISIBLE);
            txtName.setVisibility(View.VISIBLE);
            imgEmail.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
            mSignInButton.setVisibility(View.INVISIBLE);
        }

    }

}