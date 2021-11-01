package kg.geektech.taskapp35;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.geektech.taskapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.geektech.taskapp.databinding.FragmentNewsBinding;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import kg.geektech.taskapp35.models.NewsModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class NewsFragment extends Fragment {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FragmentNewsBinding binding;
    private NewsModel news = null;
    private Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(inflater);
        View view = binding.getRoot();

        if (getArguments() != null)
            news = (NewsModel) getArguments().getSerializable("news");
        if (news != null) {
            binding.editText.setText(news.getTitle());

            if (news.getImageUrl() != null) {
                Glide.with(binding.pictIv.getContext()).load(news.getImageUrl()).into(binding.pictIv);
            }else {
                binding.pictIv.setImageResource(R.drawable.ic_launcher_foreground);
            }
            if (news.getDescribe() != null) {
                binding.describeEt.setText(news.getDescribe());
            }
        }


//        Toast.makeText(requireContext(), news.getId(), Toast.LENGTH_SHORT).show();

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
        binding.setIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            binding.pictIv.setImageURI(uri);
        }
    }


    private void safeData() {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        Log.e("ololo", "Save data " + news);

        if (news == null) {
            Log.e("ololo", "Save data if " + news);

            String text = binding.editText.getText().toString();
            news = new NewsModel(text, System.currentTimeMillis(), mAuth.getCurrentUser().getEmail());
            news.setDescribe(binding.describeEt.getText().toString());
            Log.e("ololo", "Save data  = new " + news);
            if (uri != null) {
                uploadFile();
            } else {
                safeToFirestore(news);
            }
        } else {
            news.setTitle(binding.editText.getText().toString());

            update(news);
        }
    }

    private void uploadFile() {
        String uuid = UUID.randomUUID().toString();
        StorageReference reference = storage.getReference().child("images/" + uuid + ".jpg");
        UploadTask task = reference.putFile(uri);
        task.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
            }
        });
        Task<Uri> uriTask = task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    news.setImageUrl(task.getResult().toString());
                    safeToFirestore(news);
                } else {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void update(NewsModel news) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("news")
                .document(news.getId())
                .update("title", news.getTitle())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Log.e("ololo", "Update" + news);
                            Toast.makeText(requireContext(), "Успешно", Toast.LENGTH_SHORT).show();
                        } else {

                            Log.e("ololo", "update" + news);

                            Toast.makeText(requireContext(), "Неудачно", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        db.collection("news")
                .document(news.getId())
                .update("imageUrl",news.getImageUrl())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

        close();
    }

    private void safeToFirestore(NewsModel news) {
        Log.e("ololo", "safeToFirestore ");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("news")
                .add(news)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Log.e("ololo", "safeToFirestore" + news);

                        if (task.isSuccessful()) {
                            // loadUrl(task.getResult().getId());

                            Log.e("ololo", "Save data " + news);

                            Toast.makeText(requireContext(), "Успешно", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("ololo", "Save data" + news);

                            Toast.makeText(requireContext(), "Неудачно", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
        close();
    }

    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.navigation_home);
    }
}