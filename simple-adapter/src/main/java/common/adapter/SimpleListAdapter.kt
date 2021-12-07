/*
 * Copyright 2021 Rostislav Chekan
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

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
 * A convenience adapter for simple inline bindings
 */
open class SimpleListAdapter2<T, V : ViewBinding>(
        bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> V,
        diffCallback: DiffUtil.ItemCallback<T?> = SimpleDiffCallback(),
        val onBind: V.(item: T, position: Int) -> Unit
) : SimpleListAdapter<T, V>(bindingInflater, diffCallback) {

    override fun onBind(item: T, binding: V, position: Int) {
        binding.onBind(item, position)
    }
}
