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
    ArrayList<String> keyList, names;
    int index, size, a=0;

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
        names = new ArrayList<>();
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
                size = (int) dataSnapshot.getChildrenCount() - 1;

                for(int num = 0; num <= size; num++){
                    String getName = dataSnapshot.child(keyList.get(num)).child("fullname").getValue().toString();
                    names.add(getName);

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
            Boolean a = true;
            int age;
            try {
                fullname = eFullname.getText().toString();
                age = Integer.parseInt(eAge.getText().toString());
                gender = eGender.getText().toString();

                for(int nameList = 0; nameList < names.size(); nameList++){
                    String checkName = names.get(nameList);
                    if(checkName.equalsIgnoreCase(fullname)){
                        a = false;
                    }
                }
                if(a == false){
                    Toast.makeText(this, "Fullname exists", Toast.LENGTH_LONG).show();
                }else{
                    String key = person.push().getKey();
                    Person p_info = new Person(fullname, age, gender);
                    person.child(key).setValue(p_info);
                    keyList.add(key);
                    names.add(fullname);
                    Toast.makeText(this, "Record saved", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error saving data", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void displayInfo(View v){
        String fullname;
        fullname = eFullname.getText().toString();
        for(int nameList = 0; nameList < names.size(); nameList++){
            String checkName = names.get(nameList);
            if(checkName.equals(fullname)){
                 a = nameList;
            }
        }

        if(a!=0) {
            person.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String personName = dataSnapshot.child(keyList.get(a)).child("fullname").getValue().toString();
                    String personAge = dataSnapshot.child(keyList.get(a)).child("age").getValue().toString();
                    String personGender = dataSnapshot.child(keyList.get(a)).child("gender").getValue().toString();
                    a = 0;
                    tFullname.setText(personName);
                    tAge.setText(personAge);
                    tGender.setText(personGender);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            Toast.makeText(this,"Record does not exist", Toast.LENGTH_LONG).show();
        }

    }
}
