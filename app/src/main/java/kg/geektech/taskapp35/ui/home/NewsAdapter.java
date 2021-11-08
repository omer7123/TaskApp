package kg.geektech.taskapp35.ui.home;

import android.os.Build;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.geektech.taskapp.R;
import com.geektech.taskapp.databinding.ItemHomeBinding;
import com.geektech.taskapp.databinding.ItemHomeGrayBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.ocpsoft.prettytime.PrettyTime;

import kg.geektech.taskapp35.models.NewsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NewsModel> list = new ArrayList<>();
    private onItemClick longClick;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private onItemClick click;

    private final int TYPE_BLACK = 0;
    private final int TYPE_WHITE = 1;

    public void setLongClick(onItemClick longClick) {
        this.longClick = longClick;
    }

    public void setClick(onItemClick click) {
        this.click = click;
    }

    public ArrayList<NewsModel> getList() {
        return list;
    }

    @Override
    public int getItemViewType(int position) {
        int i = 0;
        if (position % 2 == 0) {
            i = 1;
        } else {
            i = 0;
        }

        return i;
    }

    public void removeItem(int position) {
        this.list.remove(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new ViewHolderGray(ItemHomeGrayBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            case 0:
                return new ViewHolder(ItemHomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                ((ViewHolder) holder).onBind(list.get(position));
                break;
            case 1:
                ((ViewHolderGray) holder).onBind(list.get(position));
                break;
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(NewsModel news) {
        list.add(0, news);
        notifyItemInserted(0);
    }

    public void refactorItem(int position, NewsModel news) {
        this.list.remove(position);
        this.list.add(news);
        notifyDataSetChanged();
    }

    public void addItems(List<NewsModel> list) {
        // list.sort(Comparator.comparing(NewsModel::getCreatedAt));
        // Collections.reverse(list);
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();

    }

    public NewsModel getItem(int position) {
        return list.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemHomeBinding binding;
        private String string;

        public ViewHolder(@NonNull ItemHomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onClick(getAdapterPosition());
                }
            });
        }

        public void onBind(NewsModel s) {

            long starttime = s.getCreatedAt();
            long endtime = System.currentTimeMillis();
            long lResultDate = endtime - starttime;
            Date resultdate = new Date(lResultDate);
            PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
            String ago = prettyTime.format(new Date(starttime));

            //            if (lResultDate < 600000) {
            binding.timeTv.setText(ago);
            binding.emailTv.setText(s.getEmail());
            binding.textTitle.setText(s.getTitle());
            Glide.with(binding.pictIv.getContext())
                    .load(s.getImageUrl())
                    .override(1000, 1000)
                    .into(binding.pictIv);
            binding.viewCountTv.setText(String.valueOf(s.getView_count()));

//            binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    longClick.onClickLong(getAdapterPosition());
//                    return false;
//                }
//            });

        }
    }


    public class ViewHolderGray extends RecyclerView.ViewHolder {
        private ItemHomeGrayBinding binding;
        private String string;

        public ViewHolderGray(@NonNull ItemHomeGrayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onClick(getAdapterPosition());
                }
            });
        }

        public void onBind(NewsModel s) {
            long starttime = s.getCreatedAt();
            PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
            String ago = prettyTime.format(new Date(starttime));
            binding.timeTv.setText(ago);
            binding.textTitle.setText(s.getTitle());
            binding.emailTv.setText(s.getEmail());
            Glide.with(binding.pictIv.getContext())
                    .load(s.getImageUrl())
                    .override(1000, 1000)
                    .into(binding.pictIv);

            binding.viewCountTv.setText(String.valueOf(s.getView_count()));

           /* binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClick.onClickLong(getAdapterPosition());
                    return false;
                }
            });*/

        }
    }

    public interface onItemClick {
        void onClickLong(int position);

        void onClick(int position);
    }

}
