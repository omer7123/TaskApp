package com.ripalay.taskapp.ui.board;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ripalay.taskapp.R;
import com.ripalay.taskapp.databinding.PagerBoardBinding;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {
    private pos pos;
    private String[] titles = new String[]{"News", "Fast", "Free", "Secure"};
    private Integer[] images = new Integer[]{R.drawable.ic_icon_telega,
            R.drawable.ic_icon_speed,
            R.drawable.ic_free,
            R.drawable.ic_secury};

    public void setPos(BoardAdapter.pos pos) {
        this.pos = pos;
        notifyDataSetChanged();
    }

    private String[] dess = new String[]{"Самая быстрая программа с последними новостями в реальном времени",
            "News отображжает новейшие новости быстрее всех",
            "News бесплатный. Без абонентской платы",
            "Приложение борется за вашу конфидициальность в мире высоких технологий"};

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(PagerBoardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private PagerBoardBinding binding;

        public ViewHolder(@NonNull PagerBoardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(int position) {
            binding.textTitle.setText(titles[position]);
            binding.imageView.setImageResource(images[position]);
            binding.textDesc.setText(dess[position]);

            pos.callBack(getAdapterPosition(),titles[position]);
        }
    }

    public interface pos {
        void callBack(int position,String length);
    }
}
