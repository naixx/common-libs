package common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class SimpleListAdapter<T, B : ViewBinding>(
        private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> B,
        diffCallback: DiffUtil.ItemCallback<T?> = SimpleDiffCallback()
) : ListAdapter<T, SimpleListAdapter.ViewHolder<B>>(diffCallback) {

    class ViewHolder<V : ViewBinding>(itemView: View, val binding: V) :
            RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder<B> {
        val binding = bindingInflater(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
        val item = getItem(position)
        onBind(item, holder.binding, position)
    }

    abstract fun onBind(item: T, binding: B, position: Int)
}

/**
 * A convenience adapter for simple bindings
 */
open class SimpleListAdapter2<T, V : ViewBinding>(
        bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> V,
        val onBind2: V.(item: T, position: Int) -> Unit,
        diffCallback: DiffUtil.ItemCallback<T?> = SimpleDiffCallback()
) : SimpleListAdapter<T, V>(bindingInflater, diffCallback) {

    override fun onBind(item: T, binding: V, position: Int) {
        binding.onBind2(item, position)
    }
}
