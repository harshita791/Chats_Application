package com.example.chats;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.chats.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    TextInputLayout input_name,input_email,input_pass;
    EditText email,pass;
    EditText username;
    Button register;

    FirebaseAuth mAuth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar=findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });




        input_name=findViewById(R.id.input_name);
        input_email=findViewById(R.id.input_email);
        input_pass=findViewById(R.id.input_pass);
        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        register=findViewById(R.id.register);


        mAuth=FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_user=username.getText().toString();
                String txt_email=email.getText().toString();
                String txt_pass=pass.getText().toString();

                if(TextUtils.isEmpty(txt_user)){
                    input_name.setError(getString(R.string.err_msg_name));
                }
                else {
                    input_name.setErrorEnabled(false);
                }
             /*   if(TextUtils.isEmpty(txt_user) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pass)){
                    Toast.makeText(Register.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }*/
             if(TextUtils.isEmpty(txt_email) && android.util.Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()){
                 input_email.setErrorEnabled(false);
             }
             else{
                 input_email.setError(getString(R.string.err_msg_email));
             }
             if(TextUtils.isEmpty(txt_pass) || txt_pass.length()<6){
                 input_pass.setError(getString(R.string.err_msg_password));
                    //Toast.makeText(Register.this, "password must be atleast 6 characters", Toast.LENGTH_SHORT).show();
                }
             else {
                 input_pass.setErrorEnabled(false);
             }
                    register(txt_user,txt_email,txt_pass);

            }
        });
    }

    private void register(final String username, String email, String password){

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                  FirebaseUser user = mAuth.getCurrentUser();
                                  String userid=user.getUid();

                                  reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);

                                HashMap<String,String> hashMap=new HashMap<>();
                                hashMap.put("id",userid);
                                hashMap.put("username",username);
                                hashMap.put("imageURL","default");
                                hashMap.put("search",username.toLowerCase());


                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){
                                            Intent i=new Intent(Register.this,Start.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                });
                            }
                        }
                    });
    }

}
