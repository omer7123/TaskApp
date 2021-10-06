package com.ripalay.taskapp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ripalay.taskapp.databinding.ItemHomeBinding;
import com.ripalay.taskapp.databinding.ItemHomeGrayBinding;
import com.ripalay.taskapp.models.NewsModel;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NewsModel> list = new ArrayList<>();
    private onItemClick longClick;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemHomeBinding binding;

        public ViewHolder(@NonNull ItemHomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(NewsModel s) {
            binding.textTitle.setText(s.getTitle());
            binding.timeTv.setText(s.getTime());
            binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClick.onClickLong(getAdapterPosition());
                    return false;
                }
            });
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onClick(getAdapterPosition());
                }
            });
        }
    }


    public class ViewHolderGray extends RecyclerView.ViewHolder {
        private ItemHomeGrayBinding binding;

        public ViewHolderGray(@NonNull ItemHomeGrayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(NewsModel s) {
            binding.textTitle.setText(s.getTitle());
            binding.timeTv.setText(s.getTime());
            binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClick.onClickLong(getAdapterPosition());
                    return false;
                }
            });
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onClick(getAdapterPosition());
                }
            });
        }
    }

    public interface onItemClick {
        void onClickLong(int position);

        void onClick(int position);

    }

}
