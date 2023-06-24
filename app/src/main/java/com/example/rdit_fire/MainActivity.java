package com.example.rdit_fire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button btnSubmit;
    TextInputEditText etName, etContact;

    TextView tvResult;
    ContactAdapter cAdap;
    RecyclerView rvlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSubmit = findViewById(R.id.btnSubmit);
        etName = findViewById(R.id.etName);
        etContact = findViewById(R.id.etContact);
        // tvResult = findViewById(R.id.tvResult);

        rvlist = findViewById(R.id.rvList);
        rvlist.setHasFixedSize(true);
        rvlist.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Contact> options =
                new FirebaseRecyclerOptions.Builder<Contact>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Contacts"), Contact.class)
                        .setLifecycleOwner(this)
                        .build();
 cAdap = new ContactAdapter(options);
        rvlist.setAdapter(cAdap);

        /*
        FirebaseDatabase.getInstance()
                        .getReference()
                                .child("Contacts")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                                String text = "";
                                                for (DataSnapshot data:snapshot.getChildren())
                                                {
                                                  String name = data.child("username").getValue().toString();
                                                  String contact = data.child("usercontact").getValue().toString();

                                                  text = text + name+" : " + contact+"\n";
                                                }

                                                tvResult.setText(text);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
        */

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String contact = etContact.getText().toString().trim();

                if(!name.isEmpty() && !contact.isEmpty())
                {

                    HashMap<String, Object> data = new HashMap<>();
                    data.put("username",name);
                    data.put("usercontact", contact);

                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Contacts")
                            .child(contact)
                            .updateChildren(data)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(MainActivity.this, "Contact Added", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                }
                else
                {
                    if(name.isEmpty())
                    {
                        etName.setError("Name can't be empty.");
                    }
                    if(contact.isEmpty())
                    {
                        etContact.setError("Contact can't be empty.");
                    }
                }
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // this method is called
                // when the item is moved.
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.

                // below line is to display our snackbar with action.
                Toast.makeText(MainActivity.this, "Swiped", Toast.LENGTH_SHORT).show();
            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(rvlist);


    }

    @Override
    protected void onStart() {
        super.onStart();
        cAdap.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cAdap.stopListening();
    }
}