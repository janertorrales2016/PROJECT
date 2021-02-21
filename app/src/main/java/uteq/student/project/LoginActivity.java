package uteq.student.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends AppCompatActivity  {
    private EditText email;
    private EditText password;
    private Button login;
    private FirebaseAuth auth;

    SignInButton btSignIn;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
        login = findViewById(R.id.btnLogin);
        auth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                loginUser(txt_email, txt_password);
            }
        });

        //Asignacion variable
        btSignIn = findViewById(R.id.sign_in_button);
        //inicializar sig in option
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("316400243423-0cr0av79elmv1le122di0n7n93hqvhjv.apps.googleusercontent.com")
                .requestEmail()
                .build();
        //inicializar el cliente
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this,
                googleSignInOptions);
        btSignIn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view){
               //inicializar inicio in intent
               Intent intent = googleSignInClient.getSignInIntent();
               //inicializar actividad de resultado
               startActivityForResult(intent, 100);
           }
        });
        FirebaseUser  firebaseUser = auth.getCurrentUser();
        if(firebaseUser != null){
            startActivity(new  Intent(LoginActivity.this,
                    MainActivity.class)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //comprobar condicion
        if(requestCode == 100){
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            if(signInAccountTask.isSuccessful()){
                String s = "Ingreso Google corecto";
                displayToast(s);
                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask
                            .getResult(ApiException.class);
                    if(googleSignInAccount != null){
                        AuthCredential authCredential = GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken()
                                , null);
                        //chekear credenciales
                        auth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            startActivity(new Intent(LoginActivity.this,
                                                    MainActivity.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                            displayToast("Ingreso exitoso firebase");
                                        }else {
                                            displayToast("Ingreso fallido: " +task.getException()
                                            .getMessage());
                                        }
                                    }
                                });
                    }
                    }catch(ApiException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public void registro(View view){
        startActivity(new Intent(LoginActivity.this,
                RegisterActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
    private void loginUser(String email, String pass) {

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login correcto", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "ERROR USER OR PASSWORD", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




    public void loginFacebook(View view) {

    }

    public void loginTwitter(View view) {

    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            startActivity( new Intent(LoginActivity.this, StartActivity.class));
        }
        return super.onKeyDown(keyCode, event);
    }
}