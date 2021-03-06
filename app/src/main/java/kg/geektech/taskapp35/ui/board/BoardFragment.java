package kg.geektech.taskapp35.ui.board;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayoutMediator;
import kg.geektech.taskapp35.Prefs;
import com.geektech.taskapp.R;
import com.geektech.taskapp.databinding.FragmentBoardBinding;

public class BoardFragment extends Fragment {
    private FragmentBoardBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBoardBinding.inflate(inflater);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewPager();
        initListenner();
        initOnBack();


    }

    private void initOnBack() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });
    }

    private void initListenner() {
        binding.imageView.setOnClickListener(v -> {
            Prefs prefs = new Prefs(requireContext());
            prefs.saveBoardState();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.loginFragment);
        });
        binding.startBt.setOnClickListener(v -> {
            Prefs prefs = new Prefs(requireContext());
            prefs.saveBoardState();
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.loginFragment);
        });

    }


    private void initViewPager() {
        BoardAdapter adapter = new BoardAdapter();
        binding.viewPager.setAdapter(adapter);

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 3) {
                    Log.e("gf", "Visible");
                    binding.startBt.setVisibility(View.VISIBLE);
                } else {
                    Log.e("gf", "Gone");
                    binding.startBt.setVisibility(View.GONE);
                }
            }
        });

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
        }).attach();

    }



}