package uteq.student.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import uteq.student.project.R;

public class AddDisp extends AppCompatActivity {
    private EditText mac, alias;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_disp);
        mac= (EditText) findViewById(R.id.txtMac);
        alias= (EditText)findViewById(R.id.txtAlias);
    }
    private long hijos;
    public void btnSaveDisp(View view){
        try {
            HashMap<String, Object> map= new HashMap<>();
            map.put("alias", alias.getText().toString());
            map.put("mac", mac.getText().toString());
            mReference = FirebaseDatabase.getInstance().getReference("dispositivos").child(user.getUid());
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                     hijos= snapshot.getChildrenCount();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            FirebaseDatabase.getInstance().getReference().child("dispositivos").child(user.getUid()).child(String.valueOf(hijos+1)).setValue(map);
        }catch (Exception e){

        }

    }

}