package com.buenafe.apriljoy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference person;
    EditText eFullname, eGender, eAge;
    TextView tFullname, tGender, tAge;
    ArrayList<String> keyList;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance();
        person = db.getReference("info");

        eFullname = findViewById(R.id.etFullname);
        eAge = findViewById(R.id.etAge);
        eGender = findViewById(R.id.etGender);

        tFullname = findViewById(R.id.displayFullname);
        tAge = findViewById(R.id.displayAge);
        tGender = findViewById(R.id.displayGender);
        keyList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        person.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ss: dataSnapshot.getChildren()) {
                    keyList.add(ss.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void saveRecord(View v){
        if(v.getId()==R.id.saveBtn) {
            String fullname, gender;
            int age;
            try {

                fullname = eFullname.getText().toString();
                age = Integer.parseInt(eAge.getText().toString());
                gender = eGender.getText().toString();

                Person p_info = new Person(fullname, age, gender);
                String key = person.push().getKey();
                person.child(key).setValue(p_info);
                keyList.add(key);

                Toast.makeText(this, "Record saved", Toast.LENGTH_LONG).show();


            } catch (Exception e) {
                Toast.makeText(this, "Error saving data", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void displayInfo(View v){

            person.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    index = (int) dataSnapshot.getChildrenCount() - 1;
                    Person per = dataSnapshot.child(keyList.get(index)).getValue(Person.class);
                    tFullname.setText(per.getFullname().toString());
                    tAge.setText(per.getAge().toString());
                    tGender.setText(per.getAge().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }
}
