package com.guilardi.baking;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.guilardi.baking.data.Recipe;
import com.guilardi.baking.utilities.Helper;
import com.squareup.picasso.Picasso;

/**
 * Created by deguilardi on 7/20/18.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesAdapterViewHolder> {

    Activity mContext;
    private List<Recipe> mData;
    final private RecipesAdapterOnClickHandler mClickHandler;

    public interface RecipesAdapterOnClickHandler {
        void onClick(int position, RecipesAdapterViewHolder adapterViewHolder);
    }

    public RecipesAdapter(@NonNull Activity context, RecipesAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipes_list_item, viewGroup, false);
        view.setFocusable(true);
        return new RecipesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesAdapterViewHolder recipesAdapterViewHolder, int position) {
        int numColumns = Helper.isTablet(mContext) ? 3 : 1;

        // parse te result
        Recipe recipe = mData.get(position);
        recipesAdapterViewHolder.mName.setText(recipe.getName());
        if(!recipe.getImage().equals("")) {
            Picasso.with(mContext).load(recipe.getImage())
//                    .resize(sizeW, sizeH)
                    .centerCrop()
                    .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.image_not_found)
                    .into(recipesAdapterViewHolder.mThumb);
        }
    }

    @Override
    public int getItemCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    public void swapData(List<Recipe> data) {
        mData = data;
        notifyDataSetChanged();
    }

//    public void swapData(Cursor cursor){
//        mData = new Movies(cursor);
//        notifyDataSetChanged();
//    }

    public List<Recipe> getData(){
        return mData;
    }

    public class RecipesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.thumb_view) ImageView mThumb;
        @BindView(R.id.name) TextView mName;

        RecipesAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandler.onClick(position, this);
        }
    }
}