package Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nutrimind.HomeActivity;
import com.example.nutrimind.R;

public class Meditation extends Fragment {

    // Constructor: Required empty public constructor for Fragment
    public Meditation() {
        // Required empty public constructor
    }

    // onCreateView method(Called to create and inflate the fragment's view)
    //inflater is used to convert the XML layout into actual views.
    // container is the parent view group that will hold this fragment’s view.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment (R.layout.fragment_meditation)
        // The 'false' here means that the inflated view will not be directly attached to the parent container (container).
        View view = inflater.inflate(R.layout.fragment_meditation, container, false);

        // Find the back button in the layout by its ID
        Button btnBackToHome = view.findViewById(R.id.btnBackToHome);

        // Set a click listener for the back button
        btnBackToHome.setOnClickListener(v -> {
            // Create an Intent to open the HomeActivity
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);  // Start the HomeActivity when the button is clicked
        });

        // Return the inflated view
        return view;
    }
}
