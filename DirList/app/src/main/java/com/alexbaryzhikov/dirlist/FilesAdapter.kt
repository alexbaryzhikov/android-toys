package com.alexbaryzhikov.dirlist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class FilesAdapter(private val context: Context, val onClick: (String) -> Unit)
    : RecyclerView.Adapter<FilesAdapter.FilesViewHolder>() {

    var fileDirItems: List<FileDirItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var folderDrawable = context.resources.getDrawable(R.drawable.ic_folder, context.theme)
    private var fileDrawable = context.resources.getDrawable(R.drawable.ic_file, context.theme)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.file_item, parent, false)
        return FilesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FilesViewHolder, position: Int) {
        val item = fileDirItems[position]
        holder.fileName.text = item.name
        if (item.isDirectory) {
            holder.fileIcon.setImageDrawable(folderDrawable)
        } else {
            holder.fileIcon.setImageDrawable(fileDrawable)
            val options = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .error(fileDrawable)
                    .centerCrop()
            Glide.with(context)
                    .load(item.path)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(options)
                    .into(holder.fileIcon)
        }
    }

    override fun getItemCount(): Int = fileDirItems.size

    inner class FilesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fileName: TextView = itemView.findViewById(R.id.file_name)
        val fileIcon: ImageView = itemView.findViewById(R.id.file_icon)

        init {
            itemView.setOnClickListener {
                val item = fileDirItems[adapterPosition]
                if (item.isDirectory) {
                    onClick(fileDirItems[adapterPosition].path)
                }
            }
        }
    }
}
