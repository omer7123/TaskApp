package com.ripalay.taskapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ripalay.taskapp.databinding.FragmentNewsBinding;
import com.ripalay.taskapp.models.NewsModel;

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
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });

    }

    private void sendData() {
        String time = new SimpleDateFormat("HH:mm dd.MM.yyyy").format(Calendar.getInstance().getTime());
        Bundle bundle = new Bundle();
        String key = "rk_news";

        if (news == null) {
            String text = binding.editText.getText().toString();
            news = new NewsModel(text, time);
        } else {
            key = "edit_rk_news";
            news.setTitle(binding.editText.getText().toString().trim());
        }

        bundle.putSerializable("news", news);
        bundle.putSerializable("time", time);
        getParentFragmentManager().setFragmentResult(key, bundle);

        close();
    }

    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigateUp();
    }
}