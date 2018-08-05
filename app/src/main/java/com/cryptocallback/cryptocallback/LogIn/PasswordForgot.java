package com.cryptocallback.cryptocallback.LogIn;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cryptocallback.cryptocallback.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordForgot extends AppCompatActivity {

    private EditText email;
    private Button reset;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forgot);

        email = (EditText) findViewById(R.id.tvEmailForgot);
        reset = (Button) findViewById(R.id.button_ForgotPass);
        firebaseAuth = FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_email = email.getText().toString().trim();

                if(user_email.equals("")){
                    Toast.makeText(PasswordForgot.this, "Please Enter Account Email", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(user_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PasswordForgot.this,"Password Reset Email Has Been Sent.",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(PasswordForgot.this,SignIn.class));
                            }else {
                                Toast.makeText(PasswordForgot.this,"Error Sending Password Reset",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });



    }
}

