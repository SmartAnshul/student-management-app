// StudentAdapter.java
package com.techmnc.studentmanagementapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.techmnc.studentmanagementapp.activity.removeStudent;

import java.util.List;


public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<removeStudent.Student> studentList;
    private FirebaseFirestore firestore;

    public StudentAdapter(List<removeStudent.Student> studentList, FirebaseFirestore firestore) {
        this.studentList = studentList;
        this.firestore = firestore;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        removeStudent.Student student = studentList.get(position);
        holder.bind(student);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView idTextView;
        private Button removeButton;
        private Button blockButton;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            idTextView = itemView.findViewById(R.id.idTextView);
            removeButton = itemView.findViewById(R.id.removeButton);
            blockButton = itemView.findViewById(R.id.blockButton);
        }

        public void bind(removeStudent.Student student) {
            // Set data to views
            nameTextView.setText(student.getName());
            idTextView.setText(student.getId());

            // Set click listeners for remove and block buttons
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle remove button click
                    removeStudentFromFirebase(student);
                }
            });

            blockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle block button click
                    blockStudentInFirebase(student);
                }
            });
        }

        private void removeStudentFromFirebase(removeStudent.Student student) {
            // Implement code to remove the student from Firebase Firestore
            firestore.collection("students").document(student.getId()).delete()
                    .addOnSuccessListener(aVoid -> {
                        // Removal from Firebase successful
                        // Remove from the local list and notify the adapter
                        int position = getAdapterPosition();
                        studentList.remove(position);
                        notifyItemRemoved(position);
                    })
                    .addOnFailureListener(e -> {
                        // Removal from Firebase failed, handle the error
                    });
        }

        private void blockStudentInFirebase(removeStudent.Student student) {
            // Implement code to update the student status to "blocked" in Firebase Firestore
            // You need to use the 'firestore' object to perform Firestore operations
            // For example:
            // firestore.collection("students").document(student.getId()).update("status", "blocked");
            // You can also update the local data accordingly
            // For example, set a 'blocked' flag in the Student class and update the UI accordingly
        }
    }
}
