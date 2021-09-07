package co.tiagoaguiar.atway.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class ATAdapter<V : ATViewHolder<M, B>, M, B : ViewBinding>(
  private val viewHolder: (ViewGroup) -> V,
  private val onItemClick: ((M) -> Unit)? = null
) : RecyclerView.Adapter<V>() {

  var items: MutableList<M> = mutableListOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V {
    return viewHolder(parent)
  }

  override fun onBindViewHolder(holder: V, position: Int) {
    holder.bind(items[position])
  }

  override fun getItemCount(): Int {
    return items.size
  }

}

abstract class ATViewHolder<M, B : ViewBinding>(
  bindingLaunch: (LayoutInflater, ViewGroup?, Boolean) -> ViewBinding,
  root: ViewGroup,
  val binding: B = bindingLaunch(LayoutInflater.from(root.context), root, false) as B
) : RecyclerView.ViewHolder(
  binding.root
) {

  abstract fun bind(item: M)

}