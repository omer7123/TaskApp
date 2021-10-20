package kg.geektech.taskapp35.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.geektech.taskapp.R;
import com.geektech.taskapp.databinding.FragmentHomeBinding;
import kg.geektech.taskapp35.models.NewsModel;

import java.util.List;

public class HomeFragment extends Fragment implements NewsAdapter.onItemClick {
    private NewsAdapter adapter;
    private String email;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new NewsAdapter();
        readData();
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
                openFragment();
            }
        });
        getParentFragmentManager().setFragmentResultListener("rk_news", getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                NewsModel news = (NewsModel) result.getSerializable("news");
                news.setCreatedAt((Long) result.getSerializable("time"));
                adapter.addItem(news);
                binding.progressBar.setVisibility(View.GONE);

            }
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });

        initList();
    }


    private void initList() {
        adapter.setLongClick(this);
        adapter.setClick(this);
        binding.homeRv.setAdapter(adapter);


    }

    private void openFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.taskFragment);
    }

    @Override
    public void onClickLong(int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(requireContext(), R.style.Theme_AppCompat_Light_Dialog_Alert);
        alert.setTitle("Внимание!").
                setMessage("Удалить ?").
                setPositiveButton("да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.removeItem(position);
                    }
                })
                .setNegativeButton("нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("news", adapter.getList().get(position));
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //if(email.equals(mAuth.getCurrentUser().getEmail())) {

            navController.navigate(R.id.taskFragment, bundle);

            getParentFragmentManager().setFragmentResultListener("edit_rk_news", getViewLifecycleOwner(), new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                    NewsModel news = (NewsModel) result.getSerializable("news");
                    news.setCreatedAt((Long) result.getSerializable("time"));
                    adapter.refactorItem(position, news);
                }
            });
        //}
    }

    private void readData(){
       // binding.progressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("news").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(QuerySnapshot snapshots) {
                List<NewsModel> list = snapshots.toObjects(NewsModel.class);
                binding.progressBar.setVisibility(View.GONE);
                adapter.addItems(list);

            }
        });
    }
}


