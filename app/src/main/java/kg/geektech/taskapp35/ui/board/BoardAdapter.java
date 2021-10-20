package kg.geektech.taskapp35.ui.board;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geektech.taskapp.R;
import com.geektech.taskapp.databinding.PagerBoardBinding;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {
    private String[] titles = new String[]{"News", "Fast", "Free", "Secure"};
    private Integer[] images = new Integer[]{R.raw.news,
            R.raw.speed,
            R.raw.free,
            R.raw.secure};



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
            binding.imageView.setAnimation(images[position]);
            binding.textDesc.setText(dess[position]);
        }
    }

}
