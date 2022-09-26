package com.example.marvelapp.presentation.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.databinding.ItemParentDetailBinding
import com.example.marvelapp.framework.imageLoader.ImageLoader

class DetailParentAdapter(
    private val detailParentList: List<DetailParentViewEnd>,
    private val imageLoader: ImageLoader
) : RecyclerView.Adapter<DetailParentAdapter.DetailParentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailParentViewHolder {
        return DetailParentViewHolder.create(parent, imageLoader)
    }

    override fun onBindViewHolder(holder: DetailParentViewHolder, position: Int) {
        return holder.bind(detailParentList[position])
    }

    override fun getItemCount() = detailParentList.size

    class DetailParentViewHolder(
        itemBinding: ItemParentDetailBinding,
        private val imageLoader: ImageLoader
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        private val textItemCategory = itemBinding.TextItemCategory
        private val recyclerChildDetail = itemBinding.recyclerChildDetail

        fun bind(detailParentViewEnd: DetailParentViewEnd) {
            textItemCategory.text =
                itemView.context.getText(detailParentViewEnd.categoryStringResId)

            recyclerChildDetail.run {
                setHasFixedSize(true)
                adapter = DetailChildAdapter(detailParentViewEnd.detailChildList, imageLoader)
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                imageLoader: ImageLoader
            ): DetailParentViewHolder {

                val itemBinding = ItemParentDetailBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                return DetailParentViewHolder(itemBinding, imageLoader)
            }
        }
    }
}