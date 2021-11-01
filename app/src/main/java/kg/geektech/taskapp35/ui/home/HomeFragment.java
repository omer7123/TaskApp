package kg.geektech.taskapp35.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.geektech.taskapp.R;
import com.geektech.taskapp.databinding.FragmentHomeBinding;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import kg.geektech.taskapp35.models.NewsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeFragment extends Fragment {
    private NewsAdapter adapter;
    private List<NewsModel> list;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private String email;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NewsAdapter();


        adapter.setClick(new NewsAdapter.onItemClick() {
            @Override
            public void onClickLong(int position) {

            }

            @Override
            public void onClick(int position) {

                //        Bundle bundle = new Bundle();
//        bundle.putSerializable("news", adapter.getList().get(position));
//                  NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
//                  FirebaseAuth mAuth;
//                  mAuth = FirebaseAuth.getInstance();
//                  FirebaseUser currentUser = mAuth.getCurrentUser();
//
                NewsModel news = adapter.getItem(position);
                if (news.getEmail() != null && news.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                    openFragment(news);
                } else {
                    Toast.makeText(requireContext(), news.getId(), Toast.LENGTH_SHORT).show();
                    news.setView_count(news.getView_count() + 1);
                    FirebaseFirestore.getInstance()
                            .collection("news")
                            .document(news.getId())
                            .update("view_count", FieldValue.increment(1));
                    openDemoFragment(news);
                }
                //navController.navigate(R.id.taskFragment, bundle);
//


//        getParentFragmentManager().setFragmentResultListener("edit_rk_news", getViewLifecycleOwner(), new FragmentResultListener() {
//            @Override
//            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
//                NewsModel news = (NewsModel) result.getSerializable("news");
//                news.setCreatedAt((Long) result.getSerializable("time"));
//                adapter.refactorItem(position, news);
//                //list.set(position, news);
//
//            }
//        });
            }

        });
        readDataLife();
    }

    private void openDemoFragment(NewsModel news) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("news", news);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.demoFragment,bundle);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fabBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(null);
            }
        });
//        getParentFragmentManager().setFragmentResultListener("rk_news", getViewLifecycleOwner(), new FragmentResultListener() {
//            @Override
//            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
//                NewsModel news = (NewsModel) result.getSerializable("news");
//                news.setCreatedAt((Long) result.getSerializable("time"));
//                adapter.addItem(news);
//                binding.progressBar.setVisibility(View.GONE);
//
//            }
//        });
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });

        initList();
    }


    private void initList() {
        // adapter.setLongClick(this);
        // adapter.setClick(this);
        binding.homeRv.setAdapter(adapter);


    }

    private void openFragment(NewsModel news) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);

        if (news != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("news", news);
            navController.navigate(R.id.taskFragment, bundle);
        } else {
            navController.navigate(R.id.taskFragment);
        }
    }

//    @Override
//    public void onClickLong(int position) {
//        AlertDialog.Builder alert = new AlertDialog.Builder(requireContext(), R.style.Theme_AppCompat_Light_Dialog_Alert);
//        alert.setTitle("Внимание!").
//                setMessage("Удалить ?").
//                setPositiveButton("да", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        adapter.removeItem(position);
//                    }
//                })
//                .setNegativeButton("нет", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).show();
//    }


    private void readData() {
        // binding.progressBar.setVisibility(View.VISIBLE);
        Log.e("TAG", "readData: ");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("news")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        list = snapshots.toObjects(NewsModel.class);
                        binding.progressBar.setVisibility(View.GONE);
                        Log.e("TAG", "readData: Success" + list);

                        adapter.addItems(list);

                    }
                });
    }

    private void readDataLife() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("news")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    list = new ArrayList<>();

                    for (DocumentSnapshot snapshot : snapshots) {
                        NewsModel news = snapshot.toObject(NewsModel.class);
                        news.setId(snapshot.getId());
                        list.add(news);
                    }

                    binding.progressBar.setVisibility(View.GONE);
                    adapter.addItems(list);
                });
    }
}


