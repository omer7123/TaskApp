package kg.geektech.taskapp35.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.geektech.taskapp.R;
import com.geektech.taskapp.databinding.FragmentDashboardBinding;
import com.geektech.taskapp.databinding.FragmentDemoBinding;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import kg.geektech.taskapp35.models.NewsModel;

public class DemoFragment extends Fragment {

    private FragmentDemoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDemoBinding.inflate(inflater);
        View view = binding.getRoot();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        getParentFragmentManager().setFragmentResultListener("news", getViewLifecycleOwner(), new FragmentResultListener() {
//            @Override
//            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
//                NewsModel news = result.getSerializable("")
//            }
//        });
        NewsModel news;
        news = (NewsModel) getArguments().getSerializable("news");
        binding.titleTv.setText(news.getTitle());
        Glide.with(binding.pictIv.getContext()).load(news.getImageUrl()).into(binding.pictIv);

        SimpleDateFormat time = new SimpleDateFormat("dd.MM.yyyy");
        if (news.getDescribe() != null)
            binding.describeTv.setText(news.getDescribe().toString());
        binding.dateTv.setText(time.format(news.getCreatedAt()));
        binding.viewTv.setText(String.valueOf(news.getView_count()));
        binding.emailTv.setText(news.getEmail());
    }
}