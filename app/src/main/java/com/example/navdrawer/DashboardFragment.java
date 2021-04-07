package com.example.navdrawer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardFragment extends Fragment {

    Button btn_signOut;
    FirebaseUser user;
    FirebaseFirestore fireStore;
    NavController navController;
    TextView txt_box;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = requireActivity().getIntent();
        user = intent.getParcelableExtra("user");
        fireStore = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_signOut = view.findViewById(R.id.btn_signOut);
        txt_box = view.findViewById(R.id.txt_messsage);

        navController = Navigation.findNavController(getActivity(),R.id.host_fragment);

        readFireStore();

        btn_signOut.setOnClickListener(view1 -> {

            FirebaseAuth.getInstance().signOut();
            requireActivity().finish();
            startActivity(new Intent(requireActivity(), DefaultActivity.class));

        });
    }

    public void readFireStore()
    {
        DocumentReference docRef = fireStore.collection("User").document(user.getUid());

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                DocumentSnapshot doc = task.getResult();

                if (doc.exists())
                {
                    Log.d("DashboardFragment",doc.getData().toString());

                    txt_box.setText("Welcome "+doc.get("Name") + ", have a good day!");


                }

            }
        });
    }
}