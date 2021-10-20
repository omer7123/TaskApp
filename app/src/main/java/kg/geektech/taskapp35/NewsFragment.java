package kg.geektech.taskapp35;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.geektech.taskapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.geektech.taskapp.databinding.FragmentNewsBinding;

import kg.geektech.taskapp35.models.NewsModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;
    private NewsModel news = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(inflater);
        if (getArguments() != null)
            news = (NewsModel) getArguments().getSerializable("news");
        View view = binding.getRoot();
        if (news != null) {
            binding.editText.setText(news.getTitle());
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.editText.requestFocus();
        if (binding.editText.requestFocus()) {
            //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.editText, InputMethodManager.SHOW_IMPLICIT);
        }
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                safeData();
            }
        });

    }

    private void safeData() {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();

        //String time = new SimpleDateFormat("HH:mm dd.MM.yyyy").format(Calendar.getInstance().getTime());
        Bundle bundle = new Bundle();
        String key = "rk_news";

        if (news == null) {
            String text = binding.editText.getText().toString();
            news = new NewsModel(text, System.currentTimeMillis(), mAuth.getCurrentUser().getEmail());
        } else {
            key = "edit_rk_news";
            news.setTitle(binding.editText.getText().toString().trim());
        }
        safeToFirestore(news);

        bundle.putSerializable("news", news);
        bundle.putSerializable("time", System.currentTimeMillis());
        getParentFragmentManager().setFragmentResult(key, bundle);

    }

    private void safeToFirestore(NewsModel news) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("news")
                .add(news)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Успешно", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Неудачно", Toast.LENGTH_SHORT).show();
                        }
                        close();
                    }
                });
    }

    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigateUp();
    }
}